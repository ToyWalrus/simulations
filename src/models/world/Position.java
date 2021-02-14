package models.world;

import util.HelperFunctions;

public class Position {
	public final double x;
	public final double y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Position(PositionInt p) {
		this.x = (double) p.x;
		this.y = (double) p.y;
	}
	
	public double distanceTo(Position other) {
		return HelperFunctions.distance(this, other);
	}
	
	public Position offset(double dx, double dy) {
		double max = Double.MAX_VALUE;
		double min = Double.MIN_VALUE;
		return offsetClamp(dx, dy, max, min, max, min);
	}
	
	public Position offsetClamp0(double dx, double dy, double maxX, double maxY) {
		return offsetClamp(dx, dy, maxX, 0, maxY, 0);
	}
	
	public Position offsetClamp(double dx, double dy, double maxX, double minX, double maxY, double minY) {
		double x = HelperFunctions.clamp(this.x + dx, minX, maxX);
		double y = HelperFunctions.clamp(this.y + dy, minY, maxY);
		return new Position(x, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return HelperFunctions.almost(x, other.x) && HelperFunctions.almost(y, other.y);
	}
	
	@Override
	public String toString() {
		return String.format("(%.2f, %.2f)", x, y);
	}

}
