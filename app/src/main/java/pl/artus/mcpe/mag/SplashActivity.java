package pl.artus.mcpe.mag;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.animation.*;
import android.widget.*;
import java.util.*;
import android.graphics.*;
import android.content.pm.*;
import android.view.*;

public class SplashActivity extends Activity{
    private static final String TAG = "MAG Splash Screen";
	Typeface mainFont;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.splash);
		mainFont = FontLoader.getFontFromRes(R.raw.minecraftia, SplashActivity.this);
        LinearLayout ll = (LinearLayout) findViewById(R.id.splashlayout);
        final ImageView i = (ImageView) findViewById(R.id.splashimage);

        final TextView t = (TextView)findViewById(R.id.splashtv);
        t.setTypeface(mainFont);
		final Animation o = AnimationUtils.makeInAnimation(SplashActivity.this, false);
		
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        ll.startAnimation(anim);

        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.translate);
        i.startAnimation(anim2);

        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                outAnim();
                t.setVisibility(TextView.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
		t.startAnimation(o);
    }
        private void outAnim(){
			final Handler ha = new Handler();
			runOnUiThread(new java.lang.Runnable(){ 
					@Override
					public void run(){
						ha.postDelayed(new Runnable(){
								@Override
								public void run(){
									TextView tv = (TextView)findViewById(R.id.splashtv);
									Animation o = AnimationUtils.makeOutAnimation(SplashActivity.this, true);
									tv.startAnimation(o);
								}
							},1);
						}});
            Animation anim3 = AnimationUtils.loadAnimation(this, R.anim.out);
            findViewById(R.id.splashimage).startAnimation(anim3);
            anim3.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.splashimage).setVisibility(ImageView.GONE);
                    Context o = SplashActivity.this;
                    finish();
                    Intent i = new Intent(o, MainActivity.class);
                    o.startActivity(i);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }
}
