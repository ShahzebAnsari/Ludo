package com.example.ludo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    int sizeOfCycle=52;
    int noPatternPerPlayer=4;

    TextView debug;

    RelativeLayout relativeLayout;
    int[] xCoordinate=new int[]{5,51,98,144,190,237,287,341,394,444,490,536,583,629,675};
    int[] yCoordinate=new int[]{204,250,297,343,389,436,485,539,593,643,689,735,781,828,874};

    int[] xHomePosition=new int[]{0,0,3,3};
    int[] yHomePosition=new int[]{0,3,3,0};
    Pair[] getCoordinate=new Pair[sizeOfCycle];
    Map<String,Integer> initialPosition;
    Map<String, Pair<Integer,Integer>> firstHomePosition;
    Map<String, Pair<Integer,Integer>> toBranch;
    ArrayList<Pattern> ref;
    ArrayList<Player> playerArrayList;
    Queue<Player> Q;
    int diceNo;
    Player currentPlayer;
    boolean playerOrDice=false;//player=true;dice=false;
    boolean extraChance=false;

    void adjust(int position,Pattern curPattern){
        int count=0;
        int size=40;
        for(int i=0;i<ref.size();i++){
            Pattern p=ref.get(i);
            if((p.getInitPosition()+p.getNoOfStep())%sizeOfCycle==position&&p!=curPattern&&p.getState()==curPattern.getState()){
                size=p.getPatternSize()-10;
                p.setPatternSize(size);
                p.getView().setLayoutParams(new RelativeLayout.LayoutParams(size,size));;
                count++;
            }
        }

            curPattern.setSpacing(2 * count);
            curPattern.setPatternSize(size);
            curPattern.getView().setLayoutParams(new RelativeLayout.LayoutParams(size,size));;
            curPattern.getView().setTranslationX(xCoordinate[(int) getCoordinate[(curPattern.getInitPosition() + curPattern.getNoOfStep()) % sizeOfCycle].first] + 2 * count);
            curPattern.getView().setTranslationY(yCoordinate[(int) getCoordinate[(curPattern.getInitPosition() + curPattern.getNoOfStep()) % sizeOfCycle].second] + 2 * count);

    }

    boolean isOnStar(int position){
        if(position%13==0)
            return true;
        if((position-8)%13==0)
            return true;
        return false;
    }

    void rollDice(View view){
        if(playerOrDice==true) return;
        playerOrDice=true;
        diceNo=(int)(Math.random()*6)+1;
        int x;
        switch (diceNo){
            case 1: x=R.drawable.one_dice;break;
            case 2: x=R.drawable.two_dice;break;
            case 3:x=R.drawable.three_dice;break;
            case 4:x=R.drawable.four_dice;break;
            case 5:x=R.drawable.five_dice;break;
            case 6:x=R.drawable.six_dice;break;
            default: x=R.drawable.one_dice;
        }
        view.setBackgroundResource(x);
        queueCall();
        //Toast.makeText(this,Q.size() + currentPlayer.color(), Toast.LENGTH_SHORT).show();
        if(diceNo==6) {
            extraChance=true;
        }

        //Toast.makeText(this, currentPlayer.color(), Toast.LENGTH_SHORT).show();
        if(!currentPlayer.canPlay(diceNo))
        {
            playerOrDice=false;
            queueSet();
            int a;
            switch(Q.peek().color()){
                case "green": a=R.color.green;break;
                case "yellow":a=R.color.yellow;break;
                case "blue":a=R.color.blue;break;
                case "red":a=R.color.red;break;
                default: a=R.color.green;
            }
            debug.setBackgroundColor(getResources().getColor(a));
            debug.setText(Q.peek().color());
        }

    }
    void queueCall(){
        if(Q.isEmpty()) return;
        currentPlayer=Q.peek();
        //currentPlayer play;
    }
    void queueSet()
    {
        //debug.setText(debug.getText()+" "+extraChance);
        if(extraChance==false){
            Q.remove();
            if(!currentPlayer.isWon()){
                Q.add(currentPlayer);
            }
        }
        extraChance=false;

    }
    void make_unlock(Pattern P){
        P.setState(2);
    }

    void play_unlock(Pattern P){
        P.setNoOfStep(diceNo);
    }
    void play_after_turn(Pattern P){P.play_after_turn(diceNo);}

    View createView(final String color, int x, int y)
    {
        final View view=new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(40,40));
        view.setTranslationX(xCoordinate[x]);
        view.setTranslationY(yCoordinate[y]);
        int a=0;
        switch(color)
        {
            case "green": a=R.drawable.greenpattern;break;
            case "yellow": a=R.drawable.yellowpattern;break;
            case "blue": a=R.drawable.bluepattern;break;
            case "red": a=R.drawable.redpattern;
        }
        view.setBackgroundResource(a);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayer==null) return;
                for(int i=0;i<ref.size();i++){
                    if(v==ref.get(i).getView()){
                        if(ref.get(i).getColor()==currentPlayer.color()){
                            if(ref.get(i).getState()==3&&ref.get(i).getNoOfStep()+diceNo>sizeOfCycle+4) { return;}
                            if(ref.get(i).getState()==1&&diceNo!=6) {return;}
                            if(ref.get(i).getState()==4) {return;}
                            if(playerOrDice==false){return;}
                            playerOrDice=false;
                            switch (ref.get(i).getState()) {
                                case 1:
                                    if (diceNo == 6) {
                                        currentPlayer.setState(2);
                                        make_unlock(ref.get(i));
                                    }
                                    break;
                                case 2:
                                    play_unlock(ref.get(i));
                                    break;
                                case 3:
                                    play_after_turn(ref.get(i));
                            }
                            //debug.setText(currentPlayer.color()+" "+Integer.toString(currentPlayer.getState())+" "+extraChance);
                            adjust((ref.get(i).getNoOfStep()+ref.get(i).getInitPosition())%sizeOfCycle,ref.get(i));
                            queueSet();
                            int a;
                            switch(Q.peek().color()){
                                case "green": a=R.color.green;break;
                                case "yellow":a=R.color.yellow;break;
                                case "blue":a=R.color.blue;break;
                                case "red":a=R.color.red;break;
                                default: a=R.color.green;
                            }
                            debug.setBackgroundColor(getResources().getColor(a));
                            debug.setText(Q.peek().color());
                        }
                    }
                }
            }
        });
        relativeLayout.addView(view);
        return view;
    }

    public class Pattern{
        private String color;
        private int state;//lock=1; unlock=2; after_turn=3; win=4;
        private boolean isStar;//
        private int noOfStep;
        private int initPosition;
        private int patternNo;
        private View view;
        private int patternSize;
        private int spacing;
        Pattern(String color,int i){
            this.color=color;
            state=1;
            this.initPosition=initialPosition.get(color);
            patternNo=i;
            view=createView(color,firstHomePosition.get(color).first+xHomePosition[i],firstHomePosition.get(color).second+yHomePosition[i]);
            ref.add(this);
            patternSize=40;
            spacing=0;
        }
        View getView(){return view;}
        String getColor(){return color;}
        int getState(){return state;}
        void setState(int state){
            if(state==2&&this.state==1) {
                this.state=state;
                initPosition = initialPosition.get(color);
                view.setTranslationX(xCoordinate[(int)getCoordinate[initPosition].first]);
                view.setTranslationY(yCoordinate[(int)getCoordinate[initPosition].second]);
                noOfStep=0;
                isStar=true;
            }
            if(state==1)
            {
                this.state=1;
                noOfStep=0;
                view.setTranslationX(xCoordinate[firstHomePosition.get(color).first+xHomePosition[patternNo]]);
                view.setTranslationY(yCoordinate[firstHomePosition.get(color).second+yHomePosition[patternNo]]);
                for(int i=0;i<playerArrayList.size();i++)
                {
                    Player tempPlayer=playerArrayList.get(i);
                    if(tempPlayer.color()==this.color){
                        tempPlayer.setState(1);
                    }
                }
            }
        }
        void setNoOfStep(int diceNo){
            if(noOfStep+diceNo>sizeOfCycle-2){
                state=3;
                play_after_turn(diceNo);
                view.setTranslationX(xCoordinate[(int) getCoordinate[(initPosition + noOfStep) % sizeOfCycle].first+toBranch.get(color).first]);
                view.setTranslationY(yCoordinate[(int) getCoordinate[(initPosition + noOfStep) % sizeOfCycle].second+toBranch.get(color).second]);
                return;
            }
            noOfStep+=diceNo;
                view.setTranslationX(xCoordinate[(int) getCoordinate[(initPosition + noOfStep) % sizeOfCycle].first]);
                view.setTranslationY(yCoordinate[(int) getCoordinate[(initPosition + noOfStep) % sizeOfCycle].second]);
            isStar=isOnStar(noOfStep+initPosition);
            for(int i=0;i<ref.size();i++)
            {
                Pattern p=ref.get(i);
                int position=(p.getNoOfStep()+p.getInitPosition())%sizeOfCycle;

                if(p.getState()==2&&!isOnStar(position)&&p.getColor()!=this.color&&position==(this.noOfStep+this.initPosition)%sizeOfCycle){
                    p.setState(1);
                    extraChance=true;
                }
            }
        }
        void play_after_turn(int diceNo){
            if(noOfStep+diceNo>sizeOfCycle+4){
                return;
            }
            noOfStep+=diceNo;
            view.setTranslationX(xCoordinate[(int) getCoordinate[(initPosition + noOfStep) % sizeOfCycle].first+toBranch.get(color).first]);
            view.setTranslationY(yCoordinate[(int) getCoordinate[(initPosition + noOfStep) % sizeOfCycle].second+toBranch.get(color).second]);
            if(noOfStep==sizeOfCycle+4) {
                state = 4;
                extraChance=true;
                currentPlayer.setState(1);
                currentPlayer.setState(3);
            }
        }
        int getNoOfStep(){
            return noOfStep;
        }
        int getInitPosition() {return initPosition;}
        int getPatternSize(){return patternSize;}
        int getSpacing(){return spacing;}
        void setPatternSize(int patternSize){
            this.patternSize=patternSize;
        }
        void setSpacing(int spacing) {
            this.spacing=spacing;
        }

    }

    class Player{
        private String color;
        private Pattern[] patterns;
        private int state;//lock=1; normal=2; win=3;
        Player(String color){
            this.color=color;
            state=1;
            patterns=new Pattern[noPatternPerPlayer];
            for(int i=0;i<noPatternPerPlayer;i++)
            {
                Pattern temp=new Pattern(color,i);

                patterns[i]=temp;
            }
        }
        boolean isWon(){
            if(state==3) return true;
            return false;
        }
        String color(){
            return color;
        }
        int getState(){return state;}
        void setState(int state)
        {
            int[] stateHash=new int[]{0,0,0,0};
            for(int i=0;i<noPatternPerPlayer;i++) {
                stateHash[patterns[i].getState()-1]++;
            }
            if(state==1){
                if(stateHash[0]>0&&stateHash[1]==0&&stateHash[2]==0){
                    this.state=state;
                }
            }
            if(state==2){
                if(this.state!=3&&diceNo==6)
                this.state=state;
            }
            if(state==3){
                if(stateHash[3]==4){
                    this.state=state;
                    extraChance=false;
                    if(Q.size()==2) {
                        //Game over
                    }
                }
            }
        }
        boolean canPlay(int diceNo){
            if(state==1&&diceNo!=6)
            {
                return false;
            }
            for(int i=0;i<noPatternPerPlayer;i++)
            {
                Pattern temp=patterns[i];
                if(temp.getState()==2){
                    return true;
                }
                if(temp.getState()==3&&temp.getNoOfStep()+diceNo<=sizeOfCycle+4){
                    return true;
                }
                if(temp.getState()==1&&diceNo==6){
                    return true;
                }
            }
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);
        debug=(TextView)findViewById(R.id.debug);
        ref = new ArrayList<>();
        playerArrayList=new ArrayList<>();
        Q = new LinkedList<>();
        initialPosition=new HashMap<String, Integer>();
        initialPosition.put("green",0);
        initialPosition.put("yellow",13);
        initialPosition.put("blue",26);
        initialPosition.put("red",39);
        firstHomePosition=new HashMap<String, Pair<Integer, Integer>>();
        firstHomePosition.put("green",new Pair<Integer, Integer>(10,1));
        firstHomePosition.put("yellow",new Pair<Integer, Integer>(10,10));
        firstHomePosition.put("blue",new Pair<Integer, Integer>(1,10));
        firstHomePosition.put("red",new Pair<Integer, Integer>(1,1));
        toBranch=new HashMap<String ,Pair<Integer, Integer>>();
        toBranch.put("green",new Pair<Integer, Integer>(-1,1));
        toBranch.put("yellow",new Pair<Integer, Integer>(-1,-1));
        toBranch.put("blue",new Pair<Integer, Integer>(1,-1));
        toBranch.put("red",new Pair<Integer, Integer>(1,1));
        Player player=new Player("green");
        Player player1=new Player("blue");
        playerArrayList.add(player);
        playerArrayList.add(player1);
        Q.add(player);
        debug.setBackgroundColor(getResources().getColor(R.color.green));
        debug.setText("green");
        Q.add(player1);
        //queueCall();
        int tempX=8;
        int tempY=6;
        int dir=3;//down=0;left=1;up=2;right=3;
        int tempPosition=4;
        int I[]={0,-1,0,1};
        int J[]={1,0,-1,0};
        for(int i=0;i<4;i++){
            for(int j=0;j<6;j++)
            {
                tempX+=I[dir];
                tempY+=J[dir];
                tempPosition=(tempPosition+1)%sizeOfCycle;
                getCoordinate[tempPosition]=new Pair(tempX,tempY);

            }
            dir=(dir+1)%4;
            for(int j=0;j<2;j++)
            {
                tempX+=I[dir];
                tempY+=J[dir];
                tempPosition=(tempPosition+1)%sizeOfCycle;
                getCoordinate[tempPosition]=new Pair(tempX,tempY);
            }
            dir=(dir+1)%4;
            for(int j=0;j<5;j++)
            {
                tempX+=I[dir];
                tempY+=J[dir];
                tempPosition=(tempPosition+1)%sizeOfCycle;
                getCoordinate[tempPosition]=new Pair(tempX,tempY);

            }
            tempX+=I[dir];
            tempY+=J[dir];
            dir=(dir-1+4)%4;
        }
    }
}
