package pl.artus.mcpe.mag;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

    private static final String TAG = "Artus Font Loader";

    static Typeface getFontFromRes(int resource, Context ctx) {
        Typeface tf = null;
        InputStream is = null;
        try {
            is = ctx.getResources().openRawResource(resource);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Could not find font in resources!" + e.toString());
        }

        String outPath = ctx.getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw";

        try {
            byte[] buffer = new byte[is.available()];
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

            int l = 0;
            while ((l = is.read(buffer)) > 0)
                bos.write(buffer, 0, l);

            bos.close();

            tf = Typeface.createFromFile(outPath);

            // clean up
            new File(outPath).delete();
        } catch (IOException e) {
            Log.e(TAG, "Error reading font!" + e.toString());
            return null;
        }

        Log.d(TAG, "Successfully loaded font!");

        return tf;
    }
}
