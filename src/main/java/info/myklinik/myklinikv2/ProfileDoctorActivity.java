package info.myklinik.myklinikv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileDoctorActivity extends AppCompatActivity {
    ImageView image;
    Button button;
    TextView nama, type, appointnum;
    TextInputEditText username, email, password, icno;
    FirebaseAuth mFirebaseAuth;
    int a = 0;
    int appointnum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_doctor);
        nama = findViewById(R.id.nama);
        username = findViewById(R.id.username);
        button = findViewById(R.id.btn4);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        icno = findViewById(R.id.nomboric);
        type = findViewById(R.id.type);
        appointnum = findViewById(R.id.appointnum);
        image = findViewById(R.id.imageView);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final String uid = mFirebaseUser.getUid();
        Query userQuery = FirebaseDatabase.getInstance().getReference()
                .child("users").orderByKey().equalTo(uid);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String type2 = dataSnapshot.child(uid).child("type").getValue().toString();
                String username = dataSnapshot.child(uid).child("username").getValue().toString();
                nama.setText(username);
                type.setText(type2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child(uid).child("username").getValue(String.class);
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                for (DataSnapshot userIDSnapshot : dataSnapshot.getChildren()) {
                    String usernamept = userIDSnapshot.child("username").getValue(String.class);
                    for (DataSnapshot clinicSnapshot : userIDSnapshot.getChildren()) {
                        //Get the suggestion by childing the key of the string you want to get.
                        String clinicname = clinicSnapshot.getKey();
                        String date = clinicSnapshot.child("Date").getValue(String.class);
                        String time = clinicSnapshot.child("Time").getValue(String.class);

                        //Add the retrieved string to the list
                        if (clinicname.equals(username)) {
                            if (date != null) {
                                appointnum2++;
                              appointnum.setText(String.valueOf(appointnum2));

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username2 = dataSnapshot.child(uid).child("username").getValue(String.class);
                String email2 = dataSnapshot.child(uid).child("email").getValue(String.class);
                String password2 = dataSnapshot.child(uid).child("password").getValue(String.class);
                String icno2 = dataSnapshot.child(uid).child("ic no").getValue(String.class);
                username.setText(username2);
                email.setText(email2);
                password.setText(password2);
                icno.setText(icno2);

                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("users").child(uid).child("username").setValue(username.getText().toString());
                database.child("users").child(uid).child("email").setValue(email.getText().toString());
                database.child("users").child(uid).child("ic no").setValue(icno.getText().toString());
                database.child("users").child(uid).child("password").setValue(password.getText().toString());
                Toast.makeText(ProfileDoctorActivity.this, "Details updated!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileDoctorActivity.this, HomeClinicActivity.class));
                finish();

            }
        });



    }
}
