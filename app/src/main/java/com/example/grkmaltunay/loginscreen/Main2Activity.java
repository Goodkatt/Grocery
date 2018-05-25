package com.example.grkmaltunay.loginscreen;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends MainActivity {
    public TableLayout tl;
    public int [] fruits = {
            R.drawable.apple,
            R.drawable.lemon,
            R.drawable.peach,
            R.drawable.cherry,
            R.drawable.melon,
            R.drawable.strawberry,
            R.drawable.pear
    };
    public String [] fruitNames = {
            "Apple",
            "Lemon",
            "Peach",
            "Cherry",
            "Melon",
            "Strawberry",
            "Pear"
    };
    public TableRow myRow;
    public int amount;
    public TextView tw, decrease, amountOfFruits;
    public Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        amount = 0;

        tl = (TableLayout)findViewById(R.id.tblLayout);

        for(int i = 0; i < fruits.length; i++){
            myRow = new TableRow(this);
            tl.setColumnStretchable(i, true);
            ImageView iw = new ImageView(this);
            tw = new TextView(this);
            decrease = new TextView(this);
            amountOfFruits = new TextView(this);
            TextView increase = new TextView(this);
            add = new Button(this);
            add.setText("Add");
            decrease.setClickable(true);
            decrease.setBackgroundResource(R.drawable.decrease);
            tw.setPadding(20, 150, 20, 20);
            iw.setImageResource(fruits[i]);
            increase.setBackgroundResource(R.drawable.add);
            tw.setText(fruitNames[i]);
            amountOfFruits.setText(String.valueOf(amount));
            myRow.addView(iw);
            myRow.addView(tw);
            myRow.addView(decrease);
            myRow.addView(amountOfFruits);
            myRow.addView(increase);
            myRow.addView(add);
            tl.addView(myRow);
        }
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount++;
                amountOfFruits.setText(String.valueOf(amount));
                tl.notifyAll();
            }
        });

    }

    @Override
    public void onBackPressed() {
        userName.setText("");
        passWord.setText("");
        Intent x = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(x);

    }



}
