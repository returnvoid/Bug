package cl.returnvoid.bug;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import cl.returnvoid.bug.adapters.BugAdapter;
import cl.returnvoid.bug.adapters.UserItemElement;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

public class ListActivity extends android.app.ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);	

		Request.executeMeRequestAsync(Session.getActiveSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub
						Log.d("user", user.getName());
					}
				});
		/*Request.executeMyFriendsRequestAsync(Session.getActiveSession(),
				new Request.GraphUserListCallback() {
					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {
						ArrayList<UserItemElement> userElements = new ArrayList<UserItemElement>();
						for (GraphUser user : users) {
							// usersAsString.add(user.getInnerJSONObject().toString());
							userElements.add(new UserItemElement(user.getId(),
									user.getName(), user.getFirstName()));
						}
						setListAdapter(new BugAdapter(getApplicationContext(),
								cl.returnvoid.bug.R.layout.user_item_element,
								userElements));
						// Log.v("friends", usersAsString.toString());
					}
				});*/
		Bundle params = new Bundle();
		params.putString("fields",
				"id,name,statuses.limit(3).fields(message,id,likes.limit(1)),location");
		params.putString("limit", "10");
		Request request = new Request(Session.getActiveSession(), "me/friends",
				params, HttpMethod.GET, new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						if (response.getError() == null) {
							ArrayList<UserItemElement> userElements = new ArrayList<UserItemElement>();
							GraphObject users = (GraphObject) response
									.getGraphObject();
							JSONObject jsonObject = users.getInnerJSONObject();
							JSONArray array;
							try {
								array = jsonObject.getJSONArray("data");
								for (int i = 0; i < array.length(); i++) {
									JSONObject friend = array.getJSONObject(i);
									userElements.add(new UserItemElement(friend));
									Log.d("friend name", friend.getString("name"));
								}
								setListAdapter(new BugAdapter(getApplicationContext(),
										cl.returnvoid.bug.R.layout.user_item_element,
										userElements));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				});
		Request.executeBatchAsync(request);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_SHORT)
				.show();
	}
}
