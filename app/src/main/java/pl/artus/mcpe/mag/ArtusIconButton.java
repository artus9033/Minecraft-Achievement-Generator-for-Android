package pl.artus.mcpe.mag;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

public class ArtusIconButton extends Button {

    private static final String TAG = "Artus Icon Button";
    Context ktx;

    public ArtusIconButton(Context context) {
        super(context);
        ktx = context;
        if (!isInEditMode()) {
            this.setTypeface(getFontFromRes(R.raw.minecraftia));
        }
    }

    public ArtusIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ktx = context;
        if (!isInEditMode()) {
            this.setTypeface(getFontFromRes(R.raw.minecraftia));
        }
    }

    public ArtusIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ktx = context;
        if (!isInEditMode()) {
            this.setTypeface(getFontFromRes(R.raw.minecraftia));
        }
    }

	private Typeface getFontFromRes(int id)
	{
		return FontLoader.getFontFromRes(id, ktx);
	}
	
}
