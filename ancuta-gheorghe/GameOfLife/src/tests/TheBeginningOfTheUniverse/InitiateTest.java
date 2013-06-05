/**
 * 
 */
package tests.TheBeginningOfTheUniverse;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import universe.BigBang;

/**
 * @author ancuta
 *
 */
public class InitiateTest {

	@Test
	public void test() {
		String output = "true true true true \n" + 
				"---\n" +
				"false true false true \n" + 
				"---\n" +
				"false false false false \n" + 
				"---\n" +
				"false false false false \n" + 
				"---\n";
				;
		BigBang bigBang = new BigBang();
        assertEquals(output, bigBang.ignite());
	}

}
