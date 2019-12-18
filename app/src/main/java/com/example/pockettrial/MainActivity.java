package com.example.pockettrial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import network.pocket.core.model.Wallet;
import network.pocket.eth.PocketEth;

public class MainActivity extends AppCompatActivity {

    Wallet wallet;
    PocketEth pocketEth;
    Context appContext;
    TextView displayKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.appContext = MainActivity.this;
        // Instantiate PocketAion
        List<String> netIds = new ArrayList<>();
        netIds.add(PocketEth.Networks.RINKEBY.getNetID());
        this.pocketEth = new PocketEth(this.appContext,"", netIds,5,50000,"4");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String address = extras.getString("address");
            String privateKey = extras.getString("privateKey");


            displayKey = (TextView)findViewById(R.id.display_key);
            displayKey.setText(privateKey);

            this.wallet = new Wallet(privateKey, address, this.pocketEth.getRinkeby().getNet().toString(), this.pocketEth.getRinkeby().getNetID().toString());

        }
    }
}
