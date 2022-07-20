package levelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class QuizCategory extends AppCompatActivity {
    TextView title;
    public CardView easy, medium, hard;
    public static String subPath="";
    public static String mLevel="";
    public static String hLevel ="";

    public static String medium_level = "";
    public static String hard_level = "";
    
    ImageView logo;
    CardView medUnlock, hardUnlock;

    FirebaseAuth fAuth;
    private DatabaseReference reference;
    private FirebaseDatabase root;
    int coin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_category);

        getSupportActionBar().hide();

        fAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();

        String userId = fAuth.getCurrentUser().getUid();

        reference = root.getReference("user/"+userId);


        easy = (CardView) findViewById(R.id.easy_category);
        medium = (CardView) findViewById(R.id.medium_category);
        hard = (CardView) findViewById(R.id.hard_category);
        logo = findViewById(R.id.appLogo);
        title = findViewById(R.id.title);

        medUnlock = (CardView) findViewById(R.id.medium_category_unlock);
        hardUnlock = (CardView) findViewById(R.id.hard_category_unlock);


//get what subject string has been sent
        Intent getData = getIntent();
        String subjectTitle = getData.getStringExtra("TITLE");
         medium_level = getData.getStringExtra("MLEVEL");
         hard_level = getData.getStringExtra("HLEVEL");
        coin = getData.getIntExtra("COIN",0);


//set the subject name as title
        title.setText(subjectTitle);
//identify what subject wi will retrieve in database
        if (subjectTitle.equalsIgnoreCase("Web Development")){
            subPath = "Web";
            mLevel = "webMed";
            hLevel = "webHard";
            logo.setImageResource(R.drawable.webs);
        }else if (subjectTitle.equalsIgnoreCase("Information Management")){
            subPath = "Im";
            mLevel = "imMed";
            hLevel = "imHard";
            logo.setImageResource(R.drawable.im);
        }else if (subjectTitle.equalsIgnoreCase("Database")){
            subPath = "Database";
            mLevel = "databaseMed";
            hLevel = "databaseHard";
            logo.setImageResource(R.drawable.db);
        }else if (subjectTitle.equalsIgnoreCase("Networking")){
            subPath = "Net";
            mLevel = "netMed";
            hLevel = "netHard";
            logo.setImageResource(R.drawable.net);
        }else if (subjectTitle.equalsIgnoreCase("Mobile Development")){
            subPath = "Mobile";
            mLevel = "mobMed";
            hLevel = "mobHard";
            logo.setImageResource(R.drawable.mobs);
        }

        if (medium_level.equalsIgnoreCase("unlocked")){
            medUnlock.setVisibility(View.GONE);
            medium.setVisibility(View.VISIBLE);
        }
        if (hard_level.equalsIgnoreCase("unlocked")){
            hardUnlock.setVisibility(View.GONE);
            hard.setVisibility(View.VISIBLE);
        }





        medUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(QuizCategory.this);
                //set title
                builder.setTitle("Unlock this level");
                //set message
                builder.setMessage("Level: Medium\nCost: 10 Coins");
                //positive yes
                builder.setPositiveButton("unlock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (coin >= 10){
                            medium.setVisibility(View.VISIBLE);
                            medUnlock.setVisibility(View.GONE);

                            String route = Character.toLowerCase(mLevel.charAt(0)) +
                                    (mLevel.length() > 1 ? mLevel.substring(1) : "");

                            coin = coin - 10;

                            HashMap<String,Object> update = new HashMap<>();
                            update.put("coins", String.valueOf(coin));
                            update.put(route,"unlocked");
                            reference.child("currency").updateChildren(update);
                            Toast.makeText(QuizCategory.this, "Level Unlocked", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(QuizCategory.this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //negative no
                builder.setNegativeButton("cancel", (dialog, which) -> {
                    //dismiss dialog
                    dialog.dismiss();
                });
                builder.show();
            }
        });


        hardUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(QuizCategory.this);
                //set title
                builder.setTitle("Unlock this level");
                //set message
                builder.setMessage("Level: Hard\nCost: 15 Coins");
                //positive yes
                builder.setPositiveButton("unlock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (coin >= 15){
                            hard.setVisibility(View.VISIBLE);
                            hardUnlock.setVisibility(View.GONE);

                            String route = Character.toLowerCase(hLevel.charAt(0)) +
                                    (hLevel.length() > 1 ? hLevel.substring(1) : "");

                            coin = coin - 15;

                            HashMap<String,Object> update = new HashMap<>();
                            update.put("coins", String.valueOf(coin));
                            update.put(route,"unlocked");
                            reference.child("currency").updateChildren(update);
                            Toast.makeText(QuizCategory.this, "Level Unlocked", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(QuizCategory.this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //negative no
                builder.setNegativeButton("cancel", (dialog, which) -> {
                    //dismiss dialog
                    dialog.dismiss();
                });
                builder.show();
            }
        });




//identify what level is clicked
//if level is identified we will send database path to the quiz container
//example, if the subPath above is "Web" and easy level is clicked
//then our database path that we will be sending(putExtra) is
//Web + easy , database path now is "WebEasy"

        easy.setOnClickListener(view -> {
            MainActivity.bgmusic.stop();
            Intent intent = new Intent(QuizCategory.this, QuizContainer.class);
            intent.putExtra("PATH",subPath+"Easy");
            startActivity(intent);

        });
        medium.setOnClickListener(view -> {
            MainActivity.bgmusic.stop();
            Intent intent = new Intent(QuizCategory.this, QuizContainer.class);
            intent.putExtra("PATH",subPath+"Med");
            startActivity(intent);

        });
        hard.setOnClickListener(view -> {
            MainActivity.bgmusic.stop();
            Intent intent = new Intent(QuizCategory.this, QuizContainer.class);
            intent.putExtra("PATH",subPath+"Hard");
            startActivity(intent);
        });
    }

    //back handler
    @Override
    public void onBackPressed() {
        onBackPressed();
        //alert dialog
       //Intent intent = new Intent(QuizCategory.this, MainActivity.class);
      // startActivity(intent);
       //finish();
    }



}