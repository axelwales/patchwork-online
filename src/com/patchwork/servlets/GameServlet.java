package com.patchwork.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.patchwork.db.ConnectionManager;
import com.patchwork.db.GameLoader;
import com.patchwork.db.InitializeTables;
import com.patchwork.game.logic.ActionInvoker;
import com.patchwork.game.logic.Command;
import com.patchwork.game.models.Game;
import com.patchwork.game.models.GameConstants;
import com.patchwork.game.models.Player;
import com.patchwork.json.ActionJSON;
import com.patchwork.mcts.PatchworkMCTS;

/**
 * Servlet implementation class GameServlet
 */
@WebServlet({ "/Play/*", "/Lobby", "/Create","/Start","/Join" })
public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GameServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init()
	 */
	public void init() throws ServletException {
		ConnectionManager.start();
		InitializeTables.init(this.getServletContext());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch(request.getServletPath().toString()) {
			case "/Play":
				forwardToGame(request, response);
				break;
			case "/Create":
			case "/Start":
			case "/Join":
			case "/Lobby":
				forwardToLobby(request, response);
				break;
			default:
				break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idString;
		switch(request.getServletPath().toString()) {
			case "/Play":
				processPlayRequest(request, response);
				break;
			case "/Create":
				Game game = new Game();
				game.name = (String) request.getParameter("gamename");
				GameLoader.createGame(game, request.getUserPrincipal().getName());
				forwardToLobby(request, response);
				break;
			case "/Start":
				idString = (String) request.getParameter("gameid");
				try {
					Long id = Long.parseLong(idString);
					GameLoader.startGame(id, request.getUserPrincipal().getName());
				} catch (NumberFormatException e) {e.printStackTrace();}
				forwardToLobby(request, response);
				break;
			case "/Join":
				idString = (String) request.getParameter("gameid");
				try {
					Long id = Long.parseLong(idString);
					GameLoader.joinGame(id, request.getUserPrincipal().getName());
				} catch (NumberFormatException e) {e.printStackTrace();}
				forwardToLobby(request, response);
				break;
			case "/Lobby":
				forwardToLobby(request, response);
				break;
			default:
				break;
		}
	}
	
	private Game[] getGames(HttpServletRequest request) {
		Game[] games = null;
		String pathInfo = request.getPathInfo();
		String idString = pathInfo.substring(1);
		try {
			Long id = Long.parseLong(idString);
			games = GameLoader.getGames(request.getUserPrincipal().getName(), id);
		} catch (NumberFormatException e) {e.printStackTrace();}
		return games;
	}
	
	private void processPlayRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher d = null;
		
		Game[] games = getGames(request);
		
		if(games != null) {
			Game currentGame = games[0];
			String commandName = (String) request.getParameter("action");
			String commandParams = (String) request.getParameter("params");
			
			if(commandName != null) {
				processAction(request, response, currentGame, commandName, commandParams);
			} else {
				forwardToGame( request, response, currentGame );
			}
		}
		else
			forwardToLobby(request, response);	
		
	}

	private void processAction(HttpServletRequest request,
			HttpServletResponse response, Game currentGame, String commandName,
			String commandParams) throws IOException, ServletException {
        
		if("ai".equalsIgnoreCase(commandName)) {
			for(Player p : currentGame.players) {
				if(p.username.equalsIgnoreCase(request.getUserPrincipal().getName()))
					if(p.isAI == 0) {
						p.isAI = 1;
						GameLoader.updateGame(currentGame);
					}
			}
			Command c = PatchworkMCTS.getCommand(currentGame);
				
			String jsonResult = ActionJSON.aiSuccess(c);
			
			PrintWriter out = response.getWriter();
			out.print(jsonResult);
			
		}  else if("updatepoll".equalsIgnoreCase(commandName)) {
			String username = (String) request.getParameter("username");
			String jsonResult = "";
			if(currentGame.iscomplete == 1 || currentGame.state.currentPlayer.username.equalsIgnoreCase(username)) {
				request.setAttribute("game", currentGame);
				request.setAttribute("patchid", currentGame.getPatchId());
				request.setAttribute("constants", GameConstants.gameConstants);
				
				HttpServletResponseWrapper wr = getResponseWrapper(response);
				request.getRequestDispatcher("/WEB-INF/views/play/GameContainer.jsp").include(request, wr);
				String container = wr.toString();
				
				jsonResult = ActionJSON.updateSuccess(container);
			} else
				jsonResult = ActionJSON.actionFailure();
			
			PrintWriter out = response.getWriter();
			out.print(jsonResult);
		}
		else {
			Boolean update = false;
			if(currentGame.state.currentPlayer.username.equalsIgnoreCase(request.getUserPrincipal().getName()))
				update = ActionInvoker.sendCommand(currentGame.state,  commandName, commandParams);
			String jsonResult = "";
			if(update) {
				GameLoader.updateGame(currentGame);
				HttpServletResponseWrapper wr;
				if("choose".equalsIgnoreCase(commandName)) {
					request.setAttribute("game", currentGame);
					request.setAttribute("patchid", currentGame.getPatchId());
					
					wr = getResponseWrapper(response);
					request.getRequestDispatcher("/WEB-INF/views/play/ClientVariables.jsp").include(request, wr);
					String clientVar = wr.toString();
					
					wr = getResponseWrapper(response);
					request.getRequestDispatcher("/WEB-INF/views/play/PatchList.jsp").include(request, wr);
					String patchList = wr.toString();
					
					for(Player p : currentGame.players) {
						if(p.username.equalsIgnoreCase(request.getUserPrincipal().getName()))
							request.setAttribute("player", p);
					}
					wr = getResponseWrapper(response);
					request.getRequestDispatcher("/WEB-INF/views/play/PlayerStats.jsp").include(request, wr);
					String playerStats = wr.toString();
					
					jsonResult = ActionJSON.chooseSuccess(clientVar, patchList, playerStats);
					
				} else if("place".equalsIgnoreCase(commandName)) {
					request.setAttribute("game", currentGame);
					request.setAttribute("patchid", currentGame.getPatchId());
					
					wr = getResponseWrapper(response);
					request.getRequestDispatcher("/WEB-INF/views/play/ClientVariables.jsp").include(request, wr);
					String clientVar = wr.toString();
					
					for(Player p : currentGame.players) {
						if(p.username.equalsIgnoreCase(request.getUserPrincipal().getName()))
							request.setAttribute("player", p);
					}
					wr = getResponseWrapper(response);
					request.getRequestDispatcher("/WEB-INF/views/play/PlayerBoard.jsp").include(request, wr);
					String playerBoard = wr.toString();
					
					jsonResult = ActionJSON.placeSuccess(clientVar, playerBoard); 
				}
				
			} else
				jsonResult = ActionJSON.actionFailure(); 
			PrintWriter out = response.getWriter();
			out.print(jsonResult);
		}
	}
	
	private HttpServletResponseWrapper getResponseWrapper(HttpServletResponse response) {
		HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
            private final StringWriter sw = new StringWriter();

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(sw);
            }

            @Override
            public String toString() {
                return sw.toString();
            }
        };
        return responseWrapper;
	}

	private void forwardToGame(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher d = null;
		Game[] games = getGames(request);
		if(games != null) {
			forwardToGame( request, response, games[0] );
		}
		else
			forwardToLobby(request, response);
	}

	private void forwardToGame(HttpServletRequest request, HttpServletResponse response, Game game) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher d = null;
		request.setAttribute("patchid", game.getPatchId());
		request.setAttribute("game", game);
		request.setAttribute("constants", GameConstants.gameConstants);
		d = request.getRequestDispatcher("/WEB-INF/views/play/Play.jsp");
		d.forward(request, response);
	}

	private void forwardToLobby(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher d = null;
		Game[] yourGames = GameLoader.getGames(request.getUserPrincipal().getName(), null);
		request.setAttribute("yourGames", yourGames);
		Game[] allGames = GameLoader.getGames(null, null);
		request.setAttribute("availableGames", allGames);
		d = request.getRequestDispatcher("/WEB-INF/views/lobby/Lobby.jsp");
		d.forward(request, response);		
	}

	@Override
	public void destroy() {
		ConnectionManager.close();
		super.destroy();
	}

}
