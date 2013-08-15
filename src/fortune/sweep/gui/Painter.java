package fortune.sweep.gui;

import fortune.sweep.geometry.Edge;
import fortune.sweep.geometry.Point;

public interface Painter
{
	public void setColor(Color color);

	public void fillRect(int x, int y, int width, int height);
	
	public void fillRect(double x, double y, double width, double height);

	public void drawLine(int x1, int y1, int x2, int y2);
	
	public void drawLine(double x1, double y1, double x2, double y2);
	
	public void drawCircle(double x, double y, double radius);

	public void fillCircle(double x, double y, double radius);

	public void paint(Point p);

	public void paint(Edge e);

}
