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
	
	
	private String conkyDef=
			"conky.config = {\n" + 
			"	use_xft= true,\n" + 
			"	xftalpha= .1,\n" + 
			"	update_interval= 1,\n" + 
			"	total_run_times= 0,\n" + 
			"	background= true,\n" + 
			"	own_window= true,\n" + 
			"	own_window_type= 'desktop',\n" + 
			"	own_window_transparent= true,\n" + 
			"	own_window_hints= 'undecorated,below,sticky,skip_taskbar,skip_pager',\n" + 
			"	own_window_colour= '000000',\n" + 
			"	own_window_argb_visual= true,\n" + 
			"	own_window_argb_value= 0,\n" + 
			"	double_buffer= true,\n" + 
			"	minimum_width= 270,\n" + 
			"	maximum_width= 270,\n" + 
			"	minimum_height= 10,\n" + 
			"	draw_shades= false,\n" + 
			"	draw_outline= false,\n" + 
			"	draw_borders= false,\n" + 
			"	draw_graph_borders= false,\n" + 
			"	default_color= 'FFFFFF',\n" + 
			"	default_shade_color= '333333',\n" + 
			"	default_outline_color= 'black',\n" + 
			"	color1 = 'A9A9A9',\n" + 
			"	color3 = '616161',\n" + 
			"	alignment= 'top_right',\n" + 
			"	gap_x= 56,\n" + 
			"	gap_y= 0,\n" + 
			"	no_buffers= true,\n" + 
			"	text_buffer_size = 2048,\n" + 
			"	uppercase= false,\n" + 
			"	cpu_avg_samples= 4,\n" + 
			"	net_avg_samples = 2,\n" + 
			"	override_utf8_locale= true,\n" + 
			"	font= 'Ubuntu:style=medium:size=12'\n" + 
			"}";
			
	
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
		textSettings.setText(conkyDef);
		
//		textSettings.setText(DEFAULT_SETTING);
		
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
		
		String hourText="${color1}\n" + 
				"${voffset 20}\n" + 
				"${alignr}${font Ubuntu:style=Medium:pixelsize=50}${time %H:%M}${font}\n" + 
				"${voffset 10}\n" + 
				"${alignr}${font Ubuntu:style=Medium:pixelsize=13}${time %A %d %B %Y}${font}\n"
				+ "${hr}\n"+
				"${alignr}${font Ubuntu:style=Medium:pixelsize=30} ";
		
		
		String conkyMessage = textSettings.getText() + " conky.text = [[ "+ hourText +textMessage.getText() +" ]]";
		
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
