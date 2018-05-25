package com.example.grkmaltunay.loginscreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Main4Activity extends Main3Activity implements OnMapReadyCallback{
    public Spinner fruitss, VEG_SPINNER;
    public ArrayAdapter fruitArrayAdapter, VEG_ARRAY_ADAPTER;
    public String [] fruitArray = {"APPLE", "PEACH"};
    public String [] vegetableArray = {"TOMATO", "POTATO"};
    public  List<String> ADD_FRUIT = new ArrayList<>();
    public List<String> FRUIT_PRICE = new ArrayList<>();
    public ImageView myIMage, myImageVEGS;
    public int [] pics = {
            R.drawable.apple,
            R.drawable.pear
    };
    public int [] VEG_PICS_ARRAY = {
            R.drawable.tomato,
            R.drawable.potato
    };
    public String [] prices = {"12", "15"};
    public String [] VEG_PRICE_ARRAY = {"13", "20"};
    public TextView priceView;
    public TextView amount, totalPriceFruits, totalPVegetables, amountTXT_VIEW_VEGS, PRICE_TXT_SINGLE_VEG;
    public Button increaseBTN, INCREASE_VEG_BTN, DECREASE_VEG_BTN, PROCEED_TO_MAP;
    public int x, y, flag;
    public TabHost myTabHost;
    public Button add, decreaseBTN, ADD_VEG_TO_CART, EMPTY_CART;
    private GoogleMap mMap;
    private Marker marker;
    private ListView cartList;
    private ArrayList<String> cartArrayList;
    private HashMap<String, Integer>myMap;
    private List<Integer> TOTAL_CART;
    private int TOTAL_PRICE_CART = 0;
    private Button ADD_NEW_FRUIT;
    private String [] newFRUITS = {"BANANA", "MELON", "STRAWBERRY", "WATERMELON", "CHERRY"};
    private String [] newFRUITPRICE = {"20", "30", "40", "50", "60"};
    private int xy = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ADD_FRUIT.add("APPLE");
        ADD_FRUIT.add("PEACH");
        FRUIT_PRICE.add("12");
        FRUIT_PRICE.add("15");
        x = 1;
        y = 1;
        flag = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hosttabs);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        cartArrayList = new ArrayList<String>();
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cartArrayList);

        myIMage = (ImageView)findViewById(R.id.imageView);

        //VEGS
        myImageVEGS = (ImageView)findViewById(R.id.imageViewVEGS);
        amountTXT_VIEW_VEGS = (TextView)findViewById(R.id.amountTxtViewVEGS);
        INCREASE_VEG_BTN = (Button)findViewById(R.id.increaseBTnVEGS);
        DECREASE_VEG_BTN = (Button)findViewById(R.id.decreaseBTNVEGS);
        PRICE_TXT_SINGLE_VEG = (TextView)findViewById(R.id.priceIDVEGS);

        priceView = (TextView)findViewById(R.id.priceID);
        amount = (TextView)findViewById(R.id.amountTxtView);
        increaseBTN = (Button)findViewById(R.id.increaseBTn);
        totalPriceFruits = (TextView)findViewById(R.id.totalPrice);
        add = (Button)findViewById(R.id.addTOCART);
        totalPVegetables = (TextView)findViewById(R.id.totalP);
        cartList = (ListView)findViewById(R.id.cartListView);
        decreaseBTN = (Button)findViewById(R.id.decrease);
        ADD_VEG_TO_CART = (Button)findViewById(R.id.addTOC);
        TOTAL_CART = new ArrayList<Integer>();
        EMPTY_CART = (Button)findViewById(R.id.EMPTY_CART);
        PROCEED_TO_MAP = (Button)findViewById(R.id.proceed);



        myTabHost = (TabHost)findViewById(android.R.id.tabhost);
        myTabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec= myTabHost.newTabSpec("Fruits");
        tabSpec.setContent(R.id.fruitTTAB);
        tabSpec.setIndicator("Fruits",null);
        myTabHost.addTab(tabSpec);

        tabSpec= myTabHost.newTabSpec("Vegetables");
        tabSpec.setContent(R.id.vegetableTab);
        tabSpec.setIndicator("Vegetables",null);
        myTabHost.addTab(tabSpec);

        tabSpec = myTabHost.newTabSpec("CART");
        tabSpec.setContent(R.id.cartLayout);
        tabSpec.setIndicator("CART", null);
        myTabHost.addTab(tabSpec);

        tabSpec = myTabHost.newTabSpec("MAP");
        tabSpec.setContent(R.id.map);
        tabSpec.setIndicator("Map", null);
        myTabHost.addTab(tabSpec);

        tabSpec = myTabHost.newTabSpec("FRUIT");
        tabSpec.setContent(R.id.newFruitTAb);
        tabSpec.setIndicator("FRUIT", null);
        myTabHost.addTab(tabSpec);

        myTabHost.setCurrentTab(0);
        myTabHost.getTabWidget().getChildTabViewAt(3).setClickable(false);
        myTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("CART")){
                        for (int i = 0; i < TOTAL_CART.size(); i++) {
                            TOTAL_PRICE_CART = TOTAL_PRICE_CART + TOTAL_CART.get(i);
                        }
                        cartArrayList.add("TOTAL PRICE:" + " " + String.valueOf(TOTAL_PRICE_CART) + " " + "TL");
                        cartList.setAdapter(adapter);
                }
                if (tabId.equals("FRUIT")) {
                    //ADD_FRUIT.add("BANANA");
                    ADD_FRUIT.add(newFRUITS[xy]);
                    FRUIT_PRICE.add(newFRUITPRICE[xy]);
                    xy++;
                }

            }
        });
        PROCEED_TO_MAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTabHost.setCurrentTab(3);
            }
        });

        fruitss = (Spinner)findViewById(R.id.fruitsSpinner);
        fruitArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ADD_FRUIT);
        fruitss.setAdapter(fruitArrayAdapter);

        fruitss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                x = 1;

                priceView.setText(FRUIT_PRICE.get(i));
                totalPriceFruits.setText(FRUIT_PRICE.get(i));
                amount.setText("1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        VEG_SPINNER = (Spinner)findViewById(R.id.vegSpinner);
        VEG_ARRAY_ADAPTER = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, vegetableArray);
        VEG_SPINNER.setAdapter(VEG_ARRAY_ADAPTER);
        VEG_SPINNER.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                y = 1;
                myImageVEGS.setImageResource(VEG_PICS_ARRAY[position]);
                PRICE_TXT_SINGLE_VEG.setText(VEG_PRICE_ARRAY[position]);
                totalPVegetables.setText(VEG_PRICE_ARRAY[position]);
                amountTXT_VIEW_VEGS.setText("1");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int price = Integer.parseInt(totalPriceFruits.getText().toString());
                TOTAL_CART.add(price);
                //totalPVegetables.setText(totalPriceFruits.getText());
                cartArrayList.add(amount.getText().toString() + " " + "KG" + " " + "X" + " " +fruitss.getSelectedItem().toString() + " " + price + " " + "TL");
                cartList.setAdapter(adapter);
                myTabHost.getTabWidget().getChildTabViewAt(0).setClickable(false);
            }
        });

        ADD_VEG_TO_CART.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(totalPVegetables.getText().toString());
                TOTAL_CART.add(price);
                cartArrayList.add(amountTXT_VIEW_VEGS.getText().toString() + " " + "KG" + " " + "X" + " " + VEG_SPINNER.getSelectedItem().toString() + " " + price + " " + "TL");
                cartList.setAdapter(adapter);
                myTabHost.getTabWidget().getChildTabViewAt(1).setClickable(false);
            }
        });
        EMPTY_CART.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartArrayList.clear();
                cartList.setAdapter(adapter);
                myTabHost.getTabWidget().getChildTabViewAt(0).setClickable(true);
                myTabHost.getTabWidget().getChildTabViewAt(1).setClickable(true);
                TOTAL_CART.clear();
                TOTAL_PRICE_CART = 0;
            }
        });
        increaseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;
                amount.setText(String.valueOf(x));
                totalPriceFruits.setText(String.valueOf(Integer.parseInt(amount.getText().toString()) * Integer.parseInt(priceView.getText().toString())));
            }
        });
        INCREASE_VEG_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                y++;
                amountTXT_VIEW_VEGS.setText(String.valueOf(y));
                totalPVegetables.setText(String.valueOf(Integer.parseInt(amountTXT_VIEW_VEGS.getText().toString()) * Integer.parseInt(PRICE_TXT_SINGLE_VEG.getText().toString())));
            }
        });
        DECREASE_VEG_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                y--;
                if(y > 0){
                    amountTXT_VIEW_VEGS.setText(String.valueOf(y));
                    totalPVegetables.setText(String.valueOf(Integer.parseInt(totalPVegetables.getText().toString()) - Integer.parseInt(PRICE_TXT_SINGLE_VEG.getText().toString())));
                }
                else if(y == 0 || y < 0){
                    y = 0;
                    amountTXT_VIEW_VEGS.setText("1");
                    DECREASE_VEG_BTN.setClickable(false);
                }
            }
        });


    }
    void decrease(View v){
        x--;
        if(x > 0 ) {
            amount.setText(String.valueOf(x));
            totalPriceFruits.setText(String.valueOf(Integer.parseInt(totalPriceFruits.getText().toString()) - Integer.parseInt(priceView.getText().toString())));
        }
        else if (x == 0 || x < 0){
            x = 0;
            amount.setText("1");
            decrease.setClickable(false);
        }

    }
    void increase(View v){
        x++;
        amount.setText(String.valueOf(x));
        totalPriceFruits.setText(String.valueOf(Integer.parseInt(amount.getText().toString()) * Integer.parseInt(priceView.getText().toString())));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        float zoomLevel = 16.0f;
        LatLng sydney = new LatLng(41.066864, 28.946853);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                System.out.println(getCompleteAddressString(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude));
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng arg0) {
                System.out.println(getCompleteAddressString(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude));

                final AlertDialog.Builder x = new AlertDialog.Builder(Main4Activity.this)
                        .setTitle("Adress")
                        .setMessage("Is that your adress?" + '\n'+ '\n'+ getCompleteAddressString(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude))
                        .setCancelable(true)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    sendEmail();
                            }
                        });
                x.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        x.show();

                    }
                }, 1000);

                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(arg0.latitude,
                                        arg0.longitude))
                        .draggable(true).visible(true));
            }
        });

    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("Cur address", strReturnedAddress.toString());
            } else {
                Log.w("My Current  address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current  address", "Canont get Address!");
        }
        return strAdd;
    }
    public void sendEmail() { //EMAIL INTENT TO SEND ORDER DETAILS TO SPECIFIC USER

        Log.i("Send email", "");
        Random rand = new Random();
        x = rand.nextInt(1000000);
        System.out.println(GLOBAL_USERNAME+"HUEHEUHEUHEUHEUHUEHUEHUHE");

        String[] TO = {GLOBAL_USERNAME};


        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order Details:");
        //emailIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(x) + '\n' + "ORDER DETAILS" + '\n' +
        //"");
        String hue = "AHSDGKJAHSDG";
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                Html.fromHtml(new StringBuilder()
                        .append("<p><b>Order Number</b></p>")
                        .append(x)
                        .append("<small><p>Items:</p></small>")
                        .append(getDETAILS())
                        .toString())
        );


        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Main4Activity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    public List<String> getDETAILS(){
        List<String> x = new ArrayList<String>();
        for(int i = 0; i < cartArrayList.size(); i++){

            x.add(cartArrayList.get(i));
            x.add("\n");
        }
        return x;
    }

}
