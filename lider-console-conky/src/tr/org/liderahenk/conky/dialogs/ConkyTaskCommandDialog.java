package tr.org.liderahenk.conky.dialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.conky.constants.ConkyConstants;
import tr.org.liderahenk.conky.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.dialogs.DefaultTaskDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;

/**
 *
 */
public class ConkyTaskCommandDialog extends DefaultTaskDialog {
	
	private static final Logger logger = LoggerFactory.getLogger(ConkyTaskCommandDialog.class);
	private Text textSettings;
	private Text textMessage;
	private Button btnCheckButtonConkyMessage;
	private Combo cmbSampleConfigs;
	
	private static final String DEFAULT_SETTING = ""
			+ "alignment top_right\r"
			+ "\nbackground yes\r"
			+ "\nborder_width 1\r"
			+ "\ncpu_avg_samples 2\r"
			+ "\ndefault_color white\r"
			+ "\ndefault_outline_color white\r"
			+ "\ndefault_shade_color white\r"
			+ "\ndraw_borders no\r"
			+ "\ndraw_graph_borders yes\r"
			+ "\ndraw_outline no\r"
			+ "\ndraw_shades no\r"
			+ "\nuse_xft yes\r"
			+ "\nxftfont DejaVu Sans Mono:size=12\r"
			+ "\ngap_x 5\r"
			+ "\ngap_y 60\r"
			+ "\nminimum_size 5 5\r"
			+ "\nnet_avg_samples 2\r"
			+ "\nno_buffers yes\r"
			+ "\nout_to_console no\r"
			+ "\nout_to_stderr no\r"
			+ "\nextra_newline no\r"
			+ "\nown_window yes\r"
			+ "\nown_window_class Conky\r"
			+ "\nown_window_type override\r"
			+ "\nstippled_borders 0\r"
			+ "\nupdate_interval 1.0\r"
			+ "\nuppercase no\r"
			+ "\nuse_spacer none\r"
			+ "\nshow_graph_scale no\r"
			+ "\nshow_graph_range no\r"
			+ "\n\r"
			+ "\nTEXT\r\n";
			//+ "\nLider Ahenk Masa\u00FCst\u00FC Arkaplan Eklentisi v1.0.0";

	
	public ConkyTaskCommandDialog(Shell parentShell, Set<String> dnSet) {
		super(parentShell, dnSet);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(650, 650);
	}

	
	@Override
	public String createTitle() {
		return Messages.getString("TITLE");
	}

	@Override
	public Control createTaskDialogArea(Composite container) {
		
		btnCheckButtonConkyMessage = new Button(container, SWT.CHECK);
		btnCheckButtonConkyMessage.setText(Messages.getString("REMOVE_MESSAGE"));
		btnCheckButtonConkyMessage.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textMessage.setEnabled(!btnCheckButtonConkyMessage.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText(Messages.getString("CONTENT"));
		
		textMessage = new Text(tabFolder, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL );
		tbtmNewItem.setControl(textMessage);
		textMessage.setSize(new Point(24, 24));
		
		TabItem tbtmSettings = new TabItem(tabFolder, SWT.NONE);
		tbtmSettings.setText(Messages.getString("VIEW_SETTINGS"));
		
		
		Composite composite = new Composite(tabFolder, SWT.NONE );
		tbtmSettings.setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(Messages.getString("CHOOSE_TEMPLATE"));
		
		cmbSampleConfigs = new Combo(composite, SWT.NONE);
		cmbSampleConfigs.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		cmbSampleConfigs.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		populateConfigs();
		
		textSettings = new Text(composite, SWT.BORDER | SWT.MULTI  |SWT.H_SCROLL | SWT.V_SCROLL);
		textSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		textSettings.setText(DEFAULT_SETTING);
		
//		textSettings = new Text(tabFolder, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL );
//		tbtmNewItem_1.setControl(textSettings);
//		textSettings.setText(DEFAULT_SETTING);
		
//		table = new Table(tabFolder, SWT.FULL_SELECTION | SWT.CHECK | SWT.BORDER | SWT.V_SCROLL  | SWT.H_SCROLL);
//		    
//		table.setHeaderVisible(true);
//		String[] titles = { "Setting", "Value" };
//		
//		for (int i = 0; i < titles.length; i++) {
//		        TableColumn column = new TableColumn(table, SWT.NULL);
//		        column.setText(titles[i]);
//		}
//		table.setHeaderVisible(true);
//		table.setLinesVisible(true);
//		
//		tbtmNewItem_1.setControl(table);
//		
		
		return container;
		
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void validateBeforeExecution() throws ValidationException {
		if(textSettings.getText().equals("")) throw new ValidationException(Messages.getString("FILL_FIELDS"));
	}
	
	@Override
	public Map<String, Object> getParameterMap() {
		Map<String, Object> map = new HashMap<>();
		
		String conkyMessage = textSettings.getText() + textMessage.getText();
		
		Boolean removeConkyMessage = false;
		if(btnCheckButtonConkyMessage.getSelection()){
			removeConkyMessage = true;
		}
		
		map.put(ConkyConstants.PARAMETERS.CONKY_MESSAGE, conkyMessage );
		map.put(ConkyConstants.PARAMETERS.REMOVE_CONKY_MESSAGE, removeConkyMessage );
		
		return map;
	}

	@Override
	public String getCommandId() {
		return "EXECUTE_CONKY";
	}

	@Override
	public String getPluginName() {
		return ConkyConstants.PLUGIN_NAME;
	}

	@Override
	public String getPluginVersion() {
		return ConkyConstants.PLUGIN_VERSION;
	}
	
	
	
	private void populateConfigs() {
		try {
			String path = SWTResourceManager.getAbsolutePath(ConkyConstants.PLUGIN_ID.CONKY, "conf/");
			if (path != null) {
				File file = new File(path);
				if (file.isDirectory()) {
					File[] configs = file.listFiles();
					if (configs != null) {
					
						for (int i = 0; i < configs.length; i++) {
							File config = configs[i];
							BufferedReader br = null;
							try {
								br = new BufferedReader(new FileReader(config));
								boolean firstLine = true;
								StringBuilder contents = new StringBuilder();
								String line = null;
								while ((line = br.readLine()) != null) {
									if (firstLine) {
										cmbSampleConfigs.add(line.replace("#", "").trim());
										firstLine = false;
									}
									contents.append(line);
									contents.append("\n");
								}
								cmbSampleConfigs.setData(i + "", contents);
								
							} catch (Exception e1) {
								logger.error(e1.getMessage(), e1);
							} finally {
								if (br != null) {
									try {
										br.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	protected String getSelectedSampleConfig() {
		int selectionIndex = cmbSampleConfigs.getSelectionIndex();
		if (selectionIndex > -1 && cmbSampleConfigs.getItem(selectionIndex) != null
				&& cmbSampleConfigs.getData(selectionIndex + "") != null) {
			return cmbSampleConfigs.getData(selectionIndex + "").toString();
		}
		return null;
	}
	
	protected void handleSelection() {
		String content = getSelectedSampleConfig();
		if (content != null && !content.isEmpty()) {
			textSettings.setText(content);
		}
	}
}
