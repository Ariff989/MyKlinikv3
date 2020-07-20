package info.myklinik.myklinikv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    CardView p1, p2;
    ImageView iv, imageView, imageView2;
    TextView textView, textView2, textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageView = findViewById(R.id.ivdoc);
        imageView2 = findViewById(R.id.ivpt);
        iv = findViewById(R.id.imageView);
        textView = findViewById(R.id.tvdoc);
        textView2 = findViewById(R.id.tvpt);
        textView2 = findViewById(R.id.textView3);
        p1 = findViewById(R.id.cv1);
        p2 = findViewById(R.id.cv2);
    }

    public void P1_bay(View view) {
        Intent tent = new Intent(LoginActivity.this, LoginClinicActivity.class);
        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(imageView, "transclinic");
        pairs[1] = new Pair<View, String>(textView, "namadr");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
        startActivity(tent, options.toBundle());
    }

    public void P2_bay(View view) {
        Intent tent = new Intent(LoginActivity.this, LoginPatientActivity.class);
        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(imageView2, "transpatient");
        pairs[1] = new Pair<View, String>(textView2, "namapt");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
        startActivity(tent, options.toBundle());
    }

    public void P3_bay(View view) {
        Intent tent = new Intent(LoginActivity.this, SignUpActivity.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(iv, "logo_image");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
        startActivity(tent, options.toBundle());
    }
}
