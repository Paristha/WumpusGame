package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ArrowTest.class, DriverTestGameMethodOrder.class, DriverTestKeyInputs.class, GameTestArrows.class,
		GameTestMap.class, GameTestMoving.class, MapTest.class, PlayerTest.class })
public class AllTests {
	// runs all tests
}
