package info.myklinik.myklinikv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAppointmentPatientActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> arrayList = new ArrayList<>();
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_patient);
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
                        if (usernamept.equals(username)) {
                            if (date != null) {
                                autoComplete.add(clinicname + "   " + date + "   " + time);
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

    }
}
