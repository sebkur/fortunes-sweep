package fortune.sweep.geometry;

public class Point
{

	private double x, y;

	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Point(Point point)
	{
		x = point.x;
		y = point.y;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public double distance(Point point)
	{
		double d = point.x - x;
		double d1 = point.y - y;
		return Math.sqrt(d * d + d1 * d1);
	}

}
