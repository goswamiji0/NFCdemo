package com.rgg.rahulgoswami.nfcdemo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    NfcAdapter mAdapter;
    IntentFilter[] mFilters;
    PendingIntent mPendingIntent;
    String tagID;
    Activity activity;
    void resolveIntent(Intent intent) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        // 1) Parse the intent and get the action that triggered this intent
        String action = intent.getAction();
        // 2) Check if it was triggered by a tag discovered interruption.
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            //  3) Get an instance of the TAG from the NfcAdapter
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] extraID = tagFromIntent.getId();

            StringBuilder sb = new StringBuilder();
            for (byte b : extraID) {
                sb.append(String.format("%02X", b));
            };

            tagID = sb.toString();
            Log.e("nfc ID", tagID);

            Toast.makeText(activity,"NFC TAG IS :"+tagID,Toast.LENGTH_SHORT).show();
        };
    }// End of method


    // Setup a tech list for all NfcF tags
    String[][] mTechLists = new String[][] { new String[] { NfcA.class.getName() } };
    Intent intent = getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity=MainActivity.this;
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef1 = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        //IntentFilter ndef2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mFilters = new IntentFilter[] {
                ndef1,
                //ndef2,
        };

        try {
            ndef1.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mAdapter==null){
            Toast.makeText(MainActivity.this,
                    "nfcAdapter==null, no NFC adapter exists",
                    Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this,
                    "Set Callback(s)",
                    Toast.LENGTH_LONG).show();
        }



        if (getIntent() != null){
            resolveIntent(getIntent());
        }

    }
}
