package tr.org.liderahenk.conky.dialogs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.conky.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.dialogs.DefaultTaskDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;
import tr.org.liderahenk.liderconsole.core.xmpp.notifications.TaskStatusNotification;

/**
 *
 */
public class ConkyTaskCommandDialog extends DefaultTaskDialog {
	
	private static final Logger logger = LoggerFactory.getLogger(ConkyTaskCommandDialog.class);
	private Text textSettings;
	private Text textMessage;
	private Button btnCheckButtonConkyMessage;
	private Table table;
	
	private static final String DEFAULT_SETTING = ""
			+ "alignment top_left\r"
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
		
		//subscribeEventHandler(taskStatusNotificationHandler);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(650, 620);
	}

	
	@Override
	public String createTitle() {
		return "Masaüstü Mesajı (ULAK) ";
	}

	@Override
	public Control createTaskDialogArea(Composite container) {
		
		btnCheckButtonConkyMessage = new Button(container, SWT.CHECK);
		btnCheckButtonConkyMessage.setText("Masaüstü Mesajlarını Kaldır");
		
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Masaüstü Mesaj İçeriği");
		
		textMessage = new Text(tabFolder, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL );
		tbtmNewItem.setControl(textMessage);
		textMessage.setSize(new Point(24, 24));
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Masaüstü Mesaj Görünüm ayarları");
		
		textSettings = new Text(tabFolder, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL );
		tbtmNewItem_1.setControl(textSettings);
		textSettings.setText(DEFAULT_SETTING);
		
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
		if(textMessage.getText().equals("") || textSettings.getText().equals("")) throw new ValidationException("Please fill required fields.");
		
		}
	
	@Override
	public Map<String, Object> getParameterMap() {
		Map<String, Object>  map= new HashMap<>();
		
		String conkyMessage= textSettings.getText() + textMessage.getText();
		
		Boolean removeConkyMessage=false;
		if(btnCheckButtonConkyMessage.getSelection()){
			removeConkyMessage=true;
		}
		
		map.put("conkyMessage", conkyMessage );
		map.put("removeConkyMessage", removeConkyMessage );
		
		return map;
	}

	@Override
	public String getCommandId() {
		// TODO command id which is used to match tasks with ICommand class in the corresponding Lider plugin
		return "execute_conky";
	}

	@Override
	public String getPluginName() {
		return "conky";
	}

	@Override
	public String getPluginVersion() {
		return "1.0.0";
	}
	
	private EventHandler taskStatusNotificationHandler = new EventHandler() {
		@Override
		public void handleEvent(final Event event) {
			Job job = new Job("TASK") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("EDIP", 100);
					try {
						TaskStatusNotification taskStatus = (TaskStatusNotification) event
								.getProperty("org.eclipse.e4.data");
						byte[] data = taskStatus.getResult().getResponseData();
						final Map<String, Object> responseData = new ObjectMapper().readValue(data, 0, data.length,
								new TypeReference<HashMap<String, Object>>() {
								});
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								
								
								
							}
						});
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						Notifier.error("", Messages.getString("ERROR"));
					}
					monitor.worked(100);
					monitor.done();

					return Status.OK_STATUS;
				}
			};

			job.setUser(true);
			job.schedule();
		}
	};
	
}
