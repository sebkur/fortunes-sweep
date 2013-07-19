package fortune.sweep.arc;

import fortune.sweep.geometry.Point;

public class ParabolaPoint extends Point
{

	private double a, b, c;

	public ParabolaPoint(Point point)
	{
		super(point);
	}

	public double realX()
	{
		return getY();
	}

	public double realY(double d)
	{
		return d - getX();
	}

	public double getA()
	{
		return a;
	}

	public double getB()
	{
		return b;
	}

	public double getC()
	{
		return c;
	}

	public CirclePoint calculateCenter(Point point, ArcNode arcnode,
			Point point1)
	{
		CirclePoint circlepoint = null;
		Point point2 = new Point(arcnode.getX() - point.getX(), arcnode.getY()
				- point.getY());
		Point point3 = new Point(point1.getX() - arcnode.getX(), point1.getY()
				- arcnode.getY());
		if (point3.getY() * point2.getX() > point3.getX() * point2.getY()) {
			double d = -point2.getX() / point2.getY();
			double d1 = (point.getY() + point2.getY() / 2D) - d
					* (point.getX() + point2.getX() / 2D);
			double d2 = -point3.getX() / point3.getY();
			double d3 = (arcnode.getY() + point3.getY() / 2D) - d2
					* (arcnode.getX() + point3.getX() / 2D);
			double d4;
			double d5;
			if (point2.getY() == 0.0D) {
				d4 = point.getX() + point2.getX() / 2D;
				d5 = d2 * d4 + d3;
			} else if (point3.getY() == 0.0D) {
				d4 = arcnode.getX() + point3.getX() / 2D;
				d5 = d * d4 + d1;
			} else {
				d4 = (d3 - d1) / (d - d2);
				d5 = d * d4 + d1;
			}
			circlepoint = new CirclePoint(d4, d5, arcnode);
		}
		return circlepoint;
	}

	public void init(double d)
	{
		double d1 = realX();
		double d2 = realY(d);
		a = 1.0D / (2D * d2);
		b = -d1 / d2;
		c = (d1 * d1) / (2D * d2) + d2 / 2D;
	}

	public double f(double d)
	{
		return (a * d + b) * d + c;
	}

	public static double[] solveQuadratic(double d, double d1, double d2)
			throws Throwable
	{
		double ad[] = new double[2];
		double d3 = d1 * d1 - 4D * d * d2;
		if (d3 < 0.0D) {
			throw new Throwable();
		}
		if (d == 0.0D) {
			if (d1 != 0.0D) {
				ad[0] = -d2 / d1;
			} else {
				throw new Throwable();
			}
		} else {
			double d4 = Math.sqrt(d3);
			double d5 = -d1;
			double d6 = 2D * d;
			ad[0] = (d5 + d4) / d6;
			ad[1] = (d5 - d4) / d6;
		}
		return ad;
	}

}
