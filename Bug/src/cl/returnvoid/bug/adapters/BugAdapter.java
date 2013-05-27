package cl.returnvoid.bug.adapters;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BugAdapter extends ArrayAdapter<UserItemElement> {
	private List<UserItemElement> userList;
	private LayoutInflater inflater;

	public BugAdapter(Context context, int resourceId,
			List<UserItemElement> userList) {
		super(context, resourceId, userList);
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.userList = userList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		UserItemElement userItem = userList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(
					cl.returnvoid.bug.R.layout.user_item_element, null);
			holder = new ViewHolder();
			holder.nameText = (TextView) convertView
					.findViewById(cl.returnvoid.bug.R.id.name);
			holder.locationText = (TextView) convertView
					.findViewById(cl.returnvoid.bug.R.id.location);
			holder.numberOfFriendsText = (TextView) convertView
					.findViewById(cl.returnvoid.bug.R.id.numberOfFriends);
			holder.picture = (ImageView) convertView
					.findViewById(cl.returnvoid.bug.R.id.pictureProfile);
			holder.layoutStatus = (LinearLayout) convertView
					.findViewById(cl.returnvoid.bug.R.id.layoutStatus);
			for (int i = 0; i < userItem.getStatuses().length(); i++) {
				View statusLayoutRow = inflater.inflate(
						cl.returnvoid.bug.R.layout.user_item_element_status, null);
				holder.statuses.add(statusLayoutRow);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		int i = 0;
		holder.layoutStatus.removeAllViews();
		Iterator<View> views = holder.statuses.iterator();
		while (views.hasNext()) {
			View statusLayoutRow = views.next();
			TextView statusTextById = (TextView) statusLayoutRow
					.findViewById(cl.returnvoid.bug.R.id.statusText);
			try {
				statusTextById.setText(userItem.getStatuses().getJSONObject(i)
						.getString("message"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			holder.layoutStatus.addView(statusLayoutRow);
			i++;
		}
		new PictureProfile(holder.picture).execute(userItem.getId());
		holder.nameText.setText(userItem.getName());
		holder.locationText.setText(userItem.getLocation());
		holder.numberOfFriendsText.setText("234");

		return convertView;
	}

	static class ViewHolder {
		ImageView picture;
		TextView nameText;
		TextView locationText;
		TextView numberOfFriendsText;
		LinearLayout layoutStatus;
		ArrayList<View> statuses = new ArrayList<View>();
	}

	private class PictureProfile extends AsyncTask<String, Void, Bitmap> {
		ImageView pictureView;

		public PictureProfile(ImageView pictureView) {
			this.pictureView = pictureView;
		}

		protected Bitmap doInBackground(String... urls) {
			String index = urls[0];
			String imageUrl = "http://graph.facebook.com/" + index
					+ "/picture?width=100&height=100";
			Bitmap image = null;
			try {
				InputStream in = new java.net.URL(imageUrl).openStream();
				image = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return image;
		}

		protected void onPostExecute(Bitmap result) {
			pictureView.setImageBitmap(result);
		}
	}
}
