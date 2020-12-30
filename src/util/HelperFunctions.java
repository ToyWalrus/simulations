package util;

import models.world.Position;

public final class HelperFunctions {
	public static double distance(Position a, Position b) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public static double clamp(double value, double min, double max) {
		return Math.min(max, Math.max(value, min));
	}
}
