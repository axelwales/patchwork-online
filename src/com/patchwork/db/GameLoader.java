package com.patchwork.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.patchwork.game.logic.Action;
import com.patchwork.game.logic.Command;
import com.patchwork.game.logic.CommandBuilder;
import com.patchwork.game.logic.StartGame;
import com.patchwork.game.models.ActionInterface;
import com.patchwork.game.models.Game;
import com.patchwork.game.models.GameState;
import com.patchwork.game.models.Player;
import com.patchwork.game.models.PlayerBoard;
import com.patchwork.game.models.TimeTrack;
import com.patchwork.game.models.patches.Patch;
import com.patchwork.game.models.patches.PatchList;
import com.patchwork.game.models.patches.Patches;
import com.patchwork.game.models.patches.Point;

public class GameLoader {
	public static final String listQuery = "SELECT g.id AS gameid, g.name AS gamename, g.isstarted AS started, p.username AS user1, q.username AS user2 " +
											"FROM Games g " +
											"INNER JOIN Players p ON g.id=p.gameid AND p.currentplayer=1 " +
											"LEFT OUTER JOIN Players q ON g.id=q.gameid AND q.currentplayer=0 ";
	public static final String allGamesListQuery = listQuery + "WHERE g.isstarted=0";
	public static final String yourGamesListQuery = listQuery + "WHERE p.username=? OR q.username=?";
	public static final String gameQuery = "SELECT g.name, g.isstarted, g.iscomplete, g.boardbonus FROM Games g WHERE g.id=?";
	public static final String playersQuery = "SELECT p.id, p.username, p.income, p.position, p.buttons, p.currentplayer, p.isai, p.boardbonus, p.board " +
												"FROM Players p " +
												"WHERE p.gameid=?";
	public static final String actionsQuery = "SELECT a.id, a.playerid, a.position " +
												"FROM Actions a " +
												"WHERE a.gameid=? AND iscomplete=0";
	public static final String commandsQuery = "SELECT c.id, c.name, c.params, c.position " +
												"FROM Commands c " +
												"WHERE c.actionid=? AND iscomplete=0";
	public static final String gamePatchesQuery = "SELECT g.id as \"gid\", p.patchid AS \"pid\", g.position " +
												"FROM GamePatches g " +
												"INNER JOIN Patches p ON g.patchid=p.id " +
												"WHERE g.gameid=? AND g.waschosen = 0";
	public static final String playerPatchesQuery = "SELECT p.id AS \"pid\", q.patchid AS \"qid\", p.columnnumber AS \"col\", p.rownumber AS \"row\" " +
												"FROM PlayerPatches p " +
												"INNER JOIN Patches q ON p.patchid=q.id " +
												"WHERE p.playerid=?";
	public static final String timeTracksQuery = "SELECT t.id, t.nextbonus, t.claimedbonuses " +
												"FROM TimeTracks t " +
												"WHERE t.gameid=?";
	
	public static boolean joinGame(Long gameid, String username) {
		Game[] games = getGames(null, gameid);
		if(games[0] == null)
			return false;
		if(games[0].players.length == 2)
			return false;
		createPlayer(games[0],gameid,false,username);
		return true;
	}
	
	public static boolean startGame(Long gameid, String username) {
		Game[] games = getGames(null, gameid);
		if(games == null)
			return false;
		if(games[0].players.length != 2)
			return false;
		boolean isPlayer = false;
		for( Player player : games[0].players) {
			if(player.username.equals(username))
				isPlayer = true;
		}
		if( isPlayer == false )
			return false;
		Game game = games[0];
		if (StartGame.startGame(game.state) == false)
			return false;
		updateGame(game);
		return true;
	}
	
