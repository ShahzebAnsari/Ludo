package com.example.ludo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    void playAgain(View view){
        Intent i=new Intent(this,Main2Activity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);
        String[] str=new String[]{"FIRST","SECOND","THIRD","LAST"};
        Intent myIntent=getIntent();
        ArrayList<String> playerColor=new ArrayList<>(myIntent.getStringArrayListExtra("playerColor"));
        ArrayList<Integer> winPosition=new ArrayList<>(myIntent.getIntegerArrayListExtra("winPosition"));
        int marginTop=100;
        int position=1;
        TextView[] textView=new TextView[playerColor.size()];
        for(int i=0;i<playerColor.size();i++){
            textView[i]=new TextView(this);
        }
        for(int i=0;i<playerColor.size()-1;i++){
            for(int j=0;j<winPosition.size();j++){
                if(winPosition.get(j)==position){
                        textView[position - 1].setTextSize(20);
                        textView[position - 1].setText(playerColor.get(j).toUpperCase() + " : " + str[position - 1] + " POSITION");
                        textView[position - 1].setTranslationY(marginTop);
                        textView[position - 1].setTextColor(Color.BLUE);
                        relativeLayout.addView(textView[position - 1]);
                        position++;
                        marginTop += 50;
                        break;
                }
            }
        }
        for(int j=0;j<winPosition.size();j++){
            if(winPosition.get(j)==-1){

                    textView[position - 1].setTextSize(20);
                    textView[position - 1].setText(playerColor.get(j).toUpperCase() + " : " + "LAST" + " POSITION");
                    textView[position - 1].setTranslationY(marginTop);
                    textView[position - 1].setTextColor(Color.BLUE);
                    relativeLayout.addView(textView[position - 1]);
                    break;

            }
        }

    }
}
