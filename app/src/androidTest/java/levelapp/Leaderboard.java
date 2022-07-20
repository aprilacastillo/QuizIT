package levelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Leaderboard extends AppCompatActivity {
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<LeaderboardModel> list;

    LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1178AF")));

        setTitleColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Leaderboard");






        rootNode = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.leade_list);

        reference = rootNode.getReference("Leaderboard");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Toast.makeText(Leaderboard.this, "No data Found", Toast.LENGTH_SHORT).show();
                }

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    LeaderboardModel leaderboardModel = dataSnapshot.getValue(LeaderboardModel.class);
                    list.add(leaderboardModel);
                }

                Collections.sort(list);


                adapter = new LeaderboardAdapter(Leaderboard.this,list);

                adapter.setHasStableIds(true);
                recyclerView.setAdapter(adapter);


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}