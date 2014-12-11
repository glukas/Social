package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post.PostType;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends SortedSetAdapter<BasicUser> {

	public FriendListAdapter(Context context, List<BasicUser> objects) {
		super(context, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		BasicUser currentUser = getItem(position);

		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.friend_list_row, parent, false);
		}
		
		// Set the text on the TextView
		TextView textView = (TextView) rowView.findViewById(R.id.userName);
		textView.setText(currentUser.getUsername());

		return rowView;
	}

}
