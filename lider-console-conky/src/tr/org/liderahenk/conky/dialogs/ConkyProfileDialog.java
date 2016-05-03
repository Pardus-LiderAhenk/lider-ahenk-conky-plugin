package tr.org.liderahenk.conky.dialogs;

import java.util.HashMap;
import java.util.Map;

import org.apache.directory.studio.ldapbrowser.ui.actions.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.conky.constants.ConkyConstants;
import tr.org.liderahenk.liderconsole.core.dialogs.IProfileDialog;
import tr.org.liderahenk.liderconsole.core.model.Profile;

public class ConkyProfileDialog implements IProfileDialog {
	
	private static final Logger logger = LoggerFactory.getLogger(ConkyProfileDialog.class);
	
	private Text txtMessage;
	private Label lblMessage;
	
	@Override
	public void init() {
		// TODO initialize 
	}
	
	@Override
	public void createDialogArea(Composite parent, Profile profile) {
		
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(100, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		lblMessage = new Label(composite, SWT.RIGHT);
		lblMessage.setText("Message");
		
		txtMessage = new Text(composite, SWT.WRAP| SWT.MULTI| SWT.BORDER);
		txtMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 100, 40));
		txtMessage.setSize(SWT.FILL,SWT.FILL);
		txtMessage.setText(Messages.getString("MESSAGE"));
	    		
	}
	
	@Override
	public Map<String, Object> getProfileData() throws Exception {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(ConkyConstants.PARAMETERS.MESSAGE, txtMessage.getText());
		return parameterMap;
	}
	
}
