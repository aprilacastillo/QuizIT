package levelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //declaration
    public CardView webdev,im,database,networking,appdev;
    FirebaseDatabase db;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    TextView coins;
    int index=0;
    ImageView leaderb;

    Dialog dialog;

    String errorMessage = "";

    TextView exit, dyk,triviaDisplay,okBtn;

    CurrencyLevelModel myCoins;
    String url,myTrivia;
    int mCoin = 0;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String databaseHard, databaseMed,imMed, imHard,mobHard, mobMed,netHard, netMed,webHard, webMed;
    
    ProgressBar pb,coinLoad;
    public static MediaPlayer bgmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        bgmusic = MediaPlayer.create(this,R.raw.lobby_music);
        bgmusic.start();

        String name = user.getDisplayName();



        //prepare username
        if (TextUtils.isEmpty(name)){
            prepareName();
        }else{
            Global.username = name;
            getSupportActionBar().setTitle(Global.username);
        }


        //instancetiate
        webdev = (CardView) findViewById(R.id.Webdev);
        im = (CardView) findViewById(R.id.Im);
        database = (CardView) findViewById(R.id.Database);
        networking = (CardView) findViewById(R.id.Networking);
        appdev = (CardView) findViewById(R.id.Mobdev);
        coins = findViewById(R.id.currency);
        pb = findViewById(R.id.pb);
        coinLoad = findViewById(R.id.coinLoading);
        leaderb = findViewById(R.id.leaderboardClick);

        coinLoad.setVisibility(View.VISIBLE);

        dialog = new Dialog(this);

        exit = findViewById(R.id.exit);
        dyk = findViewById(R.id.dyk);

        leaderb.setOnClickListener(v->{
            Intent intent = new Intent(this,Leaderboard.class);
            startActivity(intent);

        });




        

        db = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

        String userId = fAuth.getCurrentUser().getUid();
        
        databaseReference = db.getReference("user/"+userId);
        
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<CurrencyLevelModel> coinsLevel = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {

                    coinsLevel.add(dataSnapshot.getValue(CurrencyLevelModel.class));

                }
               /* myCoins = coinsLevel.get(index);
                coinLoad.setVisibility(View.GONE);
                String countCoin = String.valueOf(myCoins.getCoins());
                coins.setText(countCoin);*/
             /*   int temp = Integer.parseInt(countCoin);
                sendCoin(temp);*/

                sendData(coinsLevel);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
       

        webdev.setOnClickListener(this);
        im.setOnClickListener(this);
        database.setOnClickListener(this);
        networking.setOnClickListener(this);
        appdev .setOnClickListener(this);

        dyk.setOnClickListener(view -> {
            pb.setVisibility(View.VISIBLE);
            dyk.setVisibility(View.INVISIBLE);
            url = "https://asli-fun-fact-api.herokuapp.com/";

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                           // Log.e("response here: ",response.toString());

                            try {
                                 myTrivia = response.getJSONObject("data").getString("fact");

                                dialog.setContentView(R.layout.trivia_popup);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                triviaDisplay = dialog.findViewById(R.id.trivia);
                                okBtn = dialog.findViewById(R.id.ok);

                                triviaDisplay.setText(myTrivia);

                                okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        dyk.setVisibility(View.VISIBLE);
                                    }
                                });
                                pb.setVisibility(View.GONE);
                                dialog.show();
                                 
                               // Toast.makeText(MainActivity.this, myTrivia, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                          errorMessage = "No Trivia for the mean time";
                        }
                    }

            );

            requestQueue.add(jsonObjectRequest);


        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //set title
                builder.setTitle("Exit");
                //set message
                builder.setMessage("Are you sure you want to Exit?");
                //positive yes
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish activity
                        finishAffinity();
                        System.exit(0);
                    }
                });
                //negative no
                builder.setNegativeButton("NO", (dialog, which) -> {
                    //dismiss dialog
                    dialog.dismiss();
                });
                builder.show();

            }
        });

    }

    private void prepareName() {
        Toast.makeText(MainActivity.this, "Cannot Retrieve Username", Toast.LENGTH_SHORT).show();
    }

    private void sendData(ArrayList<CurrencyLevelModel> coinsLevels) {
        myCoins = coinsLevels.get(index);
        coinLoad.setVisibility(View.GONE);
        String countCoin = String.valueOf(myCoins.getCoins());
        databaseMed = String.valueOf(myCoins.getDatabaseMed());
        databaseHard = String.valueOf(myCoins.getDatabaseHard());
        webMed = String.valueOf(myCoins.getWebMed());
        webHard = String.valueOf(myCoins.getWebHard());
        netMed = String.valueOf(myCoins.getNetMed());
        netHard = String.valueOf(myCoins.getNetHard());
        imMed = String.valueOf(myCoins.getImMed());
        imHard = String.valueOf(myCoins.getImHard());
        mobMed = String.valueOf(myCoins.getMobMed());
        mobHard = String.valueOf(myCoins.getMobHard());
        coins.setText(countCoin);
        int temp = Integer.parseInt(countCoin);
        mCoin = temp;
    }




    //detect what subject has been clicked
    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){

            case R.id.Webdev:
                i = new Intent(this,QuizCategory.class);
             /*   if subject web dev is clicked then we will
                 send web development string to next next activity*/
                i.putExtra("TITLE","Web Development");
                i.putExtra("MLEVEL", webMed);
                i.putExtra("HLEVEL", webHard);
                i.putExtra("COIN",mCoin);
                startActivity(i);
                break;

            case R.id.Im:
                i = new Intent(this,QuizCategory.class);
            /*   if subject IM is clicked then we will
                 send Information Management string to next next activity*/
                i.putExtra("TITLE","Information Management");
                i.putExtra("MLEVEL", imMed);
                i.putExtra("HLEVEL", imHard);
                i.putExtra("COIN",mCoin);
                startActivity(i);
                break;

            case R.id.Database:
                i = new Intent(this,QuizCategory.class);
             /*   if subject Database is clicked then we will
                 send Database string to next next activity*/
                i.putExtra("TITLE","Database");
                i.putExtra("MLEVEL", databaseMed);
                i.putExtra("HLEVEL", databaseHard);
                i.putExtra("COIN",mCoin);
                startActivity(i);
                break;

            case R.id.Networking:
                i = new Intent(this,QuizCategory.class);
             /*   if subject Networking is clicked then we will
                 send Networking string to next next activity*/
                i.putExtra("TITLE","Networking");
                i.putExtra("MLEVEL", netMed);
                i.putExtra("HLEVEL", netHard);
                i.putExtra("COIN",mCoin);
                startActivity(i);
                break;

            case R.id.Mobdev:
                i = new Intent(this,QuizCategory.class);
             /*   if subject mob dev is clicked then we will
                 send Mobile Development string to next next activity*/
                i.putExtra("TITLE","Mobile Development");
                i.putExtra("MLEVEL", mobMed);
                i.putExtra("HLEVEL", mobHard);
                i.putExtra("COIN",mCoin);
                startActivity(i);
                break;

        }
    }
//back handler
    @Override
    public void onBackPressed() {
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set title
        builder.setTitle("Exit");
        //set message
        builder.setMessage("Are you sure you want to Exit?");
        //positive yes
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish activity
                finishAffinity();
                System.exit(0);
            }
        });
        //negative no
        builder.setNegativeButton("NO", (dialog, which) -> {
            //dismiss dialog
            dialog.dismiss();
        });
        builder.show();
    }

    public void Logout(View view) {
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set title
        builder.setTitle("Logout");
        //set message
        builder.setMessage("Are you sure you want to logout?");
        //positive yes
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish activity
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                MainActivity.bgmusic.stop();
            }
        });
        //negative no
        builder.setNegativeButton("NO", (dialog, which) -> {
            //dismiss dialog
            dialog.dismiss();
        });
        builder.show();
    }

}