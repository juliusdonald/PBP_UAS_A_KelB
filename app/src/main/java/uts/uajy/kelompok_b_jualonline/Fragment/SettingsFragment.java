package uts.uajy.kelompok_b_jualonline.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import uts.uajy.kelompok_b_jualonline.MainActivity;
import uts.uajy.kelompok_b_jualonline.MapActivity;
import uts.uajy.kelompok_b_jualonline.R;
import uts.uajy.kelompok_b_jualonline.SplashScreen;
import uts.uajy.kelompok_b_jualonline.persistencedata.sharedpref;

import static android.content.Context.SENSOR_SERVICE;

public class SettingsFragment extends Fragment implements SensorEventListener {
    public SwitchMaterial themeSwitch;
    public Boolean checkSwitch;
    public MaterialButton btnAboutUs;
    public Boolean check;

    //shake
    private long lastUpdate=0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        themeSwitch = view.findViewById(R.id.themeSwitch);
        btnAboutUs = view.findViewById(R.id.btnAboutUs);
        load();
        check=null;
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                save();
                if (b == true) {
                    check = true;
                }
                else {
                    check=false;
                }
                Toast.makeText(view.getContext(), "Theme changed, shake to see the difference", Toast.LENGTH_SHORT).show();
            }
        });
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MapActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    public void load() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("filename",Context.MODE_PRIVATE);
        checkSwitch = sharedPreferences.getBoolean("NightMode",false);
        if (checkSwitch) {
            getContext().setTheme(R.style.darktheme);
            themeSwitch.setChecked(true);
        }
        else {
            getContext().setTheme(R.style.AppTheme);
            themeSwitch.setChecked(false);
        }
    }

    public void save() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("filename",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NightMode",themeSwitch.isChecked());
        editor.commit();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[0];
            float z = sensorEvent.values[0];

            long curTime = System.currentTimeMillis();

            if((curTime-lastUpdate)>100) {
                long diffTime = (curTime-lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x+y+z-last_x-last_y-last_z)/diffTime*10000;

                if(speed>SHAKE_THRESHOLD) {
                    if (check!=null) {
                        if (check == true) {
                            getContext().setTheme(R.style.darktheme);
                        }
                        else {
                            getContext().setTheme(R.style.darktheme);
                        }
                        Toast.makeText(view.getContext(), "SHAKE", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(view.getContext(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        Log.d("key", "asd");
                    }
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}