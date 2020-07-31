package Assignment.briq;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

	private String latitude;
	private String human_address;
	private Boolean needs_Recoding;
	private String longitude;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getHuman_address() {
		return human_address;
	}

	public void setHuman_address(String human_address) {
		this.human_address = human_address;
	}

	public Boolean getNeeds_Recoding() {
		return needs_Recoding;
	}

	public void setNeeds_Recoding(Boolean needs_Recoding) {
		this.needs_Recoding = needs_Recoding;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
