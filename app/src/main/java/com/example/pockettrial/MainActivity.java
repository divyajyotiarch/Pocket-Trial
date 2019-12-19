package com.example.pockettrial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pockettrial.smartcontract.SmartContract;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import network.pocket.core.model.Wallet;
import network.pocket.eth.EthContract;
import network.pocket.eth.PocketEth;

public class MainActivity extends AppCompatActivity {

    Wallet wallet;
    PocketEth pocketEth;
    Context appContext;
    TextView displayKey;
    SmartContract smartContract;
    public EthContract ethContract;

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
        this.smartContract = new SmartContract(this.appContext, this.wallet, this.pocketEth);
        this.ethContract = smartContract.ethContract;
        //send transaction

    }
    public void toastAsync(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }
    public void sendTxn(View view){

        try {
            smartContract.sendTransaction(this.wallet);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
