package uts.uajy.kelompok_b_jualonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uts.uajy.kelompok_b_jualonline.api.TransaksiAPI;
import uts.uajy.kelompok_b_jualonline.api.UserAPI;
import uts.uajy.kelompok_b_jualonline.model.User;
import www.sanju.motiontoast.MotionToast;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class EditProfileActivity extends AppCompatActivity {

    private String id_user;
    TextInputEditText txtnama_depan, txtnama_belakang, txtalamat, txttanggal_lahir, txtnomor_telepon, txtemail;
    MaterialButton btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //get the user id
        loadUserId();

        //get the whole user, then set into their corresponing forms
        getUser(Integer.parseInt(id_user));

        // form definitions
        txtnama_depan = findViewById(R.id.txtInputNamaDepanEdit);
        txtnama_belakang = findViewById(R.id.txtInputNamaBelakangEdit);
        txtalamat = findViewById(R.id.txtInputAlamatEdit);
        txttanggal_lahir = findViewById(R.id.txtInputTanggalLahirEdit);
        txtnomor_telepon = findViewById(R.id.txtInputNomorTeleponEdit);
        txtemail = findViewById(R.id.txtInputEmailEdit);
        btnSubmit = findViewById(R.id.btnSubmitEdit);

        //disable user input email
        txtemail.setEnabled(false);
        txtemail.setInputType(InputType.TYPE_NULL);
        txtemail.setFocusable(false);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtnama_depan.getText().toString().isEmpty()) {
                    txtnama_depan.setError("Nama depan tidak boleh kosong");
                }
                if (txtnama_belakang.getText().toString().isEmpty()) {
                    txtnama_belakang.setError("Nama belakang tidak boleh kosong");
                }
                if (txtalamat.getText().toString().isEmpty()) {
                    txtalamat.setError("Alamat tidak boleh kosong");
                }
                if (txttanggal_lahir.getText().toString().isEmpty()) {
                    txttanggal_lahir.setError("Tanggal lahir tidak boleh kosong");
                }
                if (txtnomor_telepon.getText().toString().isEmpty()) {
                    txtnomor_telepon.setError("Nomor telepon tidak boleh kosong");
                }
                if (checkAll()){
                    updateUser(Integer.parseInt(id_user),
                               txtnama_depan.getText().toString(),
                               txtnama_belakang.getText().toString(),
                               txtalamat.getText().toString(),
                               txttanggal_lahir.getText().toString(),
                               txtnomor_telepon.getText().toString());

                }
            }
        });
    }

    public boolean checkAll(){
        return  !txtnama_depan.getText().toString().isEmpty() &&
                !txtnama_belakang.getText().toString().isEmpty() &&
                !txtalamat.getText().toString().isEmpty() &&
                !txttanggal_lahir.getText().toString().isEmpty() &&
                !txtnomor_telepon.getText().toString().isEmpty();
    }

    public void loadUserId(){
        SharedPreferences sharedPreferences = getSharedPreferences("id_user", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("id_user","");
    }

    public void getUser(int id_user_parameter) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

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

                    txtnama_depan.setText(nama_depan);
                    txtnama_belakang.setText(nama_belakang);
                    txtalamat.setText(alamat);
                    txttanggal_lahir.setText(tanggal_lahir);
                    txtnomor_telepon.setText(nomor_telepon);
                    txtemail.setText(email);

                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Disini bagian jika response jaringan terdapat ganguan/error
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void updateUser(int idUser, String namaDepan, String namaBelakang, String alamat, String tanggalLahir, String nomorTelepon){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_UPDATE + idUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error

                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    MotionToast.Companion.createColorToast(EditProfileActivity.this,"Profile Updated !",obj.getString("message"),
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_regular));
                    onBackPressed();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
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
                params.put("nama_depan",namaDepan);
                params.put("nama_belakang",namaBelakang);
                params.put("alamat",alamat);
                params.put("nomor_telepon",nomorTelepon);
                params.put("tanggal_lahir",tanggalLahir);

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}