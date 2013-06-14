package tests.universe;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import universe.CellWorld;

public class CellWorldTest {

	CellWorld cw1 = new CellWorld(1);
    CellWorld cw2 = new CellWorld(2);
    CellWorld cw3 = new CellWorld(3);
    CellWorld cw4 = new CellWorld(4);
    
	@Before
	public void setUp() throws Exception {
	    cw1.setAlive(true);
        cw2.setAlive(true);
        cw3.setAlive(true);
        cw4.setAlive(true);
	}

	@Test public void 
	test_number_of_neighbors_for_cw1() {
		cw1.setNv(cw2);
        cw1.setV(cw4);
		assertEquals(2, cw1.getNumberOfNeighbors());
		assertFalse(2 != cw1.getNumberOfNeighbors());
	}

	@Test public void 
	test_number_of_neighbors_for_cw2() {
		cw2.setSe(cw1);
        cw2.setSv(cw4);
        cw2.setNv(cw3);
		assertEquals(3, cw2.getNumberOfNeighbors());
	}
	
	@Test public void 
	test_number_of_neighbors_for_cw4() {
        cw4.setE(cw1);
        cw4.setNe(cw2);
		assertEquals(2, cw4.getNumberOfNeighbors());
	}

	@Test public void 
	test_number_of_neighbors_for_cw3() {
		cw3.setSe(cw2);
		assertEquals(1, cw3.getNumberOfNeighbors());
	}

	@Test public void 
	test_if_neighbor_cw1_for_cw2_is_alive() {
		cw1.setNv(cw2);
		assertTrue(cw1.getNv().isAlive());
	}
	
}
