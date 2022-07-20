package levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class QuizContainer extends AppCompatActivity {
    ArrayList<Modelclass> list;

    FirebaseAuth fAuth;

    FirebaseDatabase db;
    DatabaseReference databaseReference,coinsReference,scoreReferrence;

    CountDownTimer countDownTimer;
    int timerValue=20;
    RoundedHorizontalProgressBar progressBar;

    List<Modelclass> allQuestionsList;
    Modelclass modelClass;
    int index=0;
    int coinIndex = 0;
    TextView card_question,optiona,optionb,optionc,optiond;
    CardView cardOA,cardOB,cardOC,cardOD;
    int correctCount= 0;
    int wrongCount= 0;
    LinearLayout nxtBtn;

    ProgressBar loading;

    CurrencyLevelModel myCoins;
    public static int mCoin = 0, mLevelScore = 0;

    int totalCoins = 0;
    String path = "";
    int oldScore = 0;
    
    TextView nxt;

    //declare mediaplayer object
    MediaPlayer correctSfx,wrongSfx;
    public static MediaPlayer bg_music;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_container);
        getSupportActionBar().hide();
        bg_music = MediaPlayer.create(this, R.raw.bg_music_category);
        bg_music.start();
        handler = new Handler();
//getintent
        Intent getPath = getIntent();
         path = getPath.getStringExtra("PATH");
