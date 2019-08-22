package com.example.ludo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    CheckBox greenBox;
    CheckBox yellowBox;
    CheckBox blueBox;
    CheckBox redBox;
    void play(View view){
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
        greenBox=(CheckBox)findViewById(R.id.green);
        yellowBox=(CheckBox)findViewById(R.id.yellow);
        blueBox=(CheckBox)findViewById(R.id.blue);
        redBox=(CheckBox)findViewById(R.id.red);

    }
}
