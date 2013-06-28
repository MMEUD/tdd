package com.iolma.junit;
import junit.framework.TestCase;

import org.junit.Test;

import com.iolma.studio.application.DefaultGraph;


public class DefaultGraphTests extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void test_default_graph_exists() {
	    //given

	    //when

	    //then
		assertNotNull("DefaultGraph exists.", new DefaultGraph(null, null, null));
	}
}
