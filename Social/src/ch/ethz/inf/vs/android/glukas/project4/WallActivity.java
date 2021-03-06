package ch.ethz.inf.vs.android.glukas.project4;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Protocol;
import ch.ethz.inf.vs.android.glukas.project4.protocol.ProtocolInterface;

public class WallActivity extends Activity implements UserDelegate, OnScrollListener {

	private static final int PICTURE_GALLERY = 1;
	private static final int PICTURE_TAKEN = 2;

	private DatabaseAccess dbmanager;
	
	protected ProtocolInterface mProtocol;

	protected WallPostAdapter userWallAdapter;
	
	protected User wallOwner = null;
	
	private EditText textField;
	private ImageView postPicture;
	private Button cameraButton;
	
	private Bitmap currentPicture;
	
	////
	//LIFECYCLE
	////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		dbmanager = new DatabaseManager(this);

		//Protocol instantiation
		mProtocol = Protocol.getInstance(dbmanager);
		mProtocol.setDelegate(this);
		
		userWallAdapter = new WallPostAdapter(getApplicationContext(), mProtocol.getUserMapping());
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mProtocol.getUser() != null) {
			mProtocol.disconnect();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mProtocol.setDelegate(this);
		
		if (mProtocol.getUser() != null) {
			mProtocol.connect();
		}
		updateWall();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.userWallAdapter.clear();
	}
	
	protected void updateWall() {
		if (wallOwner != null) {
			if (this.userWallAdapter.isEmpty()) {
				mProtocol.getSomeUserPosts(wallOwner.id, Integer.MAX_VALUE);
			} else {
				mProtocol.getUserPosts(wallOwner.id, userWallAdapter.getItem(0).getId());
			}
			
		}
	}
	
	
	////
	//Posting to wall
	////

	public void sendPost (View v) {
		Editable post = textField.getText();
		Drawable picture =  postPicture.getDrawable();
		if (!post.toString().isEmpty() || picture != null) {
			mProtocol.post(wallOwner.id, post.toString(), picture == null ? null:currentPicture);
			textField.setText("");
			textField.clearFocus();
			removePictureFromPost();
		}
	}

	public void addPicture (View v) {
		final CharSequence[] options = { "Take Picture", "Choose from Gallery","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(WallActivity.this);
		builder.setTitle("Add picture to post");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Take Picture"))
				{
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, PICTURE_TAKEN);
				}
				else if (options[item].equals("Choose from Gallery"))
				{
					Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, PICTURE_GALLERY);

				}
				else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PICTURE_TAKEN) {
				
				File f = new File(Environment.getExternalStorageDirectory().toString());//TODO we should not use external storage as it is less safe
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					Bitmap bitmap;
					BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

					bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
							bitmapOptions); 
					
					bitmap = Bitmap.createScaledBitmap(bitmap, 
							textField.getWidth() - 40,
							(int)(bitmap.getHeight() * ((float)((textField.getWidth() - 20))/bitmap.getWidth())), 
							false);
					Log.i(this.getClass().toString(), "w : " + bitmap.getWidth() + ", h: " + bitmap.getHeight());
					currentPicture = bitmap;
					
					bitmap = getRoundedCornerBitmap(bitmap);

					addPictureToPost(bitmap);
					postPicture.setImageBitmap(bitmap);
					postPicture.setPadding(0, 30, 0, 0);
					
					f.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == PICTURE_GALLERY) {

				Uri selectedImage = data.getData();
				String[] filePath = { MediaStore.Images.Media.DATA };
				Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePath[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
				
				thumbnail = Bitmap.createScaledBitmap(thumbnail, 
						textField.getWidth() - 40,
						(int)(thumbnail.getHeight() * ((float)((textField.getWidth() - 20))/thumbnail.getWidth())), 
						false);
				currentPicture = thumbnail;
				thumbnail = getRoundedCornerBitmap(thumbnail);

				addPictureToPost(thumbnail);
			}
		}
	}   

	////
	//Fragment
	///

	public class WallFragment extends ListFragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			this.getListView().setOnScrollListener(WallActivity.this);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			this.setListAdapter(userWallAdapter);
			
			View view = inflater.inflate(R.layout.my_wall_tab, container, false);
			textField = (EditText) view.findViewById(R.id.text);
			textField.setOnFocusChangeListener(new OnFocusChangeListener() {          

				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						InputMethodManager imm = (InputMethodManager)getSystemService(
								Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(textField.getWindowToken(), 0);
						
					} else {
						setSelection(0);
					}
				}
			});

			postPicture = (ImageView) view.findViewById(R.id.postPicture);
			cameraButton = (Button) view.findViewById(R.id.cameraButton);
			return view;
		}
	}

	////
	//UserDelegate
	////

	@Override
	public void onPostReceived(Post post) {
		// only add if it belongs to this users wall
		if (post.getWallOwner().equals(this.wallOwner.id)) {
			this.userWallAdapter.add(post);
		}
	}

	@Override
	public void onConnectionFailed(FailureReason reason) {
		Log.e(this.getClass().toString(), reason.toString());
	}

	////
	// Helper functions
	////

	private static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 20;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output ;
	}

	private void addPictureToPost(Bitmap bitmap) {
		if (postPicture != null) {
			postPicture.setImageBitmap(bitmap);
			postPicture.setPadding(0, 30, 0, 0);

			if (!postPicture.hasOnClickListeners()) {
				postPicture.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						final CharSequence[] options = { "Change picture", "Remove picture","Cancel" };

						AlertDialog.Builder builder = new AlertDialog.Builder(WallActivity.this);
						builder.setTitle("Picture settings");
						builder.setItems(options, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int item) {
								if (options[item].equals("Change picture"))
								{
									cameraButton.callOnClick();
								}
								else if (options[item].equals("Remove picture"))
								{
									removePictureFromPost();
								}
								else if (options[item].equals("Cancel")) {
									dialog.dismiss();
								}
							}
						});
						builder.show();
					}
				});
			}
		}
	}

	private void removePictureFromPost() {
		postPicture.setPadding(0, 0, 0, 0);
		postPicture.setImageDrawable(null);
	}
	
	////
	//OnScrollListener
	////

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
	}
	
}
