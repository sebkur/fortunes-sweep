package fortune.sweep;

// Decompiled by Jad v1.5.7c. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packfields(5) packimports(3) nocasts braces 
// Source File Name:   Fortune.java

import java.awt.Graphics;

public class EventQueue
{

	public EventPoint events;

	public void insert(EventPoint p)
	{
		if (events != null)
			events.insert(p);

		if (p.prev == null)
			events = p;
	}

	public void remove(EventPoint eventpoint)
	{
		if (eventpoint.next != null)
			eventpoint.next.prev = eventpoint.prev;

		if (eventpoint.prev != null)
			eventpoint.prev.next = eventpoint.next;
		else
			events = eventpoint.next;
	}

	public EventPoint pop()
	{
		EventPoint eventpoint = events;
		if (eventpoint != null) {
			events = events.next;
			if (events != null) {
				events.prev = null;
			}
		}
		return eventpoint;
	}

	public void paint(Graphics g, boolean flag)
	{
		for (EventPoint eventpoint = events; eventpoint != null; eventpoint = eventpoint.next) {
			if (flag || !(eventpoint instanceof CirclePoint)) {
				eventpoint.paint(g);
			}
		}

	}

}
