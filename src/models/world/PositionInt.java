package models.world;

/**
 * Generally the PositionInt will refer to screen space
 * rather than world space, since the drawing commands
 * all take ints as positions instead of doubles.
 */
public class PositionInt {
	public final int x;
	public final int y;

	public PositionInt(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public PositionInt(Position p) {
		this.x = (int)p.x;
		this.y = (int)p.y;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		PositionInt other = (PositionInt) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
