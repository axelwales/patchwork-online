import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.patchwork.game.models.PlayerBoard;


public class boardbonustest {
	PlayerBoard pb;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		pb = new PlayerBoard();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		int[][] state = new int[][] {
				{ 1, 1, 1, 0, 0, 1, 0, 0, 0},
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{ 0, 1, 1, 1, 1, 1, 1, 1, 1},
				{ 0, 1, 1, 1, 1, 1, 1, 1, 0},
				{ 0, 1, 1, 1, 1, 1, 1, 1, 1},
				{ 0, 1, 1, 0, 1, 1, 1, 1, 1}
		};
		pb.state = state;
		assertTrue(pb.checkBoardBonus());
	}

}
