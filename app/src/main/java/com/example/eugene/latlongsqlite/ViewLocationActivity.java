package com.example.eugene.latlongsqlite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ViewLocationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textViewID;
    private EditText textViewLat;
    private EditText textViewLong;
    private Button buttonPrevious;
    private Button buttonSave;
    private Button buttonNext;
    private Button buttonDelete;
    private Button btnReturn;

    private static final String SELECT_SQL = "SELECT * FROM location";

    private SQLiteDatabase db;

    private Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openDatabase();

        textViewID = (EditText) findViewById(R.id.textViewID);
        textViewLat = (EditText) findViewById(R.id.textViewLat);
        textViewLong = (EditText) findViewById(R.id.textViewLong);

        buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        btnReturn = (Button) findViewById(R.id.btnReturn);

        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();
        showRecords();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    protected void openDatabase() {
        db = openOrCreateDatabase("CabLocationDB", Context.MODE_PRIVATE,null);
    }

    protected void showRecords() {
        String locid = c.getString(0);
        String lat = c.getString(1);
        String lon = c.getString(2);
        textViewID.setText(locid);
        textViewLat.setText(lat);
        textViewLong.setText(lon);
    }

    protected void moveNext(){
        if (!c.isLast())
            c.moveToNext();

        showRecords();
    }

    protected void movePrev(){
        if (!c.isFirst())
            c.moveToPrevious();

        showRecords();
    }

    protected void savelocation(){
        String locid = textViewID.getText().toString().trim();
        String lat = textViewLat.getText().toString().trim();
        String lon = textViewLong.getText().toString().trim();

        String sql = "UPDATE location SET latgps='" +lat + "', longgps='" + lon + "' WHERE locationid=" +locid + ";";

        if (lat.isEmpty() || lon.isEmpty()){
            Toast.makeText(getApplicationContext(), "You cannot save blank values", Toast.LENGTH_LONG).show();
            return;
        }

        db.execSQL(sql);
        Toast.makeText(getApplicationContext(), "Records Updated Successfully", Toast.LENGTH_LONG).show();
        c = db.rawQuery(SELECT_SQL,null);
        c.moveToPosition(Integer.parseInt(locid));
    }

    private void deleteRecord(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this location?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String locid = textViewID.getText().toString().trim();

                        String sql = "DELETE FROM location WHERE locationid=" + locid + ";";
                        db.execSQL(sql);
                        Toast.makeText(getApplicationContext(), "Location Deleted", Toast.LENGTH_LONG).show();
                        c = db.rawQuery(SELECT_SQL, null);
                        c.moveToPosition(Integer.parseInt(locid));
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
        if (id == R.id.action_exit) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonNext){
           moveNext();
        }

        if (v == buttonPrevious){
            movePrev();
        }

        if (v == buttonSave){
            savelocation();
        }

        if (v == btnReturn){
            onBackPressed();
        }

        if (v == buttonDelete){
            deleteRecord();
        }

    }


}
