package com.example.pockettrial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pockettrial.smartcontract.SmartContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.core.Pocket;
import network.pocket.core.errors.PocketError;
import network.pocket.core.model.Wallet;
import network.pocket.eth.EthContract;
import network.pocket.eth.PocketEth;
import network.pocket.eth.network.EthNetwork;
import network.pocket.eth.rpc.types.BlockTag;

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
           // String address = extras.getString("address");
            String address = "";
            String privateKey = "";
          //  String privateKey = extras.getString("privateKey");


            displayKey = (TextView)findViewById(R.id.display_key);
            displayKey.setText(address);

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
    /*   pocketEth.getRinkeby().getEth().getBalance(wallet.getAddress(),null, new Function2<PocketError, BigInteger, Unit>() {
            @Override
            public Unit invoke(PocketError pocketError, BigInteger bigInteger) {

                if (bigInteger != null) {
                   toastAsync(bigInteger.toString());
                } else {
                   toastAsync("Couldn't fetch balance at this moment");
                }
                return null;
            }
        });*/


        try {

               smartContract.Transact();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
