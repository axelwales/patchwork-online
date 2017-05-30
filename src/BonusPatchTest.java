import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.patchwork.game.logic.Action;
import com.patchwork.game.logic.Choose;
import com.patchwork.game.logic.Command;
import com.patchwork.game.models.Game;
import com.patchwork.game.models.Player;


public class BonusPatchTest {
	Game game;
	Player p1;
	Player p2;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.game = new Game();
		this.p1 = new Player();
		this.p1.username = "p1";
		this.p1.id = 1;
		this.p2 = new Player();
		this.p2.username = "p2";
		this.p2.id = 2;
		this.game.players[0] = p1;
		this.game.state.currentPlayer = p1;
		this.game.players[1] = p2;
		this.game.state.nextPlayer = p2;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		this.p1.position = 48;
		this.p2.position = 49;
		Action a = new Action(p1);
		Command testCommand = new Choose(a);
		testCommand.parameters.put("choice",3);
		testCommand.execute(game.state);
		int bonusPatch = game.state.track.bonusClaimed[4];
		assertEquals(1,bonusPatch);
	}

}
