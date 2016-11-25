package pl.artus.mcpe.mag;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;
import android.util.*;
import android.hardware.display.*;
import android.view.*;
import android.graphics.*;

public class DrawingView extends View {
    private static final String TAG = "MAG Drawing Thread";
    Context ktx;
    public static int currentIcon;
    Bitmap bg_bmp;
    private Paint drawPaint, canvasPaint;
    public Canvas drawCanvas;
    private Bitmap canvasBitmap;
	Typeface tf;
	ArtusIconButton kgl = (ArtusIconButton)findViewById(R.id.kolorgl);
	
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
            if (!isInEditMode()) {
                setupDrawing(context);
				this.setDrawingCacheEnabled(true);
            }
        }

    private void setupDrawing(Context ctx){
        ktx = ctx;
        currentIcon = R.drawable.grass;
        drawPaint = new Paint();
        drawPaint.setColor(Color.TRANSPARENT);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        bg_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mca);
        invalidate();
		findViewById(R.id.canvas).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				Log.i(TAG, "Redrawing the Canvas");
				findViewById(R.id.canvas).invalidate();
			}
		});
    }

    private int mWidth;
    private int mHeight;

    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);
    }
	
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = bg_bmp;
        Bitmap mutableBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mca).copy(Bitmap.Config.ARGB_8888, true);
        drawCanvas = new Canvas();
        drawCanvas.drawBitmap(mutableBitmap,w,h,canvasPaint);
    }
	
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
  		if(bg_bmp == null){
        bg_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mca);
		}
		bg_bmp.setHasAlpha(true);
        canvasBitmap = bg_bmp;
		canvasBitmap.setHasAlpha(true);
		Bitmap mb = drawTextToBitmap(ktx, MainActivity.getMainText(), MainActivity.getSecondaryText(), R.drawable.mca, currentIcon);
		mb.setHasAlpha(true);
		int cx = mWidth - mb.getWidth()>>1;
		int cy = mHeight - mb.getHeight() >> 1; 
        canvas.drawBitmap(mb, cx, cy, canvasPaint);
        }
	
    public  Bitmap drawTextToBitmap(Context gContext, String gText, String sText, int resId, int icId) {
        Resources resources = gContext.getResources();
		Bitmap icbmp = BitmapFactory.decodeResource(resources, icId);
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
        Bitmap.Config bitmapConfig = bitmap.getConfig();
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
		bitmap.setHasAlpha(true);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)gContext.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		int m = 9/2;
		int dsth = (height/m)*(3/2);
		Bitmap fbmp = Bitmap.createScaledBitmap(bitmap, width-80, (bitmap.getHeight()/5)*4, false);
        if(tf == null){
		tf = FontLoader.getFontFromRes(R.raw.minecraftia, gContext);
		}
        Paint paint = new Paint();
        paint.setTypeface(tf);
        Canvas canvas = new Canvas(fbmp);
        paint.setColor(Color.YELLOW);
        paint.setTextSize((int) (26 * scale));
        Rect bounds = new Rect();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(gText, 0, gText.length(), bounds);
		int offset = 34;
        canvas.drawText(gText, -bounds.left + dsth, -bounds.top + offset, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(sText, -bounds.left + dsth, -bounds.top + ((offset*2) + 16), paint);
		int fh = ((fbmp.getHeight()/3)*2)-19;
		Bitmap icbmp_scaled = Bitmap.createScaledBitmap(icbmp, fh, fh, false);
		icbmp_scaled.setHasAlpha(true);
		canvas.drawBitmap(icbmp_scaled,-bounds.left+(scale*13), ((fbmp.getHeight()/5) + (fbmp.getHeight()/18)), paint);
		fbmp.setHasAlpha(true);
        return fbmp;
    }
	}
