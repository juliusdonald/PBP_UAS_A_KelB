package uts.uajy.kelompok_b_jualonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import uts.uajy.kelompok_b_jualonline.api.UserAPI;
import www.sanju.motiontoast.MotionToast;

import static com.android.volley.Request.Method.POST;

public class ActivityLogin extends AppCompatActivity {
    TextInputEditText email,password;
    Button signup;
    Button signin;


    private String CHANNEL_ID = "Channel 1";
    private Boolean checkTheme;

    SharedPreferences sharedPEmail;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //load the theme
        loadTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
//        mFirebaseAuth = FirebaseAuth.getInstance();
        password = findViewById(R.id.pass);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityLogin.this, RegisterActivity.class);
                startActivity(i);
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Email Tidak boleh kosong",Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Password tidak boleh kosong",Toast.LENGTH_SHORT).show();
                }else if(!isValidEmailId(email.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "Email Tidak Valid", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().length()<6){
                    Toast.makeText(getApplicationContext(), "Password Harus 6 Karakter", Toast.LENGTH_SHORT).show();
                }else{
                    String sEmail = email.getText().toString();
                    String sPassword = password.getText().toString();
                    loginUser(sEmail, sPassword);
                }
            }
        });
    }


    public void loadTheme(){
        SharedPreferences sharedPreferences = getSharedPreferences("filename", Context.MODE_PRIVATE);
        checkTheme = sharedPreferences.getBoolean("NightMode",false);
        if(checkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            this.setTheme(R.style.darktheme);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            this.setTheme(R.style.AppTheme);
        }
    }

    public void save(FirebaseUser mFirebaseAuth){
        String helo = mFirebaseAuth.getEmail();
        sharedPEmail = getSharedPreferences("userEmail",Context.MODE_PRIVATE);
        editor = sharedPEmail.edit();
        editor.putString("email",helo);
        editor.apply();
    }

    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Channel 1";
            String description = "This is Channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void addNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Login Success")
                .setContentText("Welcome Back")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }


    public void loginUser(String email, String password){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ActivityLogin.this);
        progressDialog.setMessage("Please wait while loading....");
        progressDialog.setTitle("Logging In");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
//                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("message").equals("Login Success"))
                    {
                        progressDialog.dismiss();

                        createNotificationChannel();
                        addNotification();
                        MotionToast.Companion.createToast(ActivityLogin.this,
                                "Hurray success",
                                "Upload Completed successfully!",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(ActivityLogin.this,R.font.helvetica_regular));
//                        FancyToast.makeText(getApplicationContext(),"Login Success",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true);
//                        Toast.makeText(ActivityLogin.this, "User ID : "+obj.getString("user"), Toast.LENGTH_SHORT).show();

                        //save ke shared pref
                        saveUserId(obj.getString("user"));
                        Intent i = new Intent(ActivityLogin.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getApplicationContext(), "Masuk error response", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void saveUserId(String id) {
        SharedPreferences sharedPreferences = getSharedPreferences("id_user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_user",id);
        editor.commit();
    }
}