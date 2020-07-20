package info.myklinik.myklinikv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginPatientActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Animation bottomAnim;
    LinearLayout linearLayout;
    TextInputEditText email, password;
    Button forPas, logIn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.slogan_name);
        forPas = findViewById(R.id.forPas);
        logIn = findViewById(R.id.logIn);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        linearLayout = findViewById(R.id.layout2);

        linearLayout.setAnimation(bottomAnim);
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (mFirebaseUser != null) {
                    final String uid = mFirebaseUser.getUid();
                    Query userQuery = FirebaseDatabase.getInstance().getReference()
                            .child("users").orderByKey().equalTo(uid);
                    userQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String type = dataSnapshot.child(uid).child("type").getValue().toString();
                            if (type.equals("Patient")) {
                                Toast.makeText(LoginPatientActivity.this, "You are logged in!",
                                        Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginPatientActivity.this, HomePatientActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(LoginPatientActivity.this, "Please Login.", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    return;
                }
            }
        };

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e_mail = email.getText().toString();
                String pwd = password.getText().toString();
                if (e_mail.isEmpty()) {
                    email.setError("Please enter email id");
                    email.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter password");
                    password.requestFocus();
                } else if (e_mail.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginPatientActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                } else if (!(e_mail.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(e_mail, pwd).addOnCompleteListener(
                            LoginPatientActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginPatientActivity.this,
                                                "Login Error, Please Login Again.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                                        final String uid = mFirebaseUser.getUid();
                                        Query userQuery = FirebaseDatabase.getInstance().getReference()
                                                .child("users").orderByKey().equalTo(uid);
                                        userQuery.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                                String type = dataSnapshot.child(uid).child("type").getValue().toString();
                                                if (type.equals("Patient")) {
                                                    Intent intToHome = new Intent(LoginPatientActivity.this,
                                                            HomePatientActivity.class);
                                                    startActivity(intToHome);
                                                } else {
                                                    Toast.makeText(LoginPatientActivity.this, "Login Error, " +
                                                            "Please Login at doctor.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                }
                            });
                } else {
                    Toast.makeText(LoginPatientActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();

                }
            }


        });
    }

    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
