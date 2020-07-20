package info.myklinik.myklinikv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;

public class ViewAppointmentClinicActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    ListView listView;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> arrayList = new ArrayList<>();
    FirebaseAuth mFirebaseAuth;
    MaterialAutoCompleteTextView materialAutoCompleteTextView;
    private TextInputEditText dateText, time2;
    CardView q1, q2, q3;
    Animation bottomAnim;
    private TextInputLayout time;
    String test;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_clinic);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final String uid = mFirebaseUser.getUid();
        Query userQuery = FirebaseDatabase.getInstance().getReference()
                .child("users").orderByKey().equalTo(uid);
        listView = (ListView) findViewById(R.id.list1);
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);


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
                                autoComplete.add(usernamept );
                                autoComplete.add("   " + date + "   " + time);

                                arrayList.add(usernamept);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setAdapter(autoComplete);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if( position%2 != 0 ) {
                    view.setClickable(false);
                }else {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    String test2 = selectedItem.toString();
                    Toast.makeText(ViewAppointmentClinicActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), EditAppointmentActivity.class);
                    intent.putExtra("username", test2);
                    startActivity(intent);
                }

            }
        });

    }

    private void displayDialog(String display){
        Dialog d = new Dialog(this);
        d.setTitle("Edit Data");
        d.setContentView(R.layout.dialog_box);

        d.show();
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