//variable path now has the value of our databsae path or route

        list=new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();




        db = FirebaseDatabase.getInstance();

 //we are saying here that go to this path
 //example if the path variable above has retrieved WebEasy
 //then our database reference would look like this
 // databaseReference = db.getReference("WebEasy")

        databaseReference = db.getReference(path);
        
        nxt = findViewById(R.id.next);


        progressBar=findViewById(R.id.quiz_timer);
        card_question=findViewById(R.id.card_question);
        optiona=findViewById(R.id.card_optiona);
        optionb=findViewById(R.id.card_optionb);
        optionc=findViewById(R.id.card_optionc);
        optiond=findViewById(R.id.card_optiond);

        cardOA=findViewById(R.id.cardOA);
        cardOB=findViewById(R.id.cardOB);
        cardOC=findViewById(R.id.cardOC);
        cardOD=findViewById(R.id.cardOD);

        optiona.setEnabled(false);
        optionb.setEnabled(false);
        optionc.setEnabled(false);
        optiond.setEnabled(false);

        cardOA.setEnabled(false);
        cardOB.setEnabled(false);
        cardOC.setEnabled(false);
        cardOD.setEnabled(false);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        nxtBtn=findViewById(R.id.nxtBtn);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Modelclass> dataList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {

                    dataList.add(dataSnapshot.getValue(Modelclass.class));


                }

              //  Toast.makeText(QuizContainer.this, String.valueOf(dataList.size()), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                optiona.setEnabled(true);
                optionb.setEnabled(true);
                optionc.setEnabled(true);
                optiond.setEnabled(true);

                cardOA.setEnabled(true);
                cardOB.setEnabled(true);
                cardOC.setEnabled(true);
                cardOD.setEnabled(true);



                sendlist(dataList);


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });



      //  Hooks();



        cardOA.setBackgroundColor(getResources().getColor(R.color.white));
        cardOB.setBackgroundColor(getResources().getColor(R.color.white));
        cardOC.setBackgroundColor(getResources().getColor(R.color.white));
        cardOD.setBackgroundColor(getResources().getColor(R.color.white));

        nxtBtn.setClickable(false);
        nxtBtn.setEnabled(false);

        countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerValue = timerValue-1;
                progressBar.setProgress(timerValue);
            }

            @Override
            public void onFinish() {
                Dialog dialog = new Dialog(QuizContainer.this);
                dialog.setContentView(R.layout.time_out_dialog);

                dialog.findViewById(R.id.btn_tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QuizContainer.this,QuizCategory.class);
                        startActivity(intent);
                    }
                });

                dialog.show();
            }
        }.start();








    }

    private void sendlist(ArrayList<Modelclass> dataLists) {
        allQuestionsList = dataLists;
        Collections.shuffle(allQuestionsList);
        modelClass = dataLists.get(index);
        for(int i = 0; i < dataLists.size(); i++){
            list.add(dataLists.get(i));
        }

        setAllData();


    }

    private void setAllData() {
        nxtBtn.setEnabled(false);
        nxtBtn.setClickable(false);
        card_question.setText(modelClass.getQuestion());
        optiona.setText(modelClass.getoA());
        optionb.setText(modelClass.getoB());
        optionc.setText(modelClass.getoC());
        optiond.setText(modelClass.getoD());
        timerValue = 20;
        countDownTimer.cancel();
        countDownTimer.start();
    }

    public void Correct(CardView cardView){

        cardView.setBackgroundColor(getResources().getColor(R.color.green));
        correctSfx = MediaPlayer.create(this, R.raw.right_sound);
        correctSfx.start();

        Dialog dialog = new Dialog(QuizContainer.this);
        dialog.setContentView(R.layout.check_dialog);
        dialog.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.dismiss();

            }
        }, 1500);

        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctCount++;
                index++;
                if(index != 10){
                    modelClass=list.get(index);
                }else {
                    GameWon();
                }

                resetColor();
                setAllData();
                enableButton();
            }
        });


    }

    public void Wrong(CardView cardOA){

        cardOA.setBackgroundColor(getResources().getColor(R.color.red));
        wrongSfx = MediaPlayer.create(this, R.raw.wrong_sound);
        wrongSfx.start();

        Dialog dialog = new Dialog(QuizContainer.this);
        dialog.setContentView(R.layout.wrong_dialog);
        dialog.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.dismiss();

            }
        }, 1500);

        nxtBtn.setOnClickListener(v -> {
            wrongCount++;
            index++;

            if(index != 10){
                modelClass=list.get(index);
            }else {
                GameWon();
            }

            resetColor();
            setAllData();
            enableButton();


              /*  if (index < list.size()){

                    modelClass = list.get(index);
                    resetColor();
                    setAllData();
                    enableButton();
                }else {
                    GameWon();
                }*/


        });


    }

    private void GameWon() {
        QuizContainer.bg_music.stop();
        String userId = fAuth.getCurrentUser().getUid();

        coinsReference = db.getReference("user/"+userId);
        scoreReferrence = db.getReference("quizData/"+userId);

        String route = Character.toLowerCase(path.charAt(0)) +
                (path.length() > 1 ? path.substring(1) : "");

        scoreReferrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    oldScore = Integer.parseInt(dataSnapshot.child(route).getValue().toString());
                }
                gotoCoins(oldScore,correctCount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void gotoCoins(int oldScores, int correctCounts) {
        String userId = fAuth.getCurrentUser().getUid();

        coinsReference = db.getReference("user/"+userId);

        coinsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<CurrencyLevelModel> coinsCount = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    coinsCount.add(dataSnapshot.getValue(CurrencyLevelModel.class));
                }
                myCoins = coinsCount.get(coinIndex);
                mCoin = Integer.parseInt(myCoins.getCoins());
                gotoCalculateCoins(mCoin,oldScores,correctCounts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void gotoCalculateCoins(int mCoins, int oldScores, int correctCounts) {
        String userId = fAuth.getCurrentUser().getUid();

        if (correctCounts > oldScores ){
            scoreReferrence = db.getReference("quizData/"+userId+"/score");
            String route = Character.toLowerCase(path.charAt(0)) +
                    (path.length() > 1 ? path.substring(1) : "");
            scoreReferrence.child(route).setValue(correctCounts);
            totalCoins = correctCount+mCoins;
        }else{
            totalCoins = mCoins + 0;
        }

        HashMap<String,Object> update = new HashMap<>();
        update.put("coins", String.valueOf(totalCoins));
        coinsReference.child("currency").updateChildren(update);

        Intent intent = new Intent(QuizContainer.this,WonActivity.class);
        intent.putExtra("Correct",correctCount);
        intent.putExtra("Wrong",wrongCount);
        startActivity(intent);
        finish();

        GotoLeaderBoard(String.valueOf(correctCounts),String.valueOf(wrongCount));




    }

    private void GotoLeaderBoard(String correct, String wrong) {

        databaseReference = db.getReference("Leaderboard");
        LeaderboardModel scoreModel = new LeaderboardModel(Global.username,String.valueOf(correct));
        databaseReference.child(Global.username).setValue(scoreModel);
    }


    public void enableButton(){
        cardOA.setClickable(true);
        cardOB.setClickable(true);
        cardOC.setClickable(true);
        cardOD.setClickable(true);
    }

    public void disableButton(){
        cardOA.setClickable(false);
        cardOB.setClickable(false);
        cardOC.setClickable(false);
        cardOD.setClickable(false);
    }

    public void resetColor(){
        cardOA.setBackgroundColor(getResources().getColor(R.color.white));
        cardOB.setBackgroundColor(getResources().getColor(R.color.white));
        cardOC.setBackgroundColor(getResources().getColor(R.color.white));
        cardOD.setBackgroundColor(getResources().getColor(R.color.white));
    }



    public void OptionAClick(View view){
        disableButton();
        countDownTimer.cancel();
        nxtBtn.setClickable(true);
        nxtBtn.setEnabled(true);
        if(modelClass.getoA().equals(modelClass.getAns())){
            cardOA.setCardBackgroundColor(getResources().getColor(R.color.green));
            
            if (index == 9){
                nxt.setText("Finish");
            }

            if(index < list.size()){
                Correct(cardOA);
            }else {
                GameWon();
            }
        }else {
            Wrong(cardOA);
        }
    }

    public void OptionBClick(View view){
        disableButton();
        countDownTimer.cancel();
        nxtBtn.setClickable(true);
        nxtBtn.setEnabled(true);
        if(modelClass.getoB().equals(modelClass.getAns())){
            cardOB.setCardBackgroundColor(getResources().getColor(R.color.green));

            if (index == 9){
                nxt.setText("Finish");
            }

            if(index < list.size()){
                Correct(cardOB);
            }else {
                GameWon();
            }
        }else {
            Wrong(cardOB);
        }
    }

    public void OptionCClick(View view){
        disableButton();
        countDownTimer.cancel();
        nxtBtn.setClickable(true);
        nxtBtn.setEnabled(true);
        if(modelClass.getoC().equals(modelClass.getAns())){
            cardOC.setCardBackgroundColor(getResources().getColor(R.color.green));

            if (index == 9){
                nxt.setText("Finish");
            }

            if(index < list.size()){
                Correct(cardOC);
            }else {
                GameWon();
            }
        }else {
            Wrong(cardOC);
        }
    }

    public void OptionDClick(View view){
        disableButton();
        countDownTimer.cancel();
        nxtBtn.setClickable(true);
        nxtBtn.setEnabled(true);
        if(modelClass.getoD().equals(modelClass.getAns())){
            cardOD.setCardBackgroundColor(getResources().getColor(R.color.green));

            if (index == 9){
                nxt.setText("Finish");
            }

            if(index < list.size()){
                Correct(cardOD);
            }else {
                GameWon();
            }
        }else {
            Wrong(cardOD);
        }
    }

    private void Hooks() {


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(QuizContainer.this, QuizCategory.class);
        startActivity(intent);
        finish();

    }

    public void back(View view) {
        Intent intent = new Intent(QuizContainer.this, QuizCategory.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
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
}