package com.example.pockettrial.smartcontract;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.pockettrial.model.Transaction;
import com.example.pockettrial.util.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigInteger;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.core.errors.PocketError;
import network.pocket.core.model.Wallet;
import network.pocket.eth.EthContract;
import network.pocket.eth.PocketEth;
import network.pocket.eth.exceptions.EthContractException;

import com.example.pockettrial.util.AppConfig;

public class SmartContract {

    PocketEth pocketEth;
    Context appContext;
    Wallet wallet;
    public EthContract ethContract;


    public SmartContract(Context context, Wallet wallet, PocketEth pocketEth){
        this.pocketEth = pocketEth;
        this.appContext = context;
        this.wallet = wallet;
        // Setup AionContract
        String contractAddress = AppConfig.contractAddress;
        String contractABI = AppConfig.contractABI;

        try {
            JSONArray contractABIArray = new JSONArray(contractABI);
            this.ethContract = new EthContract(pocketEth.getRinkeby(),contractAddress,contractABIArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String encode(String ascii){
        // Step-1 - Convert ASCII string to char array
        char[] ch = ascii.toCharArray();

        // Step-2 Iterate over char array and cast each element to Integer.
        StringBuilder builder = new StringBuilder();

        for (char c : ch) {
            int i = (int) c;
            // Step-3 Convert integer value to hex using toHexString() method.
            builder.append(Integer.toHexString(i).toUpperCase());
        }
        return  "0x" + builder.toString();
    }


    public void sendTransaction(Wallet wallet) throws JSONException {

         String recipient = "0x53D8C4d0a0dDD9faC8f5D1ab33E8e1673d9481Da";
         String Send_am = encode("500");
         String Re_am = encode("400");
         String Send_cur=encode("INR");
         String Re_cur = encode("USD");

        ArrayList<Object> functionParams = new ArrayList<>();
        functionParams.add(0,recipient);
        functionParams.add(1,Send_am);
        functionParams.add(2,Re_am);
        functionParams.add(3,Send_cur);
        functionParams.add(4,Re_cur);

        //execute contract function
        try {
            this.ethContract.executeFunction("transact", wallet, functionParams, null, new BigInteger("200000"), new BigInteger("10000000000"), new BigInteger("0"), new Function2<PocketError, String, Unit>() {
                @Override
                public Unit invoke(PocketError pocketError, String result) {
                    if (pocketError != null) {
                        pocketError.printStackTrace();
                    }else{
                        Log.d("txHash", result);
                        Toast.makeText(appContext,result,Toast.LENGTH_LONG);
                    }
                    return null;
                }
            });
        } catch (EthContractException e) {
            e.printStackTrace();
        }

    }
}
