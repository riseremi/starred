package me.riseremi.utils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author riseremi
 */
public class NameGeneratorTest {

    public NameGeneratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class NameGenerator.
     */
    @Test
    public void testGetName() {
        System.out.println("me.riseremi.utils.NameGenerator: getName");
        int expResult = "two words".split(" ").length;
        int result = NameGenerator.getName().split(" ").length;
        assertEquals(expResult, result);
    }

}
