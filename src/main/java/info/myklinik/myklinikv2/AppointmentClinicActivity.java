package info.myklinik.myklinikv2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialAutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AppointmentClinicActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    MaterialAutoCompleteTextView materialAutoCompleteTextView;
    private TextInputEditText dateText, time2;
    CardView q1, q2, q3;
    Animation bottomAnim;
    private TextInputLayout time;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_clinic);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        dateText = findViewById(R.id.date_text);
        reff = FirebaseDatabase.getInstance().getReference();
        time = findViewById(R.id.time);
        time2 = findViewById(R.id.time2);
        q1 = findViewById(R.id.Q1);
        q2 = findViewById(R.id.Q2);
        q3 = findViewById(R.id.Q3);


        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String uid = mFirebaseUser.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        Query userQuery = FirebaseDatabase.getInstance().getReference()
                .child("users").orderByKey().equalTo(uid);

        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                for (DataSnapshot userIDSnapshot : dataSnapshot.getChildren()) {
                    //Get the suggestion by childing the key of the string you want to get.
                    String icno = userIDSnapshot.child("ic no").getValue(String.class);
                    String username = userIDSnapshot.child("username").getValue(String.class);
                    //Add the retrieved string to the list
                    autoComplete.add(icno);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        materialAutoCompleteTextView = (MaterialAutoCompleteTextView) findViewById(R.id.autocom);
        materialAutoCompleteTextView.setAdapter(autoComplete);



        findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    public void Q1_bay(View view) {
        time2.setText("8:00 - 10:00");
    }

    public void Q2_bay(View view) {
        time2.setText("10:00 - 12:00");
    }

    public void Q3_bay(View view) {
        time2.setText("2:00 - 4:00");
    }

    public void Q4_bay(View view) {
        final ArrayAdapter<String> userid = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        final String add1 = time2.getText().toString();
        final String add2 = dateText.getText().toString();
        if (add1.isEmpty()) {
            time2.setError("Please enter Time");
            time2.requestFocus();
        } else if (add2.isEmpty()) {
            dateText.setError("Please enter Date");
            dateText.requestFocus();
        } else if (add2.isEmpty() && add1.isEmpty()) {
            Toast.makeText(AppointmentClinicActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        } else if (!(add2.isEmpty() && add1.isEmpty())) {
            mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            final String uid = mFirebaseUser.getUid();
            Query userQuery = FirebaseDatabase.getInstance().getReference()
                    .child("users").orderByKey().equalTo(uid);
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String namaa = materialAutoCompleteTextView.getText().toString();
                    for (DataSnapshot userIDSnapshot : dataSnapshot.getChildren()) {
                        //Get the suggestion by childing the key of the string you want to get.
                        String username = userIDSnapshot.child("ic no").getValue(String.class);
                        userid.add(username);

                        //Add the retrieved string to the list
                        if (username.equals(namaa)) {
                            String test = userIDSnapshot.getKey();
                            String username3 = dataSnapshot.child(uid).child("username").getValue().toString();
                            //Get the suggestion by childing the key of the string you want to get.
                            reff.child("users").child(test).child(username3).child("Time").setValue(add1);
                            reff.child("users").child(test).child(username3).child("Date").setValue(add2);
                            Intent i = new Intent(AppointmentClinicActivity.this, HomeClinicActivity.class);
                            startActivity(i);


                        } else {

                        }
                    }
                    Toast.makeText(AppointmentClinicActivity.this, "Appointment time and date saved", Toast.LENGTH_SHORT).show();

                    //writeNewUser(uid, type);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    /*private void writeNewUser(String userId, String type) {
        User user = new User();
        if (type.equals("`Doctor`")){
            reff.child("users").child(userId).child("username").setValue(username.getText().toString());
            reff.child("users").child(userId).child("email").setValue(email.getText().toString());
            reff.child("users").child(userId).child("type").setValue("Doctor");
        }
        else {
            reff.child("users").child(userId).child("username").setValue(username.getText().toString());
            reff.child("users").child(userId).child("email").setValue(email.getText().toString());
            reff.child("users").child(userId).child("type").setValue("Patient");
        }
    }*/
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + " / " + dayOfMonth + " / " + year;
        dateText.setText(date);
        time.setVisibility(View.VISIBLE);
        q1.setVisibility(View.VISIBLE);
        q2.setVisibility(View.VISIBLE);
        q3.setVisibility(View.VISIBLE);

        q1.setAnimation(bottomAnim);
        q2.setAnimation(bottomAnim);
        q3.setAnimation(bottomAnim);
        time.setAnimation(bottomAnim);
    }
}
