package tr.org.liderahenk.conky.handlers;

import java.util.Set;

import org.eclipse.swt.widgets.Display;

import tr.org.liderahenk.conky.dialogs.SendMessageTaskDialog;
import tr.org.liderahenk.liderconsole.core.handlers.MultipleSelectionHandler;

public class SendMessageTaskHandler extends MultipleSelectionHandler {
	
	@Override
	public void executeWithDNSet(Set<String> dnSet) {
		SendMessageTaskDialog dialog = new SendMessageTaskDialog(Display.getDefault().getActiveShell(), dnSet);
		dialog.create();
		dialog.open();
	}
}
