package info.myklinik.myklinikv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomePatientActivity extends AppCompatActivity {
    TextView type, nama;
    Button btn4;
    ImageView image;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        nama = findViewById(R.id.nama);
        image = findViewById(R.id.image2);
        btn4 = findViewById(R.id.button4);
        type = findViewById(R.id.type);
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
                nama.setText("Welcome " + username + "!");
                type.setText(type2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePatientActivity.this, ProfilePatientActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomePatientActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });
    }

    public void P5_bay(View view) {
        Toast.makeText(this, "You have clicked view appointment", Toast.LENGTH_SHORT).show();
        Intent intToMain = new Intent(HomePatientActivity.this, ViewAppointmentPatientActivity.class);
        startActivity(intToMain);
    }

    public void P7_bay(View view) {
        Intent intToMain = new Intent(HomePatientActivity.this, MapPatientActivity.class);
        startActivity(intToMain);
    }

    public void P6_bay(View view) {
        Toast.makeText(this, "You have clicked Log Out", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(HomePatientActivity.this, LoginActivity.class);
        startActivity(intToMain);
    }
}
