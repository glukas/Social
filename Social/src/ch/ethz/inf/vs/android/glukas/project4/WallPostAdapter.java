package ch.ethz.inf.vs.android.glukas.project4;

import java.util.ArrayList;
import java.util.Map;

import ch.ethz.inf.vs.android.glukas.project4.Post.PostType;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WallPostAdapter extends SortedSetAdapter<Post> {

	private Map<UserId, User> userMap;
	
	public WallPostAdapter(Context context, Map<UserId, User> userMap) {
		super(context, new ArrayList<Post>());
		this.userMap = userMap;
	}

	public View getView (int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Post currentPost = getItem(position);

		View rowView = null;
		// Inflate the views from XML
		if (currentPost.getType() == PostType.PICTURE) {
			rowView = inflater.inflate(R.layout.my_wall_list_row_picture, parent, false);
			
			// Load the image and set it on the ImageView
			ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
			imageView.setImageDrawable(new BitmapDrawable(null, currentPost.getImage()));
		} else {
			rowView = inflater.inflate(R.layout.my_wall_list_row_text, parent, false);
		}
		
		//Set the author
		TextView authorTextView = (TextView) rowView.findViewById(R.id.author);
		User author = userMap.get(currentPost.getPoster());
		if (author != null) {//we may not know all friends of friends (with the current protocol, though it is easily feasible in principle)
			authorTextView.setText(author.getUsername());
			authorTextView.setTextColor(Color.parseColor(author.getColor()));
		} else {
			authorTextView.setText("unknown");
		}
		// Set the text on the TextView
		TextView textView = (TextView) rowView.findViewById(R.id.post);
		textView.setText(currentPost.getText());
		
		return rowView;
	}
	
}
