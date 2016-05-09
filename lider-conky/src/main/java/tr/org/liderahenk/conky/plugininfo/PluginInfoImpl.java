package tr.org.liderahenk.conky.plugininfo;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.lider.core.api.plugin.IPluginInfo;
import tr.org.liderahenk.lider.core.api.pluginmanager.IPluginDistro;
import tr.org.liderahenk.lider.core.api.pluginmanager.PluginDistroHTTP;

public class PluginInfoImpl implements IPluginInfo {

	private static Logger logger = LoggerFactory.getLogger(PluginInfoImpl.class);

	private String pluginName;

	private String pluginVersion;

	private String description;

	private Boolean machineOriented;

	private Boolean userOriented;

	private Boolean policyPlugin;

	private Boolean xbased;

	// Distro configuration

	private String distroUrl;

	public void refresh() {
		logger.info("Configuration updated using blueprint: {}", prettyPrintConfig());
	}

	@Override
	public String toString() {
		return "PluginInfoImpl [pluginName=" + pluginName + ", pluginVersion=" + pluginVersion + ", description="
				+ description + ", machineOriented=" + machineOriented + ", userOriented=" + userOriented
				+ ", policyPlugin=" + policyPlugin + ", xbased=" + xbased + ", distroUrl=" + distroUrl + "]";
	}

	public String prettyPrintConfig() {
		try {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (Exception e) {
		}
		return toString();
	}

	@Override
	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	@Override
	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Boolean getMachineOriented() {
		return machineOriented;
	}

	public void setMachineOriented(Boolean machineOriented) {
		this.machineOriented = machineOriented;
	}

	@Override
	public Boolean getUserOriented() {
		return userOriented;
	}

	public void setUserOriented(Boolean userOriented) {
		this.userOriented = userOriented;
	}

	@Override
	public Boolean getPolicyPlugin() {
		return policyPlugin;
	}

	public void setPolicyPlugin(Boolean policyPlugin) {
		this.policyPlugin = policyPlugin;
	}

	@Override
	public Boolean getXbased() {
		return xbased;
	}

	public void setXbased(Boolean xbased) {
		this.xbased = xbased;
	}

	public String getDistroUrl() {
		return distroUrl;
	}

	public void setDistroUrl(String distroUrl) {
		this.distroUrl = distroUrl;
	}

	@JsonIgnore
	@Override
	public IPluginDistro getDistro() {
		return new PluginDistroHTTP() {
			private static final long serialVersionUID = 8638535178751232641L;

			@Override
			public String getUrl() {
				return getDistroUrl();
			}
		};
	}

}