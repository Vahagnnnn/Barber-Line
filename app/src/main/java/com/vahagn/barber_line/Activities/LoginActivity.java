package com.vahagn.barber_line.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Classes.Users;
import com.vahagn.barber_line.R;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    EditText email;
    FrameLayout continue_button,testUser_button;
    SignInButton googleSignInButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//        FirebaseAuth.getInstance().signOut();
//        SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//        Toast.makeText(LoginActivity.this, "You have been logged out.", Toast.LENGTH_SHORT).show();
//        MainActivity.isLogin = false;
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();





        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        continue_button = findViewById(R.id.continue_button);
        testUser_button = findViewById(R.id.testUser_button);
        googleSignInButton = findViewById(R.id.sign_in_button);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        continue_button.setOnClickListener(v -> signInUser());
        testUser_button.setOnClickListener(v -> signInTestUser());
        googleSignInButton.setOnClickListener(v -> signIn());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    public void ToPasswordActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String email_str = email.getText().toString().trim();
        editor.putString("email", email_str);
        editor.apply();
        navigateTo(PasswordActivity.class);
    }

    public void signInUser() {
        String email_str = email.getText().toString().trim();
        if (email_str.isEmpty()) {
            email.setError("Email can't be empty");
            Toast.makeText(LoginActivity.this, "Email can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            usersRef.orderByChild("email").equalTo(email_str).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        ToPasswordActivity();
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("email_str", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email_str);
                        editor.apply();
                        navigateTo(RegisterActivity.class);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("DatabaseError", "User check cancelled", databaseError.toException());
                }
            });
        }
    }
    private void signInTestUser() {
        String testEmail = "individualproject2025@gmail.com";
        String testPassword = "Samsung2025";

        mAuth.signInWithEmailAndPassword(testEmail, testPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(LoginActivity.this, "Test user login successful", Toast.LENGTH_SHORT).show();
                            MainActivity.isLogin = true;

                            SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", testEmail);
                            editor.putString("password", testPassword);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Test user login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("FirebaseAuth", "Test login failed", task.getException());
                    }
                });
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());

            mAuth.addAuthStateListener(authStateListener -> {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    sendInfoToPhoneNumberActivity(user, "Google");
                    MainActivity.isLogin = true;
                }
            });
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private void sendInfoToPhoneNumberActivity(FirebaseUser user, String password) {
        String userId = user.getUid();

        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {

                    Log.i("mtav",snapshot.child("photoUrl").getValue(String.class));
                    if (Objects.equals(snapshot.child("photoUrl").getValue(String.class), "https://i.pinimg.com/736x/09/21/fc/0921fc87aa989330b8d403014bf4f340.jpg"))
                    {
                        String photoUrl = String.valueOf(user.getPhotoUrl());
                        usersRef.child(userId).child("photoUrl").setValue(photoUrl);
                        Log.i("mtav","mtav");
                    }
                    Log.i("mtav","helav");
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String fullName = user.getDisplayName();
                    String[] first_name_last_name = fullName.split(" ");
                    String email = user.getEmail();
                    String photoUrl = String.valueOf(user.getPhotoUrl());

                    SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("first_name", first_name_last_name[0]);
                    editor.putString("last_name", first_name_last_name[1]);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.putString("photoUrl", photoUrl);
                    editor.apply();


//                    MainActivity.userClass = new Users(first_name_last_name[0], first_name_last_name[1], email, password, phoneNumber, photoUrl);


//                    MainActivity.userClass = new Users(first_name_last_name[0], first_name_last_name[1], email, password, photoUrl);
//
//                    Log.i("userClass", MainActivity.userClass.getFirst_name());
//                    Log.i("userClass", MainActivity.userClass.getLast_name());
//                    Log.i("userClass", MainActivity.userClass.getEmail());
//                    Log.i("userClass", MainActivity.userClass.getPassword());
//

                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, PhoneNumberActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Database check failed", databaseError.toException());
            }
        });
    }


    private void signIn() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        Log.w("FirebaseAuth", "signInWithCredential:failure", task.getException());
                    }
                });
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }
    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }
}