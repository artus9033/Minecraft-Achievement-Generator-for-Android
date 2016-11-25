package pl.artus.mcpe.mag;

import android.preference.*;
import android.os.*;

public class Settings extends PreferenceActivity
{
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   addPreferencesFromResource(R.xml.prefs);
}
}
