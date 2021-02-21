package util;

import java.awt.Color;
import java.util.Random;

import models.world.Position;

public final class HelperFunctions {
	public static double distance(Position a, Position b) {
		double epsilon = 0.001;
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		if (almost(dx, dy, epsilon)) {
			return 0;
		}
		return Math.sqrt(dx * dx + dy * dy);
	}

	public static double randomRange(Random rand, double low, double high) {
		double range = high - low;
		double val = rand.nextDouble() * range;
		return val + low;
	}

	public static int lerp(int a, int b, double f) {
		return (int) Math.round(a + f * (b - a));
	}

	public static double lerp(double a, double b, double f) {
		return a + f * (b - a);
	}
	
	public static Position lerp(Position a, Position b, double f) {
		double x = lerp(a.x, b.x, f);
		double y = lerp(a.y, b.y, f);
		return new Position(x, y);
	}

	public static Color lerp(Color c1, Color c2, double f) {
		int r = clamp(lerp(c1.getRed(), c2.getRed(), f), 0, 255);
		int g = clamp(lerp(c1.getGreen(), c2.getGreen(), f), 0, 255);
		int b = clamp(lerp(c1.getBlue(), c2.getBlue(), f), 0, 255);
		return new Color(r, g, b);
	}

	public static int clamp(int value, int min, int max) {
		return Math.min(max, Math.max(value, min));
	}
	
	public static double clamp(double value, double min, double max) {
		return Math.min(max, Math.max(value, min));
	}

	public static boolean almost(double a, double b) {
		return almost(a, b, .0001);
	}

	public static boolean almost(double a, double b, double epsilon) {
		return Math.abs(a - b) < epsilon;
	}
}
