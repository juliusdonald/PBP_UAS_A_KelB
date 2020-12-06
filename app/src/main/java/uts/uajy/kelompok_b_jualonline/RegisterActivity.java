package uts.uajy.kelompok_b_jualonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
//import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


import uts.uajy.kelompok_b_jualonline.api.UserAPI;
import www.sanju.motiontoast.MotionToast;

import static com.android.volley.Request.Method.POST;
import static java.security.AccessController.getContext;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText txtInputNamaDepan, txtInputNamaBelakang, txtInputAlamat, txtInputTanggalLahir, txtInputNomorTelepon, txtInputEmail, txtInputPassword;
    private MaterialButton btnRegisterF;

    private String namaDepan, namaBelakang, alamat, tanggalLahir, nomorTelepon, email, password;
    String valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        valid = "";

        txtInputNamaDepan = findViewById(R.id.txtInputNamaDepan);
        txtInputNamaBelakang = findViewById(R.id.txtInputNamaBelakang);
        txtInputAlamat = findViewById(R.id.txtInputAlamat);
        txtInputTanggalLahir = findViewById(R.id.txtInputTanggalLahir);
        txtInputNomorTelepon = findViewById(R.id.txtInputNomorTelepon);
        txtInputEmail = findViewById(R.id.txtInputEmail);
        txtInputPassword = findViewById(R.id.txtInputPassword);
        btnRegisterF = findViewById(R.id.btnRegisterF);

        btnRegisterF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (checkRegisterValidity()) {
//                    if(txtInputNamaDepan.getText().toString().isEmpty()) {
//                        valid = "Nama depan tidak boleh kosong";
//                    }
//                    if(txtInputNamaBelakang.getText().toString().isEmpty()){
//                        valid = "Nama belakang tidak boleh kosong";
//                    }
//                    if(txtInputAlamat.getText().toString().isEmpty()) {
//                        valid = "Alamat tidak boleh kosong";
//                    }
//                    if(txtInputNomorTelepon.getText().toString().isEmpty()) {
//                        valid = "Nomor Telepon tidak boleh kosong";
//                    }
//                    if(txtInputEmail.getText().toString().isEmpty()) {
//                        valid = "Email tidak boleh kosong";
//                    }
//                    if(isValidEmailId(txtInputEmail.getText().toString())){
//                        valid = "Format email tidak benar";
//                    }
//                    if(txtInputPassword.getText().toString().isEmpty()) {
//                        valid = "Password tidak boleh kosong";
//                    }
//                    if(txtInputPassword.getText().toString().length() < 6) {
//                        valid = "Password kurang dari 6 digit";
//                    }
//                    MotionToast.Companion.createColorToast(RegisterActivity.this,
//                            "Check Again !",
//                            valid,
//                            MotionToast.TOAST_WARNING,
//                            MotionToast.GRAVITY_BOTTOM,
//                            MotionToast.LONG_DURATION,
//                            ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_regular));
//                }
//                else {
                    registerUser(txtInputNamaDepan.getText().toString(),
                                 txtInputNamaBelakang.getText().toString(),
                                 txtInputAlamat.getText().toString(),
                                 txtInputTanggalLahir.getText().toString(),
                                 txtInputNomorTelepon.getText().toString(),
                                 txtInputEmail.getText().toString(),
                                 txtInputPassword.getText().toString());
                }
//            }
        });
    }

    public boolean checkRegisterValidity() {
        return  txtInputNamaDepan.getText().toString().isEmpty() ||
                txtInputNamaBelakang.getText().toString().isEmpty() ||
                txtInputAlamat.getText().toString().isEmpty() ||
                txtInputTanggalLahir.getText().toString().isEmpty() ||
                txtInputNomorTelepon.getText().toString().isEmpty() ||
                txtInputEmail.getText().toString().isEmpty() ||
                txtInputPassword.getText().toString().isEmpty();
    }

    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void registerUser(String namaDepan, String namaBelakang, String alamat, String tanggalLahir, String nomorTelepon, String email, String password){
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
//                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("MESSAGE >>>>>>>>>>>>>>>>>>>> "+obj.getString("message"));
                    if(obj.getString("message").equals("Please Check Email to Verify Your Account"))
                    {
                        MotionToast.Companion.createToast(RegisterActivity.this,
                                "Registered",
                                obj.getString("message"),
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_regular));

                        Intent i = new Intent(RegisterActivity.this,ActivityLogin.class);
                        startActivity(i);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Toast.makeText(getApplicationContext(), jsonError, Toast.LENGTH_SHORT).show();
                }
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
                params.put("nama_depan", namaDepan);
                params.put("nama_belakang", namaBelakang);
                params.put("alamat", alamat);
                params.put("tanggal_lahir", tanggalLahir);
                params.put("nomor_telepon", nomorTelepon);
                params.put("email", email);
                params.put("password", password);
                params.put("imageUrl", "haha");
                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}