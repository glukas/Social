package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post.PostType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WallPostAdapter extends SortedSetAdapter<Post> {

	public WallPostAdapter(Context context, List<Post> objects) {
		super(context, objects);
	}

	@SuppressLint("InflateParams") 
	public View getView (int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Post currentPost = getItem(position);

		View rowView = null;
		// Inflate the views from XML
		if (currentPost.getType() == PostType.PICTURE) {
			rowView = inflater.inflate(R.layout.my_wall_list_row_picture, null);
			
			// Load the image and set it on the ImageView
			ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
			imageView.setImageDrawable(new BitmapDrawable(null, currentPost.getImage()));
		} else {
			rowView = inflater.inflate(R.layout.my_wall_list_row_text, null);
		}

		// Set the text on the TextView
		TextView textView = (TextView) rowView.findViewById(R.id.post);
		textView.setText(currentPost.getText());

		return rowView;
	}
	
}
