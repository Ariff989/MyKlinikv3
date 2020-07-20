package info.myklinik.myklinikv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Animation bottomAnim;
    TextInputEditText email, password, username, icNo;
    RadioGroup radioGroup;
    RadioButton radioButton, radioButton1;
    Button signUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference reff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        reff = FirebaseDatabase.getInstance().getReference();
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        mFirebaseAuth = FirebaseAuth.getInstance();

        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        email = findViewById(R.id.email2);
        password = findViewById(R.id.password2);
        username = findViewById(R.id.username2);
        linearLayout = findViewById(R.id.layout2);
        signUp = findViewById(R.id.signUp);
        icNo = findViewById(R.id.icNo2);

        linearLayout.setAnimation(bottomAnim);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(selectedId == -1 || selectedId == 0){
                    username.requestFocus();
                }else{
                    radioButton = (RadioButton) SignUpActivity.this.findViewById(selectedId);
                }
                String e_mail = email.getText().toString();
                String pwd = password.getText().toString();
                String uname = username.getText().toString();
                String icno = icNo.getText().toString();
                final String tipe = radioButton.getText().toString();
                if (uname.isEmpty()) {
                    username.setError("Please enter username");
                    username.requestFocus();
                } else if (e_mail.isEmpty()) {
                    email.setError("Please enter e - mail");
                    email.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter password");
                    password.requestFocus();
                } else if (icno.isEmpty()) {
                    icNo.setError("Please enter IC Number");
                    icNo.requestFocus();
                }else if (tipe.isEmpty() || selectedId == -1 || radioGroup.getCheckedRadioButtonId() == 0) {
                    radioButton.setError("Please choose profession");
                    radioButton.requestFocus();
                } else if (e_mail.isEmpty() && pwd.isEmpty() && uname.isEmpty() && icno.isEmpty() && tipe.isEmpty() && selectedId == -1) {
                    Toast.makeText(SignUpActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                } else if (!(e_mail.isEmpty() && pwd.isEmpty() && uname.isEmpty() && icno.isEmpty() && tipe.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(e_mail, pwd).addOnCompleteListener(SignUpActivity.this,
                            new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                Toast.makeText(SignUpActivity.this, "Sign Up Unsuccessful, Please Try Again."
                                                +  e.getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                if(tipe.equals("Clinic")) {
                                    finish();
                                    onAuthSuccess(task.getResult().getUser());

                                }else {
                                    finish();
                                    onAuthSuccess(task.getResult().getUser());

                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }
    private void onAuthSuccess(FirebaseUser user) {
        //String username = usernameFromEmail(user.getEmail());

         String type = radioButton.getText().toString();

        // Write new user
        writeNewUser(user.getUid(), type);

        // Go to MainActivity
        if(type.equals("Clinic")) {
            startActivity(new Intent(SignUpActivity.this, HomeClinicActivity.class));
            finish();
        } else {
            startActivity(new Intent(SignUpActivity.this, HomePatientActivity.class));
        }
    }

    private void writeNewUser(String userId, String type) {
        User user = new User();
        if (type.equals("Clinic")){
            reff.child("users").child(userId).child("username").setValue(username.getText().toString());
            reff.child("users").child(userId).child("email").setValue(email.getText().toString());
            reff.child("users").child(userId).child("ic no").setValue(icNo.getText().toString());
            reff.child("users").child(userId).child("password").setValue(password.getText().toString());
            reff.child("users").child(userId).child("type").setValue("Clinic");
        }
        else {
            reff.child("users").child(userId).child("username").setValue(username.getText().toString());
            reff.child("users").child(userId).child("email").setValue(email.getText().toString());
            reff.child("users").child(userId).child("ic no").setValue(icNo.getText().toString());
            reff.child("users").child(userId).child("password").setValue(password.getText().toString());
            reff.child("users").child(userId).child("type").setValue("Patient");
        }
    }
}
