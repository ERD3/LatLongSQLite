package com.example.eugene.latlongsqlite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextLat;
    private EditText editTextLong;
    private Button btnAdd;
    private Button btnView;

    private SQLiteDatabase db;//defines SQLiteDatabase called db

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createDatabase();

        editTextLat = (EditText) findViewById(R.id.editTextLat);
        editTextLong = (EditText) findViewById(R.id.editTextLong);

        btnAdd = (Button) findViewById(R.id.buttonAdd);


        btnAdd.setOnClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    private void showLocation(){
        Intent intent = new Intent(this,ViewLocationActivity.class);
        startActivity(intent);

    }
    private void showMap() {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }

    //Method to create database called CabLocationDB
    protected void createDatabase() {
        db = openOrCreateDatabase("CabLocationDB", Context.MODE_PRIVATE, null);
        //Creates table called location only if it doesn't exists and the following fields (locationid, latgps and longgps)
        db.execSQL("CREATE TABLE IF NOT EXISTS location(locationid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,t REAL, d REAL, latgps REAL, longgps REAL);");    //Method to write to database
    }
    protected void insertIntoDB(){
        String lat = editTextLat.getText().toString().trim();
        String lon = editTextLong.getText().toString().trim();
        if (lat.isEmpty() || lon.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }
        //Toast.makeText(getApplicationContext(),"Lat="+lat+"Long="+lon, Toast.LENGTH_LONG).show();
        String query = "INSERT INTO location (latgps, longgps) VALUES('"+lat+"', '"+lon+"');";
        db.execSQL(query);
        Toast.makeText(getApplicationContext(),"Saved Successfully", Toast.LENGTH_LONG).show();

        textClearField();

    }
    protected void textClearField(){
        editTextLat.setText("");
        editTextLong.setText("");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_db){
            showLocation();
        }
        if (id == R.id.action_map){
            showMap();
        }
        if (id == R.id.action_exit) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            insertIntoDB();
        }

    }

}
