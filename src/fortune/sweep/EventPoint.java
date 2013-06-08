package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

class EventPoint extends MyPoint
{

	EventPoint prev, next;

	EventPoint(MyPoint mypoint)
	{
		super(mypoint);
	}

	EventPoint(double d, double d1)
	{
		super(d, d1);
	}

	public void insert(EventPoint eventpoint)
	{
		if (eventpoint.x > x || eventpoint.x == x && eventpoint.y > y) {
			if (next != null) {
				next.insert(eventpoint);
				return;
			} else {
				next = eventpoint;
				eventpoint.prev = this;
				return;
			}
		}
		if (eventpoint.x != x || eventpoint.y != y
				|| (eventpoint instanceof CirclePoint)) {
			eventpoint.prev = prev;
			eventpoint.next = this;
			if (prev != null) {
				prev.next = eventpoint;
			}
			prev = eventpoint;
			return;
		} else {
			eventpoint.prev = eventpoint;
			System.out
					.println("Double point ignored: " + eventpoint.toString());
			return;
		}
	}

	public void action(MyCanvas mycanvas)
	{
		mycanvas.arcs.insert(this, mycanvas.xPos, mycanvas.events);
	}

}
