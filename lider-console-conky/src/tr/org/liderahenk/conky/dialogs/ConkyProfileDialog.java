package tr.org.liderahenk.conky.dialogs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.conky.constants.ConkyConstants;
import tr.org.liderahenk.conky.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.dialogs.IProfileDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.liderconsole.core.model.Profile;

public class ConkyProfileDialog implements IProfileDialog {

	private static final String DEFAULT_CONF = "alignment bottom_right\nbackground yes\nown_window yes\nown_window_type normal\nown_window_class conky\nown_window_hints undecorated,skip_taskbar,skip_pager,sticky,below\nown_window_argb_visual yes\nown_window_transparent yes\n\nTEXT\nLIDER AHENK";

	private Text txtMessage;

	@Override
	public void init() {
	}

	@Override
	public void createDialogArea(Composite parent, Profile profile) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(100, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label lblMessage = new Label(composite, SWT.RIGHT);
		lblMessage.setText(Messages.getString("MESSAGE"));

		txtMessage = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 100, 40);
		data.heightHint = 100;
		data.widthHint = 160;
		txtMessage.setLayoutData(data);
		txtMessage.setText(profile != null && profile.getProfileData() != null
				&& profile.getProfileData().get(ConkyConstants.PARAMETERS.MESSAGE) != null
						? profile.getProfileData().get(ConkyConstants.PARAMETERS.MESSAGE).toString() : DEFAULT_CONF);
	}

	@Override
	public Map<String, Object> getProfileData() throws Exception {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put(ConkyConstants.PARAMETERS.MESSAGE, txtMessage.getText());
		return parameterMap;
	}

	@Override
	public void validateBeforeSave() throws ValidationException {
		if (txtMessage.getText() == null || txtMessage.getText().isEmpty()) {
			throw new ValidationException(Messages.getString("FILL_MESSAGE"));
		}
	}

}
