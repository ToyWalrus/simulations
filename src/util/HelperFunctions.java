package util;

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
