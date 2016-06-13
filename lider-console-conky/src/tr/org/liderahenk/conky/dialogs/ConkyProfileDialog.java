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

	private static final String DEFAULT_CONF = "alignment top_left\r\nbackground no\r\nborder_width 1\r\ncpu_avg_samples 2\r\ndefault_color white\r\ndefault_outline_color white\r\ndefault_shade_color white\r\ndraw_borders no\r\ndraw_graph_borders yes\r\ndraw_outline no\r\ndraw_shades no\r\nuse_xft yes\r\nxftfont DejaVu Sans Mono:size=12\r\ngap_x 5\r\ngap_y 60\r\nminimum_size 5 5\r\nnet_avg_samples 2\r\nno_buffers yes\r\nout_to_console no\r\nout_to_stderr no\r\nextra_newline no\r\nown_window yes\r\nown_window_class Conky\r\nown_window_type desktop\r\nstippled_borders 0\r\nupdate_interval 1.0\r\nuppercase no\r\nuse_spacer none\r\nshow_graph_scale no\r\nshow_graph_range no\r\n\r\nTEXT\r\nLider Ahenk Masa\u00FCst\u00FC Arkaplan Eklentisi v1.0.0";

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
		txtMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 100, 40));
		txtMessage.setSize(SWT.FILL, SWT.FILL);
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
