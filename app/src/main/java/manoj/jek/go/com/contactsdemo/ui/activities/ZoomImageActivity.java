package manoj.jek.go.com.contactsdemo.ui.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.soundcloud.android.crop.Crop;

import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;


public class ZoomImageActivity extends AppCompatActivity {

    private ImageView _zoomedImageView;
    private String _imageUri;

    public static final String TRANSITION_VIEW_NAME = "transition_view";
    public static final String IMAGE_URI_EXTRA = "image_extra";

    public static void launch(Activity activity, String imageUri, ImageView transitionView)
    {
        Intent intent = new Intent(activity, ZoomImageActivity.class);
        intent.putExtra(IMAGE_URI_EXTRA, imageUri);
        if(Utils.isLollipopOrAbove()) {
            transitionView.setTransitionName(TRANSITION_VIEW_NAME);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, transitionView, TRANSITION_VIEW_NAME);
            activity.startActivity(intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _imageUri = getIntent().getStringExtra(IMAGE_URI_EXTRA);
        initImageView();
        RelativeLayout layout = new RelativeLayout(this);
        layout.setGravity(Gravity.CENTER);
        layout.addView(_zoomedImageView);
        setContentView(layout);
    }

    private void initImageView() {
        _zoomedImageView = new ImageView(this);
        _zoomedImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        _zoomedImageView.setAdjustViewBounds(true);
        _zoomedImageView.setImageResource(R.drawable.contacts_placeholder);
        if(Utils.isLollipopOrAbove()) {
            _zoomedImageView.setTransitionName(TRANSITION_VIEW_NAME);
        }
        loadImage();
    }

    private void loadImage() {
        Glide.with(this).load(_imageUri).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation animation) {
                _zoomedImageView.setImageBitmap(resource);
            }
        });
    }


}
