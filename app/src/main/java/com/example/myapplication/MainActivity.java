package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    View[][] cells;
    int darkColor;
    int brightColor;
    int activeColor;
    int notActiveColor;
    boolean isOneMode;
    int brightColorCounter;
    boolean isHelp;
    ImageView congratulation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        darkColor = getResources().getColor(R.color.teal_700);
        brightColor = getResources().getColor(R.color.purple_200);
        activeColor = getResources().getColor(R.color.active);
        notActiveColor = getResources().getColor(R.color.notActive);

        ViewGroup field = findViewById(R.id.field);
        cells = createCells(field, 8, 8);

        Random r = new Random();
        brightColorCounter = 0;
        for(View[] line : cells){
            for(View cell : line){
                if(r.nextBoolean()) {
                    cell.setBackgroundColor(brightColor);
                    brightColorCounter++;
                }else{
                    cell.setBackgroundColor(darkColor);
                }
            }
        }
        findViewById(R.id.one_mode).setBackgroundColor(notActiveColor);
        findViewById(R.id.help).setBackgroundColor(notActiveColor);

        congratulation= new ImageView(getApplicationContext());
        congratulation.setImageResource(R.drawable.win2);
    }
    public void onClickCell(View view){
        String tagPosition = (String)view.getTag();
        int im = tagPosition.charAt(0) - '0';
        int jm = tagPosition.charAt(2) - '0';

        changeColor(cells[im][jm]);
        if(isOneMode){
            return;
        }
        for(int i=0;i<cells.length; i++){
            changeColor(cells[i][jm]);
        }
        for(int j=0;j<cells[0].length; j++){
            changeColor(cells[im][j]);
        }
        if(brightColorCounter==0 || brightColorCounter == cells.length*cells[0].length){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "                                                                                 ", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0,0);
            //LinearLayout layout = (LinearLayout) toast.getView();
           // layout.addView(congratulation,0);
            toast.show();

        }
    }
    private void changeColor(View view){
        ColorDrawable d = (ColorDrawable) view.getBackground();
        if (d.getColor() == brightColor) {
            view.setBackgroundColor(darkColor);
            brightColorCounter--;
        } else {
            view.setBackgroundColor(brightColor);
            brightColorCounter++;
        }
    }
    public void onClickOneMode(View view){
        Button button = (Button) view;
        isOneMode = !isOneMode;
        if(isOneMode){
            button.setBackgroundColor(activeColor);
            button.setText("One mode(Activated)");
        }else{
            button.setBackgroundColor(notActiveColor);
            button.setText("One mode");
        }
    }
    public void onClickHelp(View view){
        isHelp = !isHelp;
        if(isHelp){
            ArrayList<Integer> result = computeSolution();
            for(int x : result){
                cells[x/cells.length][x%cells[0].length].setAlpha((float) 0.3);
            }
        }else{
            for(View[] line : cells){
                for(View cell : line){
                    cell.setAlpha((float) 1);
                }
            }
        }
    }
    private ArrayList<Integer> computeSolution(){
        int[] linesCount = new int[cells.length];
        int[] rowsCount = new int[cells[0].length];
        int[][] cellCount = new int[cells.length][cells[0].length];
        for(int i=0;i<cells.length; i++){
            for(int j=0;j<cells[0].length;j++){
                ColorDrawable d = (ColorDrawable) cells[i][j].getBackground();
                if(d.getColor() == brightColor) {
                    linesCount[i]++;
                    rowsCount[j]++;
                    cellCount[i][j]=1;
                }
            }
        }
        ArrayList<Integer> result = new ArrayList<>();
        for(int i=0;i<cells.length; i++){
            for(int j=0;j<cells[0].length;j++) {
                if((linesCount[i] + rowsCount[j] - cellCount[i][j])%2==1){
                    result.add(i*cells[0].length + j);
                }
            }
        }
        return result;
    }
    private View[][] createCells(ViewGroup field, int h, int w){
        View[][] cells = new View[h][w];
        for(int i=0;i<h;i++){
            LinearLayout line = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            line.setOrientation(LinearLayout.HORIZONTAL);
            line.setLayoutParams(params);

            for(int j=0;j<w;j++){
                View cell = new View(this);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params2.weight = 1;
                params2.setMargins(5,5,5,5);
                cell.setLayoutParams(params2);
                cell.setTag(Integer.toString(i) + " " + Integer.toString(j));
                cell.setOnClickListener(this::onClickCell);

                line.addView(cell);
                cells[i][j] = cell;

            }
            field.addView(line);
        }
        return cells;
    }
}