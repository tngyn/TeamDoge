package com.teamdoge.restaurantapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;

public class View_Profile extends FragmentActivity {

	// establish Text views
	private TextView userNameText;
	private TextView userEmailText;
	private TextView userAcctText;
	private TextView userPNumberText;
	private TextView userUserNameText;

	// Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;



	//** Called when the user clicks the set Available shifts button */
//	public void setAvailableTimes(View view) {
//    	Intent intent = new Intent(View_Profile.this, ProfileActivity.class);
//
//		startActivity(intent);
//	}

	//** Called on when the edit button on the action bar is pressed.
	public void openEdit() {
    	Intent intent = new Intent(View_Profile.this, Edit_Profile.class);

		startActivity(intent);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle("Profile"); 
		setContentView(R.layout.activity_view__profile);

		// setup initial Text views
		userNameText = (TextView) findViewById(R.id.userNameText);
		userUserNameText = (TextView) findViewById(R.id.userUserNameText);
		userEmailText = (TextView) findViewById(R.id.userEmailText);
		userAcctText = (TextView) findViewById(R.id.userAcctText);
		userPNumberText = (TextView) findViewById(R.id.userPNumberText);

		// link to parse
		Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", "fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");

		// pull values from data base
//		ParseUser user = ParseUser.getCurrentUser();
//		String tempUserName = user.getUsername();
//		String tempEmail = user.getEmail();
//		String accountType = user.getString("Acc_Type");
//		String tempRegName = user.getString("Name");
//		String tempPhone = user.getString("PhoneNumber");

		// set text equal to data base values
		userNameText.setText("Hannah");
		userUserNameText.setText("Hannah");
		userEmailText.setText("Hannah");
		userAcctText.setText("Hannah");
		userPNumberText.setText("Hannah");
		
		final View thumb1View = findViewById(R.id.image_button_1);
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(thumb1View, R.drawable.blank_profile);
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
	}
	
	private void zoomImageFromThumb(final View thumbView, int imageResId) {
	    // If there's an animation in progress, cancel it
	    // immediately and proceed with this one.
	    if (mCurrentAnimator != null) {
	        mCurrentAnimator.cancel();
	    }

	    // Load the high-resolution "zoomed-in" image.
	    final ImageView expandedImageView = (ImageView) findViewById(
	            R.id.expanded_image);
	    expandedImageView.setImageResource(imageResId);

	    // Calculate the starting and ending bounds for the zoomed-in image.
	    // This step involves lots of math. Yay, math.
	    final Rect startBounds = new Rect();
	    final Rect finalBounds = new Rect();
	    final Point globalOffset = new Point();

	    // The start bounds are the global visible rectangle of the thumbnail,
	    // and the final bounds are the global visible rectangle of the container
	    // view. Also set the container view's offset as the origin for the
	    // bounds, since that's the origin for the positioning animation
	    // properties (X, Y).
	    thumbView.getGlobalVisibleRect(startBounds);
	    findViewById(R.id.container)
	            .getGlobalVisibleRect(finalBounds, globalOffset);
	    startBounds.offset(-globalOffset.x, -globalOffset.y);
	    finalBounds.offset(-globalOffset.x, -globalOffset.y);

	    // Adjust the start bounds to be the same aspect ratio as the final
	    // bounds using the "center crop" technique. This prevents undesirable
	    // stretching during the animation. Also calculate the start scaling
	    // factor (the end scaling factor is always 1.0).
	    float startScale;
	    if ((float) finalBounds.width() / finalBounds.height()
	            > (float) startBounds.width() / startBounds.height()) {
	        // Extend start bounds horizontally
	        startScale = (float) startBounds.height() / finalBounds.height();
	        float startWidth = startScale * finalBounds.width();
	        float deltaWidth = (startWidth - startBounds.width()) / 2;
	        startBounds.left -= deltaWidth;
	        startBounds.right += deltaWidth;
	    } else {
	        // Extend start bounds vertically
	        startScale = (float) startBounds.width() / finalBounds.width();
	        float startHeight = startScale * finalBounds.height();
	        float deltaHeight = (startHeight - startBounds.height()) / 2;
	        startBounds.top -= deltaHeight;
	        startBounds.bottom += deltaHeight;
	    }

	    // Hide the thumbnail and show the zoomed-in view. When the animation
	    // begins, it will position the zoomed-in view in the place of the
	    // thumbnail.
	    thumbView.setAlpha(0f);
	    expandedImageView.setVisibility(View.VISIBLE);

	    // Set the pivot point for SCALE_X and SCALE_Y transformations
	    // to the top-left corner of the zoomed-in view (the default
	    // is the center of the view).
	    expandedImageView.setPivotX(0f);
	    expandedImageView.setPivotY(0f);

	    // Construct and run the parallel animation of the four translation and
	    // scale properties (X, Y, SCALE_X, and SCALE_Y).
	    AnimatorSet set = new AnimatorSet();
	    set
	            .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
	                    startBounds.left, finalBounds.left))
	            .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
	                    startBounds.top, finalBounds.top))
	            .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
	            startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
	                    View.SCALE_Y, startScale, 1f));
	    set.setDuration(mShortAnimationDuration);
	    set.setInterpolator(new DecelerateInterpolator());
	    set.addListener(new AnimatorListenerAdapter() {
	        @Override
	        public void onAnimationEnd(Animator animation) {
	            mCurrentAnimator = null;
	        }

	        @Override
	        public void onAnimationCancel(Animator animation) {
	            mCurrentAnimator = null;
	        }
	    });
	    set.start();
	    mCurrentAnimator = set;

	    // Upon clicking the zoomed-in image, it should zoom back down
	    // to the original bounds and show the thumbnail instead of
	    // the expanded image.
	    final float startScaleFinal = startScale;
	    expandedImageView.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	            if (mCurrentAnimator != null) {
	                mCurrentAnimator.cancel();
	            }

	            // Animate the four positioning/sizing properties in parallel,
	            // back to their original values.
	            AnimatorSet set = new AnimatorSet();
	            set.play(ObjectAnimator
	                        .ofFloat(expandedImageView, View.X, startBounds.left))
	                        .with(ObjectAnimator
	                                .ofFloat(expandedImageView, 
	                                        View.Y,startBounds.top))
	                        .with(ObjectAnimator
	                                .ofFloat(expandedImageView, 
	                                        View.SCALE_X, startScaleFinal))
	                        .with(ObjectAnimator
	                                .ofFloat(expandedImageView, 
	                                        View.SCALE_Y, startScaleFinal));
	            set.setDuration(mShortAnimationDuration);
	            set.setInterpolator(new DecelerateInterpolator());
	            set.addListener(new AnimatorListenerAdapter() {
	                @Override
	                public void onAnimationEnd(Animator animation) {
	                    thumbView.setAlpha(1f);
	                    expandedImageView.setVisibility(View.GONE);
	                    mCurrentAnimator = null;
	                }

	                @Override
	                public void onAnimationCancel(Animator animation) {
	                    thumbView.setAlpha(1f);
	                    expandedImageView.setVisibility(View.GONE);
	                    mCurrentAnimator = null;
	                }
	            });
	            set.start();
	            mCurrentAnimator = set;
	        }
	    });
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view__profile, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.actionEdit:
	            openEdit();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


}