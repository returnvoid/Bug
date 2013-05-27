package cl.returnvoid.bug.adapters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class UserItemElement {
	private String id;
	private String name;
	private String status;
	private String location;
	private String totalLikes;
	private JSONArray statuses;

	public UserItemElement(JSONObject friend) throws JSONException {
		super();
		this.id = friend.getString("id");
		this.name = friend.getString("name");
		if(friend.has("location")){
			this.location = friend.getJSONObject("location").getString("name");
		}else{
			this.location = "No Location";
		}
		
		try {
			this.statuses = friend.getJSONObject("statuses").getJSONArray("data");
			this.status = this.statuses.getJSONObject(0).getString("message");
			try {
				this.totalLikes = String.valueOf(this.statuses.getJSONObject(0)
						.getJSONObject("likes").getJSONArray("data").length());
			} catch (JSONException e) {
				// TODO: handle exception
				this.totalLikes = "0";
			}
		} catch (JSONException e) {
			// TODO: handle exception
			this.status = "Error";
		}

	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public JSONArray getStatuses() {
		return statuses;
	}

	public void setStatuses(JSONArray statuses) {
		this.statuses = statuses;
	}

	public String getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(String totalLikes) {
		this.totalLikes = totalLikes;
	}
}
