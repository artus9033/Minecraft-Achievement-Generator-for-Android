package pl.artus.mcpe.mag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.os.*;
import java.io.*;
import android.graphics.*;
import android.util.*;
import android.app.*;
import android.widget.*;
import android.view.*;
import android.widget.AdapterView.*;
import android.location.*;
import android.preference.*;


public class MainActivity extends Activity {
    static EditText gEdit;
    static EditText sEdit;
    boolean firstRun = true;
    String TAG = "MAG Main Thread";
    private GridView gridView;
    private GridViewAdapter gridAdapter;
	String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MAG/achievements";
	Typeface mainFont;
	Dialog mwindow;
	String imgType = null;

    public static String getMainText() {
        return gEdit.getText().toString();
    }

    public static String getSecondaryText() {
        return sEdit.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
		try{
		mainFont = FontLoader.getFontFromRes(R.raw.minecraftia, MainActivity.this);
        gEdit = (EditText) findViewById(R.id.g_tekst);
        sEdit = (EditText) findViewById(R.id.s_tekst);
        ArtusIconButton kgl = (ArtusIconButton) findViewById(R.id.kolorgl);
        kgl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    selIcon();
            }
        });
        kgl.setBackgroundResource(DrawingView.currentIcon);
        LinearLayout layout = new LinearLayout(MainActivity.this);
        ImageView image = new ImageView(MainActivity.this);
        image.setImageResource(R.drawable.logo);
        layout.addView(image);
        final Toast mtoast = new Toast(getApplicationContext());
        mtoast.setGravity(Gravity.BOTTOM, 0, 0);
        mtoast.setDuration(Toast.LENGTH_LONG);
        mtoast.setView(layout);
		TimerTask tt = new TimerTask(){
			public void run(){
        mtoast.show();
		}
		};
		Timer t = new Timer();
		t.schedule(tt, 2000);
		File mf = new File(dir);
		if(!mf.exists()){
			mf.mkdirs();
		}
		initPrefs();
		showTip();
		initIcons();
		}finally{
		mwindow = new Dialog(MainActivity.this);
		mwindow.setContentView(gridView);
		mwindow.setTitle("Choose an icon");
        initInput();
		}
    }
	private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
		Field[] fields = R.drawable.class.getFields();
		try{
        for(int count=0; count < fields.length; count++) {
			int rid = fields[count].getInt(fields[count]);
            String filename = fields[count].getName();
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), rid);
			    bitmap = Bitmap.createScaledBitmap(bitmap,60,60,false);
				if(count == 0){
				Bitmap b = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_delete);
				imageItems.add(new ImageItem(b, "Cancel", "Cancel",rid));
				}
				if(rid!=R.drawable.mca&&rid!=R.drawable.logo&&rid!=R.drawable.splashimage){
                imageItems.add(new ImageItem(bitmap, "", "",rid));
				}
			}			}
		catch (IllegalArgumentException e)
		{}
		catch (IllegalAccessException e)
		{}
        return imageItems;
    }
	
	public void initIcons(){
		LayoutInflater li = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		gridView = (GridView)li.inflate(R.layout.grid, null, false);
        gridAdapter = new GridViewAdapter(MainActivity.this, R.layout.grid_item, getData());
        gridView.setAdapter(gridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			ImageItem item = (ImageItem) parent.getItemAtPosition(position);
			int old;
			old = DrawingView.currentIcon;
			DrawingView.currentIcon=item.getResource();
			ArtusIconButton ib = (ArtusIconButton)findViewById(R.id.kolorgl);
			ib.setBackgroundResource(item.getResource());
			if(item.getTitle() == "Cancel"){
			//avoid choosing the first icon while exiting
			DrawingView.currentIcon = old;
			ib.setBackgroundResource(old);
			}
			mwindow.dismiss();
			Log.e(TAG, "Icon chosen: "+item.getResource());
			findViewById(R.id.canvas).invalidate();
			}
	});
	}
    public void selIcon(){
		LinearLayout layout = new LinearLayout(MainActivity.this);
        ImageView image = new ImageView(MainActivity.this);
        image.setImageBitmap(drawCustomBitmap(MainActivity.this, "Choose an icon!", "Or tap the bin to cancel.", R.drawable.mca, R.drawable.painting, 24));
        layout.addView(image);
        final Toast mtoast = new Toast(getApplicationContext());
        mtoast.setGravity(Gravity.BOTTOM, 0, 0);
        mtoast.setDuration(Toast.LENGTH_SHORT);
        mtoast.setView(layout);
        mtoast.show();
		mwindow.show();
		ArtusIconButton kgl = (ArtusIconButton) findViewById(R.id.kolorgl);
        kgl.setBackgroundResource(DrawingView.currentIcon);
    }

    private void initInput() {
		TextView at = (TextView)findViewById(R.id.kl);
		at.setTypeface(mainFont);
		at.setTextSize(30);
		at.setText("MAG for Android by artus9033");
		at.setShadowLayer(1.0f, 0f, 1.0f, Color.blue(Color.LTGRAY));
        gEdit.setText("Achievement Get!");
        sEdit.setText("Using MAG by artus9033!");
        gEdit.setTextColor(Color.YELLOW);
        sEdit.setTextColor(Color.WHITE);
        gEdit.setShadowLayer(1.0f, 0f, 1.0f, Color.BLACK);
        sEdit.setShadowLayer(1.0f, 0f, 1.0f, Color.BLACK);
        gEdit.setTypeface(mainFont);
        sEdit.setTypeface(mainFont);
        addFloating();
    }

    private void addFloating() {
        final FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save))
                .withButtonColor(Color.alpha(Color.YELLOW))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
		        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Generating achievement...");
				try
				{
					File f = new File(dir);
					int fs = f.listFiles().length;
					fs++;
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
					imgType = sp.getString("MAG_IMG_TYPE", "PNG");
					String lowerType = imgType.toLowerCase();
					String n = "/Achievement"+fs+"."+lowerType;
					buduj(n);
					LinearLayout layout = new LinearLayout(MainActivity.this);
					ImageView image = new ImageView(MainActivity.this);
					Bitmap bm = drawCustomBitmap(MainActivity.this, "Achivement saved to "+Environment.getExternalStorageDirectory().getAbsolutePath().toString()+":", ("MAG/achievements"+n).toString(), R.drawable.mca, R.drawable.cookie,17);
					image.setImageBitmap(bm);
					layout.addView(image);
					final Toast mtoast = new Toast(getApplicationContext());
					mtoast.setGravity(Gravity.BOTTOM, 0, 0);
					mtoast.setDuration(Toast.LENGTH_SHORT);
					mtoast.setView(layout);
					mtoast.show();				}
				catch (IOException e)
				{
					Log.e(TAG, e.toString());
				}
            }
        });
		fabButton.hideNow();
		final FloatingActionButton refresh = new FloatingActionButton.Builder(this)
			.withDrawable(getResources().getDrawable(android.R.drawable.ic_menu_rotate))
			.withButtonColor(Color.alpha(Color.YELLOW))
			.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
			.withMargins(0, 0, 16, 86)
			.create();
		refresh.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				findViewById(R.id.canvas).invalidate();
				}
			});
		refresh.hideNow();
		final Handler ha = new Handler();
		runOnUiThread(new java.lang.Runnable(){ 
		@Override
		public void run(){
		ha.postDelayed(new Runnable(){
			@Override
			public void run(){	
			fabButton.showFloatingActionButton();
			refresh.showFloatingActionButton();
			}
		},Toast.LENGTH_LONG+1800);
		}});
}
    private void buduj(String name) throws IOException {
        DrawingView dw = (DrawingView) findViewById(R.id.canvas);
        dw.invalidate();
		Bitmap source = dw.getDrawingCache();
		int dstW = source.getWidth();
		int dstH = source.getHeight();
		Bitmap cb = Bitmap.createBitmap(dstW, dstH, Bitmap.Config.ARGB_8888);
		cb.setHasAlpha(true);
		cb.eraseColor(Color.TRANSPARENT);
		cb = dw.getDrawingCache();
		String path = dir+name;
OutputStream stream = new FileOutputStream(path);
int qualityInt = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("MAG_IMG_QUALITY", "100"));
if(name.contains("png")){
	Log.e(TAG, "Compressing to png");
	cb.compress(Bitmap.CompressFormat.PNG, 0, stream);
}if(name.contains("webp")){
	Log.e(TAG,"compressing to webp");
	cb.compress(Bitmap.CompressFormat.WEBP, qualityInt, stream);
}
stream.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
     if (id == R.id.settings) {
		 Intent i = new Intent(MainActivity.this, Settings.class);
		 startActivity(i);
     return true;
     }
        return super.onOptionsItemSelected(item);
    }
	
    public final Bitmap drawCustomBitmap(Context gContext, String gText, String sText, int resId, int icId, int u) {
        Resources resources = gContext.getResources();
		Bitmap icbmp = BitmapFactory.decodeResource(resources, icId);
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
        Bitmap.Config bitmapConfig = bitmap.getConfig();
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)gContext.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		int m = 9/2;
		int dsth = (height/m)*(3/2);
		Bitmap fbmp = Bitmap.createScaledBitmap(bitmap, width-80, (bitmap.getHeight()/5)*4, false);
        Paint paint = new Paint();
        paint.setTypeface(mainFont);
        Canvas canvas = new Canvas(fbmp);
        paint.setColor(Color.YELLOW);
        paint.setTextSize((int) (u * scale));
        Rect bounds = new Rect();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(gText, 0, gText.length(), bounds);
		int offset = 34;
        canvas.drawText(gText, -bounds.left + dsth, -bounds.top + offset, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(sText, -bounds.left + dsth, -bounds.top + ((offset*2) + 16), paint);
		int fh = ((fbmp.getHeight()/3)*2)-19;
		Bitmap icbmp_scaled = Bitmap.createScaledBitmap(icbmp, fh, fh, false);
		canvas.drawBitmap(icbmp_scaled,-bounds.left+(scale*13), ((fbmp.getHeight()/5) + (fbmp.getHeight()/18)), paint);
        return fbmp;
    }
	
    private void initPrefs() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
			SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            firstRun = sharedPreferences.getBoolean("MAG_first_run_key", true);
			imgType = sharedPreferences.getString("MAG_IMG_TYPE", "PNG");
			if(sharedPreferences.getString("MAG_IMG_TYPE", "empty")=="empty"){
				prefEditor.putString("MAG_IMG_TYPE", "PNG");
			}
			if(imgType==""||imgType==" "){
				imgType="PNG";
			}
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "An error occured: " + e.toString(), Toast.LENGTH_LONG);
            firstRun = true;
        }
    }

    private void showTip() {
        if (firstRun) {

			}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean("MAG_first_run_key", false);
        prefEditor.commit();
    }

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean("MAG_first_run_key", false);
        prefEditor.commit();
	}
}