	public static void updateGame(Game game) {
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		values.put("isstarted", game.isstarted);
		values.put("iscomplete", game.iscomplete);
		values.put("boardbonus", game.state.boardBonus);
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();
			ReadWriteValidator.startWrite(con, game.id);
			StatementProcessor.executePreparedUpdateStatement("Games", values, game.id, con);
			updatePlayers(game, game.players, con);
			updateState(game, game.state, con);
			ReadWriteValidator.endWrite(con, game.id);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {}
		} finally {
			if(con != null)
				try { con.close();
				} catch (SQLException e) {}
		}
	}

	private static void updateState(Game game, GameState state, Connection con) throws SQLException {
		if(state == null)
			return;
		updateActions(game, state.actionQueue, con);
		updateActions(game, state.completedActionQueue, con);
		updateGamePatches(game, state.patches, con);
		updateTimeTrack(game, state.track, con);
	}

	private static void updateActions(Game game, LinkedList<ActionInterface> actionQueue,
			Connection con) throws SQLException {
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		Action a;
		int i = 0;
		int iscomplete;
		for(ActionInterface ai : actionQueue) {
			if(ai == null)
				continue;
			a = (Action) ai;
			if(a.id == 0)
				insertAction(game, a, i, con);
			else {
				values.clear();
				values.put("position", i);
				iscomplete = (a.isComplete) ? 1 : 0;
				values.put("iscomplete", iscomplete);
				StatementProcessor.executePreparedUpdateStatement("Actions", values, a.id, con);
				updateCommands(game, a.commands, con);
				updateCommands(game, a.completeCommands, con);
			}
			i++;
		}
	}

	private static void insertAction(Game game, Action a, int position, Connection con) throws SQLException {
		a.id = IdManager.getNextId(con);
		if(a.id == -1) {
			con.close();
			throw new SQLException("failed to get id");
		}
		int iscomplete = (a.isComplete) ? 1 : 0;
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		
		values.put("id", a.id);
		values.put("playerid", a.player.id);
		values.put("gameid", game.id);
		values.put("position", position);
		values.put("iscomplete", iscomplete);
		
		StatementProcessor.executePreparedInsertStatement("Actions", values, con);
		
		int i = 0;
		for(Command c : a.commands) {
			insertCommand(game, c, i, con);
			i++;
		}
	}

	private static void updateCommands(Game game, LinkedList<Command> commands,
			Connection con) throws SQLException {
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		int i = 0;
		int iscomplete;
		for(Command c : commands) {
			if(c == null)
				continue;
			if(c.id == 0)
				insertCommand(game, c, i, con);
			else {
				values.clear();
				values.put("position", i);
				values.put("params", c.parametersToString());
				iscomplete = (c.isCompleted) ? 1 : 0;
				values.put("iscomplete", iscomplete);
				StatementProcessor.executePreparedUpdateStatement("Commands", values, c.id, con);
			}
			i++;
		}
	}

	private static void insertCommand(Game game, Command c, int position, Connection con) throws SQLException {
		c.id = IdManager.getNextId(con);
		if(c.id == -1) {
			con.close();
			throw new SQLException("failed to get id");
		}
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		
		values.put("id", c.id);
		values.put("actionid", c.action.id);
		values.put("name", c.name);
		values.put("params", c.parametersToString());
		values.put("position", position);
		values.put("iscomplete", c.isCompleted);
		
		StatementProcessor.executePreparedInsertStatement("Commands", values, con);
	}

	private static void updateGamePatches(Game game, PatchList patches, Connection con) throws SQLException {
		int i = 0;
		for(Patch p : patches.getUnclaimed()) {
			updateGamePatch(game, p, i, con);
			i++;
		}
		i = 0;
		for(Patch p : patches.getClaimed()) {
			updateGamePatch(game, p, i, con);
			i++;
		}
	}

	private static void updateGamePatch(Game game, Patch p,
			int i, Connection con) throws SQLException {
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		if(p == null)
			return;
		if(p.id == 0)
			insertGamePatch(game, p, i, con);
		else {
			values.clear();
			values.put("position", i);
			values.put("waschosen", p.wasChosen);
			StatementProcessor.executePreparedUpdateStatement("GamePatches", values, p.id, con);
		}
	}

	private static void insertGamePatch(Game game, Patch p, int position,
			Connection con) throws SQLException {
		p.id = IdManager.getNextId(con);
		if(p.id == -1) {
			con.close();
			throw new SQLException("failed to get id");
		}
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		
		values.put("id", p.id);
		values.put("gameid", game.id);
		values.put("patchid", p.staticId);
		values.put("position", position);
		values.put("waschosen", p.wasChosen);
		
		StatementProcessor.executePreparedInsertStatement("GamePatches", values, con);
	}

	private static void updateTimeTrack(Game game, TimeTrack track, Connection con) throws SQLException {
		if(track == null)
			return;
		if(track.id == 0)
			insertTimeTrack(game, track, con);
		else {
			LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
			values.put("nextbonus", track.nextUnclaimed);
			values.put("claimedbonuses", track.claimedToString());
			StatementProcessor.executePreparedUpdateStatement("TimeTracks", values, track.id, con);
		}
	}

	private static void insertTimeTrack(Game game, TimeTrack track,
			Connection con) throws SQLException {
		track.id = IdManager.getNextId(con);
		if(track.id == -1) {
			con.close();
			throw new SQLException("failed to get id");
		}
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		
		values.put("id", track.id);
		values.put("gameid", game.id);
		values.put("nextbonus", track.nextUnclaimed);
		values.put("claimedbonuses", track.claimedToString());
		
		StatementProcessor.executePreparedInsertStatement("TimeTracks", values, con);
	}

	private static void updatePlayers(Game game, Player[] players, Connection con) throws SQLException {
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		int currentplayer;
		for(Player player : players) {
			if(player == null)
				continue;
			values.clear();
			values.put("income", player.income);
			values.put("position", player.position);
			values.put("buttons", player.buttons);
			currentplayer = (player.currentPlayer) ? 1 : 0;
			values.put("currentplayer", currentplayer);
			values.put("isai", player.isAI);
			values.put("boardbonus", player.boardBonus);
			values.put("board", player.board.toDataString());
			StatementProcessor.executePreparedUpdateStatement("Players", values, player.id, con);
			updatePlayerPatches(player, player.board.patches, con);
		}
	}

	private static void updatePlayerPatches(Player player, Patch[] patches, Connection con) throws SQLException {
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		for(Patch p : patches) {
			if(p == null)
				continue;
			if(p.id == 0)
				insertPlayerPatch(player, p, con);
			else {
				values.clear();
				values.put("columnnumber", p.location.x);
				values.put("rownumber", p.location.y);
				StatementProcessor.executePreparedUpdateStatement("PlayerPatches", values, p.id, con);
			}
		}
	}

	private static void insertPlayerPatch(Player player, Patch p, Connection con) throws SQLException {
		p.id = IdManager.getNextId(con);
		if(p.id == -1) {
			con.close();
			throw new SQLException("failed to get id");
		}
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		
		values.put("id", p.id);
		values.put("playerid", player.id);
		values.put("patchid", p.staticId);
		values.put("columnnumber", p.location.x);
		values.put("rownumber", p.location.y);
		
		StatementProcessor.executePreparedInsertStatement("PlayerPatches", values, con);
		
	}

	public static boolean createGame(Game game, String username) {
		long gameid = initGame(game);
		if(gameid == -1)
			return false;
		Player player = createPlayer(game, gameid, true, username);
		if(player == null)
			return false;
		game.players[0] = player;
		return true;
	}

	public static Player createPlayer(Game game, long gameid, boolean currentPlayer, String username) {
		long playerid = -1;
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		Player p = null;
		try(Connection con = ConnectionManager.getConnection()) {
			playerid = IdManager.getNextId(con);
			if(playerid == -1) {
				con.close();
				return null;
			}
			p = new Player(playerid, username, currentPlayer);
			int currentplayer = (p.currentPlayer) ? 1 : 0;
			values.put("id", playerid);
			values.put("username", username);
			values.put("gameid", game.id);
			values.put("income", p.income);
			values.put("position", p.position);
			values.put("buttons", p.buttons);
			values.put("currentplayer", currentplayer);
			values.put("board", p.board.toDataString());
			StatementProcessor.executePreparedInsertStatement("Players", values, con);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return p;

	}

	public static long initGame(Game game) {
		long id = -1;
		LinkedHashMap<String,Object> values = new LinkedHashMap<String,Object>();
		try(Connection con = ConnectionManager.getConnection()) {
			id = IdManager.getNextId(con);
			if(id == -1) {
				con.close();
				return -1;
			}
			game.id = id;
			values.put("id",id);
			values.put("name",game.name);
			values.put("isstarted",0);
			values.put("iscomplete",0);
			StatementProcessor.executePreparedInsertStatement("Games", values, con);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return id;
	}
	
	public static Game[] getGames(String username, Long id) {
		Game[] result = null;
		try(Connection con = ConnectionManager.getConnection()) {
			if( id == null ) {
				result = getGameList(username, con);
			}
			else {
				Game g = getGame(id, con);
				if(g != null)
					result = new Game[]{g};
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	private static Game getGame(Long id, Connection con) throws SQLException {
		Game result = null;
		String query = gameQuery;
		ReadWriteValidator.initialRead(id);
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet r = stmt.executeQuery()) {
				if(r.next()) {
					result = new Game();
					result.id = id;
					result.name = r.getString("name");
					result.isstarted = r.getInt("isstarted");
					result.iscomplete = r.getInt("iscomplete");
					result.state.boardBonus = r.getInt("boardbonus");
				}
			}			
		}
		if(result != null) {
			result.players = getPlayers(id, result, con);		
			result.state = getState(id, result, con);
		}
		return result;
	}

	private static GameState getState(Long id, Game game, Connection con) throws SQLException {
		GameState result = game.state;
		result.game = game;
		if(game.players.length == 2) {
			if(game.players[0].currentPlayer) {
				game.state.currentPlayer = game.players[0];
				game.state.nextPlayer = game.players[1];
			}
			else {
				game.state.currentPlayer = game.players[1];
				game.state.nextPlayer = game.players[0];
			}
		}
		result.actionQueue = getActions(id, game, con);
		result.patches = getGamePatches(id, game, con);
		result.track = getTimeTrack(id, game, con);
		
		return result;
	}

	private static TimeTrack getTimeTrack(Long id, Game game, Connection con) throws SQLException {
		String query = timeTracksQuery;
		TimeTrack t = null;
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet r = stmt.executeQuery()) {
				if(r.next()) {
					t = new TimeTrack();
					t.id = r.getLong("id");
					t.nextUnclaimed = r.getInt("nextbonus");
					t.claimedFromString(r.getString("claimedbonuses"));
				}
			}			
		}
		if( t == null )
			t = new TimeTrack();
		return t;
	}

	private static PatchList getGamePatches(Long id, Game game,
			Connection con) throws SQLException {
		String query = gamePatchesQuery;
		LinkedList<QueueDTO> unordered = new LinkedList<QueueDTO>();
		Patch p = null;
		Integer staticId = null;
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet r = stmt.executeQuery()) {
				QueueDTO q = null;
				while (r.next()) {
					staticId = r.getInt("pid");
					p = Patches.getPatch(staticId);
					p.id = r.getLong("gid");
					q = new QueueDTO(p);
					q.position = r.getInt("position");
					unordered.add(q);
				}				
			}			
		}
		Patch[] patchArray = new Patch[unordered.size()];
		for(QueueDTO dto : unordered)
			patchArray[dto.position] = (Patch) dto.child;
		return new PatchList(patchArray);
	}

	private static LinkedList<ActionInterface> getActions(Long id, Game game, Connection con) throws SQLException {
		String query = actionsQuery;
		LinkedList<QueueDTO> unordered = new LinkedList<QueueDTO>();
		Action a = null;
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet r = stmt.executeQuery()) {
				QueueDTO q = null;
				while (r.next()) {
					a = new Action();
					q = new QueueDTO(a);
					a.id = r.getLong("id");
					q.parentId = r.getLong("playerid");
					q.position = r.getInt("position");
					unordered.add(q);
				}				
			}			
		}
		Action[] actionArray = new Action[unordered.size()];
		for(QueueDTO dto : unordered) {
			a = (Action) dto.child;
			for(Player player : game.players) {
				if(player.id == dto.parentId)
					a.player = player;
			}
			actionArray[dto.position] = a;
		}
		for(Action action : actionArray) {
			action.commands = getCommands(action.id, action, con);
		}
		LinkedList<ActionInterface> actionQueue = new LinkedList<ActionInterface>(Arrays.asList(actionArray));
		return actionQueue;
	}

	private static LinkedList<Command> getCommands(long id, Action a, Connection con) throws SQLException {
		String query = commandsQuery;
		LinkedList<QueueDTO> unordered = new LinkedList<QueueDTO>();
		Command c = null;
		Long cid = null;
		String name = null;
		String params = null;
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet r = stmt.executeQuery()) {
				QueueDTO q = null;
				while (r.next()) {
					cid = r.getLong("id");
					name = r.getString("name");
					params = r.getString("params");
					c = CommandBuilder.buildCommand(a, cid, name, params);
					q = new QueueDTO(c);
					q.position = r.getInt("position");
					unordered.add(q);
				}				
			}			
		}
		Command[] commandArray = new Command[unordered.size()];
		for(QueueDTO dto : unordered)
			commandArray[dto.position] = (Command) dto.child;
		return new LinkedList<Command>(Arrays.asList(commandArray));
	}

	private static Player[] getPlayers(Long id, Game game, Connection con) throws SQLException {
		LinkedList<Player> playerList = new LinkedList<Player>();
		String query = playersQuery;
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet r = stmt.executeQuery()) {
				Player newPlayer = null;
				while( r.next() ) {
					newPlayer = new Player();
					newPlayer.id = r.getLong("id");
					newPlayer.username = r.getString("username");
					newPlayer.income = r.getInt("income");
					newPlayer.position = r.getInt("position");
					newPlayer.buttons = r.getInt("buttons");
					newPlayer.currentPlayer = r.getBoolean("currentplayer");
					newPlayer.isAI = r.getInt("isai");
					newPlayer.boardBonus = r.getInt("boardbonus");
					newPlayer.board = new PlayerBoard(r.getString("board"));
					playerList.add(newPlayer);
				}
			}			
		}
		for(Player player : playerList){
			player.board.patches = getPlayerPatches(player.id,player.board,con);
		}
		if(playerList.size() > 0) {
			Player[] result = new Player[playerList.size()];
			return playerList.toArray(result);
		}
		return null;
	}

	private static Patch[] getPlayerPatches(long id, PlayerBoard board,
			Connection con) throws SQLException {
		String query = playerPatchesQuery;
		LinkedList<Patch> patchList = new LinkedList<Patch>();
		Patch p = null;
		Integer staticId = null;
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet r = stmt.executeQuery()) {
				while (r.next()) {
					staticId = r.getInt("qid");
					p = Patches.getPatch(staticId);
					p.id = r.getLong("pid");
					p.location = new Point(r.getInt("col"),r.getInt("row"));
					patchList.add(p);
				}				
			}			
		}
		if(patchList.size() > 0) {
			Patch[] result = new Patch[patchList.size()];
			return patchList.toArray(result);
		}
		return new Patch[0];
	}

	private static Game[] getGameList(String username, Connection con) throws SQLException {
		String query = null;
		String value = null;
		LinkedList<Game> gameList = new LinkedList<Game>();
		if(username == null)
			query = allGamesListQuery;
		else {
			query = yourGamesListQuery;
			value = username;
		}
		try(PreparedStatement stmt = con.prepareStatement(query)) {
			if(value != null) {
				stmt.setObject(1, value);
				stmt.setObject(2, value);
			}
			try (ResultSet r = stmt.executeQuery()) {
				Game newGame = null;
				LinkedList<Player> playerList = new LinkedList<Player>();
				while(r.next()){
					playerList.clear();
					newGame = new Game();
					newGame.id = r.getLong("gameid");
					newGame.name = r.getString("gamename");
					newGame.isstarted = r.getInt("started");
					String user = r.getString("user1");
					if(user != null) {
						Player p = new Player();
						p.username = user;
						playerList.add(p);
					}
					user = r.getString("user2");
					if(user != null) {
						Player p = new Player();
						p.username = user;
						playerList.add(p);
					}
					Player[] playerArray = new Player[playerList.size()];
					newGame.players = playerList.toArray(playerArray);
					gameList.add(newGame);
				}
			}			
		}
		if(gameList.size() > 0) {
			Game[] result = new Game[gameList.size()];
			return gameList.toArray(result);
		}
		return null;
	}	
}

class QueueDTO {
	public int position;
	public long parentId;
	public Object child;
	
	public QueueDTO(Object child) {
		this.child = child;
	}
}
