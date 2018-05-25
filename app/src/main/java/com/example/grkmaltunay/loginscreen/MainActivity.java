package com.example.grkmaltunay.loginscreen;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public EditText userName, passWord;
    public Button confirmLogin, send, newUser;
    public String path;
    public SQLiteDatabase database=null;
    static String MAIL_QUERY, userN, GLOBAL_USERNAME;
    public ArrayList<String> HUE;


    // DATABASE METHODS TAKEN FROM LECTURE EXAMPLE AND WORKSHEET
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);


        userName = (EditText)findViewById(R.id.userNameEditText);
        passWord = (EditText)findViewById(R.id.passwordEditText);
        confirmLogin = (Button)findViewById(R.id.loginConfirmationButton);
        newUser = (Button)findViewById(R.id.newUser);

        ///////////////////////////////////////////////////////////////DATABASE
        File myDbPath= getApplication().getFilesDir();
        path= myDbPath+"/"+"CMPE410";
        MAIL_QUERY = "";
        HUE = new ArrayList<String>();
        try{


            if (!databaseExist()) {

                database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
                Toast.makeText(this, "We have data base", Toast.LENGTH_LONG).show();

                String mytable = "create table student12 ("
                        + " recID integer PRIMARY KEY autoincrement, "
                        + " name text,  "
                        + " surname text,  "
                        + " mail text  );";
                database.execSQL(mytable); // we have table

                Toast.makeText(this, "We have table", Toast.LENGTH_LONG).show();
                String testName = "Bilgi";
                String lastName = "University";
                String mail = "bilgiedutr";

                String input = " insert into student12 (name, surname, mail) values ('" + testName + "','" + lastName + "','" + mail + "')";
                database.execSQL(input);
                database.close();

                //Toast.makeText(this, "We have one row of data", Toast.LENGTH_LONG).show();
            }

            else{
                //Toast.makeText(this, "We have a data base already", Toast.LENGTH_LONG).show();

            }




        }
        catch (SQLException e){

        }
        ////////////////////////////////////////////////////////////////DATABASE


        /*confirmLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userName.getText().toString().equals("grkm") && passWord.getText().toString().equals("123")) {
                    Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(myIntent);

                }
                else{
                    AlertDialog.Builder x = new AlertDialog.Builder(MainActivity.this);
                    x.setTitle("Login Error");
                    x.setMessage("Invalid username or password");
                    x.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userName.setText("");
                            passWord.setText("");
                        }
                    });
                    x.show();
                }


            }
        });*/
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(myIntent);
            }
        });
    }
////////////////////////////////////DATABASE METHODS
    private  boolean databaseExist(){

        File dbFile = new File(path);
        return dbFile.exists();
    }

    // display data base contents

    /*public void read(View v){

        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        String readData= " select * from student1";
        Cursor cursor= database.rawQuery(readData,null);
        ArrayList<String> tabaleData= new ArrayList<>();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,tabaleData);
        while (cursor.moveToNext()){

            String name= cursor.getString(cursor.getColumnIndex("name"));
            String lastName= cursor.getString(cursor.getColumnIndex("surname"));
            String mail = cursor.getString(cursor.getColumnIndex("mail"));
            String result= name+"  "+lastName + " " + mail;
            //String result = name;
            tabaleData.add(result);
            System.out.println(tabaleData.get(0));

        } // end of while
        System.out.println(tabaleData.get(1));
        database.close();



    } // end of read*/
    public void read(View v){
        userN = userName.getText().toString();
        String pw = passWord.getText().toString();
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        String readData= " select * from student12 where name = ? AND surname = ? ";
        Cursor cursor = database.rawQuery(readData, new String[]{userN, pw});

    ///METHOD TO CHECK LOGIN USERNAME AND PW IF MATCH


        if (cursor != null) {
            if (cursor.getCount() > 0) {
                GLOBAL_USERNAME = getEMAIL();
                HUE.add(GLOBAL_USERNAME);

                System.out.println(GLOBAL_USERNAME+"xDDDDDDDDDDDDD");
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(MainActivity.this, Main4Activity.class);
                startActivity(myIntent);

            } else {
                AlertDialog.Builder x = new AlertDialog.Builder(MainActivity.this);
                x.setTitle("Invalid Login");
                x.setMessage("Check Username or Password");
                x.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                x.show();
            }
        }
        database.close();




    } // end of read
    String getEMAIL(){

        String name = "";
        String readData= " select * from student12 where name=?";
        Cursor cursor= database.rawQuery(readData,new String[]{userN});
        while (cursor.moveToNext()){

            name= cursor.getString(cursor.getColumnIndex("mail"));
        }
        return name;
    }//GET EMAIL OF USER WHO LOGGED IN

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu(menu);
        return true;
    }
    public void myMenu(Menu menu){
        int groupId=0;
        // define menu items...
        menu.add(groupId,1,1,"APP INFO");
        menu.add(groupId, 1, 2, "EMPTY ITEM");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return applyMenuOptins(item);
    }
    public boolean applyMenuOptins(MenuItem item){

        int menuItemId= item.getItemId();
        if(menuItemId == 1)
            Toast.makeText(MainActivity.this, "Görkem Altunay" + '\n' +"114200063" + '\n', Toast.LENGTH_LONG).show();
        return true;
    } //

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        myMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return applyMenuOptins(item);
    }
}


