package com.example.grkmaltunay.loginscreen;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Main3Activity extends Main2Activity {
    public Button registerNewUser;
    public int x;
    public EditText nameTxt, passwordTxt, mailTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        registerNewUser = (Button)findViewById(R.id.reggo);
        nameTxt = (EditText)findViewById(R.id.userName);
        passwordTxt = (EditText)findViewById(R.id.passWord);
        mailTxt = (EditText)findViewById(R.id.mail);


    }

    protected void sendEmail() {
        Log.i("Send email", "");
        Random rand = new Random();
        x = rand.nextInt(1000000);

        String[] TO = {"gorkem444altunay@hotmail.com"};
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Confirmation Code");
        emailIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(x));

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Main3Activity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void write(View v){

        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String name = nameTxt.getText().toString();
            String password = passwordTxt.getText().toString();
            String mail = mailTxt.getText().toString();
            //String input = " insert into student (name, surname, mail) values ('" + name + "','" + password + "')";
            String input = " insert into student12 (name, surname, mail) values ('" + name + "','" + password + "','" + mail + "')";
            database.execSQL(input);
            Toast.makeText(this, "one row is inserted", Toast.LENGTH_LONG).show();
            database.close();
        }
        catch (SQLException e){

        }

    } // end of write

    public void read(View v){

        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        String readData= " select * from student12";
        Cursor cursor= database.rawQuery(readData,null);
        ArrayList<String> tabaleData= new ArrayList<>();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,tabaleData);
        while (cursor.moveToNext()){

            String name= cursor.getString(cursor.getColumnIndex("name"));
            String lastName= cursor.getString(cursor.getColumnIndex("surname"));
            String mail = cursor.getString(cursor.getColumnIndex("mail"));
            String result= name+"  "+lastName + " " + mail;
            tabaleData.add(result);
            System.out.println(tabaleData.get(0));

        } // end of while
        System.out.println(tabaleData.get(1));
        database.close();



    } // end of read
    void hue(View v){
        Intent myIntent = new Intent(Main3Activity.this, Main4Activity.class);
        startActivity(myIntent);
    }
}
