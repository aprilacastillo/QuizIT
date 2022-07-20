package levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText resetMail;
    FirebaseAuth fAuth;
    TextView resetNotif;
    ImageView check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();

        resetNotif = findViewById(R.id.resetToast);
        check = findViewById(R.id.img_check);

        check.setVisibility(View.INVISIBLE);

        resetMail = findViewById(R.id.forgot_password_email_et);
        fAuth = FirebaseAuth.getInstance();
    }

    public void ClickSend(View view) {

        //extract emailand sent rest link
        String mail = resetMail.getText().toString();
        if (mail.isEmpty()){
            resetMail.setError("Required");
        }else{

            fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    resetNotif.setText("Reset Link Has been sent.");
                    resetNotif.setBackgroundColor(Color.parseColor("#64b5f6"));
                    check.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ForgotPasswordActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
}