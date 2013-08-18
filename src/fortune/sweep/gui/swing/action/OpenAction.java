package fortune.sweep.gui.swing.action;

import java.awt.event.ActionEvent;

public class OpenAction extends BaseAction
{

	private static final long serialVersionUID = 1L;

	public OpenAction()
	{
		super("Open", "Open a point set", null);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.exit(0);
	}

}
