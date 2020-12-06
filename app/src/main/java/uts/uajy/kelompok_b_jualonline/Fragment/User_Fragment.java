package uts.uajy.kelompok_b_jualonline.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import uts.uajy.kelompok_b_jualonline.ActivityLogin;
import uts.uajy.kelompok_b_jualonline.EditProfileActivity;
import uts.uajy.kelompok_b_jualonline.MainActivity;
import uts.uajy.kelompok_b_jualonline.R;
import uts.uajy.kelompok_b_jualonline.api.BarangAPI;
import uts.uajy.kelompok_b_jualonline.api.UserAPI;
import uts.uajy.kelompok_b_jualonline.model.Barang;
import uts.uajy.kelompok_b_jualonline.model.User;
import uts.uajy.kelompok_b_jualonline.persistencedata.sharedpref;

import static com.android.volley.Request.Method.GET;

public class User_Fragment extends Fragment {
    private FloatingActionButton btnSettings;
    private MaterialButton btnUpdateProfilePicture,btnSignout, btnEdit;
    public SettingsFragment settingsFragment;
    public MaterialTextView txtNama, txtNamaFull, txtAlamat, txtTanggalLahir, txtNomorTelepon,txtEmail;

    //hardware
    private long lastUpdate=0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int CAMERA_PERMISSION_CODE = 1;

    private String CHANNEL_ID = "Channel 1" ;
    private String id_user;

    private User user;

    private SwipeRefreshLayout swipe_profile;

    Boolean checkTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("filename", Context.MODE_PRIVATE);
        checkTheme = sharedPreferences.getBoolean("NightMode",false);
        if(checkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getContext().setTheme(R.style.darktheme);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getContext().setTheme(R.style.AppTheme);
        }



        //load id user
        loadUserId();

        // get user dari id
        getUser(Integer.parseInt(id_user), view);


        //retrieve the email
        txtNama = view.findViewById(R.id.Profile);
        txtNamaFull = view.findViewById(R.id.txtNama);
        txtAlamat = view.findViewById(R.id.txtAlamat);
        txtNomorTelepon = view.findViewById(R.id.txtNomorTelepon);
        txtEmail = view.findViewById(R.id.txtEmail);
        swipe_profile = view.findViewById(R.id.swipe_profile);

        SharedPreferences sharedEmail = getContext().getSharedPreferences("userEmail",Context.MODE_PRIVATE);
        String email = sharedEmail.getString("email","");
        txtEmail.setText(email);

        settingsFragment = new SettingsFragment();
        btnSettings = view.findViewById(R.id.fab_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment addFragment = new SettingsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(User_Fragment.this);
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment,settingsFragment);
                transaction.commit();
            }
        });

        // INI BUTTON UNTUK UPDATE PROFILE PICTURE
        btnUpdateProfilePicture = view.findViewById(R.id.btnUpdateProfile);
        btnUpdateProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("TENTANG DEVELOPER")
                        .setMessage("Aplikasi PBP Mart dibuat dan dikembangkan oleh kelompok B :\n\n\n" +
                                "1. Cornellius Philipo Julianto / 180709605\n" +
                                "2. Daniel Axcella Kurniawan / 180709738\n" +
                                "3. Julius Donald Giftiardi / 180709834")
                        .show();
            }
        });

        btnSignout = view.findViewById(R.id.btn_signout);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Sign Out Alert")
                        .setMessage("Are you sure want to sign out ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                FirebaseAuth.getInstance().signOut();
                                deleteUserId(view);
                                createNotificationChannel(view);
                                addNotificaion("Goodbye","Comeback Again...",view);
                                startActivity(new Intent(view.getContext(), ActivityLogin.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        swipe_profile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUser(Integer.parseInt(id_user), view);
                swipe_profile.setRefreshing(false);
            }
        });
        return view;
    }

    public void loadUserId(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("id_user", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("id_user","");
    }

    public void deleteUserId(View view) {
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("id_user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_user",null);
        editor.commit();
    }

    public void createNotificationChannel(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel 1";
            String description = "This is Channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = view.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    public void addNotificaion(String title, String desc, View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(Notification.PRIORITY_DEFAULT);


        //intent yang menampilkan notifikasi
        Intent notificationIntent = new Intent(view.getContext(), MainActivity.class);
        PendingIntent ContentIntent = PendingIntent.getActivity(view.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(ContentIntent);

        //tampil notifikasi
        NotificationManager manager = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void getUser(int id_user_parameter, View view) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_GET_ID + String.valueOf(id_user_parameter), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {

                    //Mengambil data response json object yang berupa data barang
                    JSONObject jsonObject = response.getJSONObject("data");

                    int id                      = jsonObject.optInt("id");
                    String nama_depan           = jsonObject.optString("nama_depan");
                    String nama_belakang        = jsonObject.optString("nama_belakang");
                    String alamat               = jsonObject.optString("alamat");
                    String tanggal_lahir        = jsonObject.optString("tanggal_lahir");
                    String nomor_telepon        = jsonObject.optString("nomor_telepon");
                    String email                = jsonObject.optString("email");
                    String imageUrl             = jsonObject.optString("imageUrl");

                    //Membuat objek user
                    User userKetemu = new User(id, nama_depan, nama_belakang, alamat, tanggal_lahir, nomor_telepon, email, imageUrl);

                    txtNama.setText(nama_depan);
                    txtNamaFull.setText(nama_depan+" "+nama_belakang);
                    txtAlamat.setText(alamat);
                    txtNomorTelepon.setText(nomor_telepon);
                    txtEmail.setText(email);

                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}