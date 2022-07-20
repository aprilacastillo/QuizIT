package levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText emailSignUp,passSignUp,confirmPass,nameSignup;
    TextView loginLinkText;
    Button signUpBtn;
    private LinearLayout layout;

    ProgressBar pb;

    FirebaseAuth fAuth;
    private DatabaseReference reference,scoreReferrence;
    private FirebaseDatabase root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        emailSignUp = findViewById(R.id.signUp_email_et);
        passSignUp = findViewById(R.id.signUp_password_et);
        nameSignup = findViewById(R.id.signUp_name_et);
        confirmPass = findViewById(R.id.signUp_confrim_password_et);
        pb = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        //hide progress bar at first
        pb.setVisibility(View.GONE);




    }

    public void HaveAnAccount(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void ClickSignUpBtn(View view) {
        String email = emailSignUp.getText().toString();
        String password = passSignUp.getText().toString();
        String name = nameSignup.getText().toString();
        String confirmPassword = confirmPass.getText().toString();
        //show progress bar if sign up is clicked
        pb.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(name) ){
            nameSignup.setError("Username is Required");
            pb.setVisibility(View.GONE);
            return;
        }else if (TextUtils.isEmpty(email) ){
            emailSignUp.setError("Email is Required");
            pb.setVisibility(View.GONE);
            return;
        }else if (TextUtils.isEmpty(password)){
            passSignUp.setError("Password is Required");
            pb.setVisibility(View.GONE);
            return;
        } else if (password.length() < 6){
            passSignUp.setError("Too short Password!");
            pb.setVisibility(View.GONE);
            return;
        }else if (TextUtils.isEmpty(confirmPassword)){
            confirmPass.setError("Please Confirm Password");
            pb.setVisibility(View.GONE);
            return;
        }else if(!password.equals(confirmPassword)){
            passSignUp.setError("Not Matched!");
            confirmPass.setError("Not Matched!");
            pb.setVisibility(View.GONE);
            return;

        }else{


            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String userId = fAuth.getCurrentUser().getUid();
                        FirebaseUser user = fAuth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();

                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                  //  Log.d(TAG, "User profile updated.");
                                }
                            }
                        });

                        //hide progress bar
                        pb.setVisibility(View.GONE);


                        CurrencyLevelModel currencyLevelModel = new CurrencyLevelModel("25",userId,"locked","locked","locked","locked","locked","locked","locked","locked","locked","locked");
                        reference = root.getReference("user/"+userId);
                        reference.child("currency").setValue(currencyLevelModel);

                        ScoreModel scoreModel = new ScoreModel("0","0","0","0","0","0","0","0","0","0","0","0","0","0","0");
                        scoreReferrence = root.getReference("quizData/"+userId);
                        scoreReferrence.child("score").setValue(scoreModel);




                        Toast.makeText(SignUpActivity.this,"Redirecting...",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }else{
                        //hide progress bar
                        pb.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this,"Error Occurred"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }


    }
}