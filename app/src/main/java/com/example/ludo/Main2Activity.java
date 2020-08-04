package com.example.ludo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    CheckBox greenBox;
    CheckBox yellowBox;
    CheckBox blueBox;
    CheckBox redBox;
    Button play;
    void play(){
        int count=0;
        ArrayList<String> S=new ArrayList<>();
        if(greenBox.isChecked()){
            S.add("green");
            count++;
        }
        if(yellowBox.isChecked()){
            S.add("yellow");
            count++;
        }
        if(blueBox.isChecked()){
            S.add("blue");
            count++;
        }
        if(redBox.isChecked()){
            S.add("red");
            count++;
        }
        if(count<2) {
            Toast.makeText(this, "Choose at least two player", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(this,MainActivity.class);
        intent.putStringArrayListExtra("S",S);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        greenBox= findViewById(R.id.green);
        yellowBox= findViewById(R.id.yellow);
        blueBox= findViewById(R.id.blue);
        redBox= findViewById(R.id.red);
        play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

    }
}
