package info.myklinik.myklinikv2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeClinicActivity extends AppCompatActivity {
    TextView nama, type;
    ImageView imageView, image;
    Button btn4;

    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_clinic);
        nama = findViewById(R.id.nama);
        image = findViewById(R.id.image2);
        btn4 = findViewById(R.id.button4);
        imageView = findViewById(R.id.imageView);
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
                Intent intent = new Intent(HomeClinicActivity.this, ProfileDoctorActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeClinicActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

    }



    public void P3_bay(View view) {
        Intent tent = new Intent(HomeClinicActivity.this, AppointmentClinicActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(imageView, "logo_image");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeClinicActivity.this, pairs);
        startActivity(tent, options.toBundle());
    }

    public void P4_bay(View view) {
        Intent tent = new Intent(HomeClinicActivity.this, ViewAppointmentClinicActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(imageView, "logo_image");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeClinicActivity.this, pairs);
        startActivity(tent, options.toBundle());
    }

    public void P6_bay(View view) {
        Toast.makeText(this, "You have clicked Log Out", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(HomeClinicActivity.this, LoginActivity.class);
        startActivity(intToMain);
    }

}
