package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.*;

import models.world.Position;
import util.HelperFunctions;

public class HelperFunctionsTests {
	@Test
	@DisplayName("distance()")
	void testDistance() {
		Position origin = new Position(0, 0);
		Position a = new Position(1, 0);
		Position b = new Position(0, 1);
		Position c = new Position(-1, 0);
		Position d = new Position(0, -1);
		Position e = new Position(1, 1);
		
		assertEquals(1, HelperFunctions.distance(origin, a));
		assertEquals(1, HelperFunctions.distance(origin, b));
		assertEquals(1, HelperFunctions.distance(origin, c));
		assertEquals(1, HelperFunctions.distance(origin, d));
		assertEquals(Math.sqrt(2), HelperFunctions.distance(origin, e));
	}
	
	@Test
	@DisplayName("lerp() - Color")
	void testLerpColor() {
		Color r = Color.red;
		Color g = Color.green;
		
		assertEquals(r, HelperFunctions.lerp(r, g, 0));
		assertEquals(g, HelperFunctions.lerp(r, g, 1));
		assertEquals(new Color(128, 128, 128), HelperFunctions.lerp(Color.black, Color.white, .5));
	}
	
	@Test
	@DisplayName("lerp() - Double")
	void testLerpDouble() {
		double a = 5;
		double b = 10;
		
		assertEquals(a, HelperFunctions.lerp(a, b, 0));
		assertEquals(b, HelperFunctions.lerp(a, b, 1));
		assertEquals(7.5, HelperFunctions.lerp(a, b, .5));
	}
	
	@Test
	@DisplayName("lerp() - Int")
	void testLerpInt() {
		int a = 5;
		int b = 10;
		
		assertEquals(a, HelperFunctions.lerp(a, b, 0));
		assertEquals(b, HelperFunctions.lerp(a, b, 1));
		assertEquals(8, HelperFunctions.lerp(a, b, .5));
	}
}
