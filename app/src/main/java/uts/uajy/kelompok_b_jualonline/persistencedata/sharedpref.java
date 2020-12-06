package uts.uajy.kelompok_b_jualonline.persistencedata;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedpref {
    SharedPreferences mySharedPref;
    public  sharedpref(Context context) {
        mySharedPref = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode",state);
        editor.apply();
    }

    public Boolean loadNightModeState() {
        Boolean state = mySharedPref.getBoolean("NightMode",false);
        return state;
    }
}
