package uts.uajy.kelompok_b_jualonline.util;

import android.content.Context;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class FireAuthentication {
    private FireAuthentication auth;

    private static FireAuthentication instance = null;

    private FireAuthentication() {
        this.auth = FireAuthentication.getInstance();
    }
    public Task<AuthResult> Login(String email, String password) {
        return auth.Login(email, password);
    }
    public static FireAuthentication getInstance() {
        if (instance == null)
            instance = new FireAuthentication();
        return instance;
    }
    public void Logout() {
        auth.Logout();
    }

    public Task<AuthResult> Register(String email, String password) {
        return auth.Register(email, password);
    }
    public FirebaseUser getUser() {
        return auth.getUser();
    }
    public FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }
}
