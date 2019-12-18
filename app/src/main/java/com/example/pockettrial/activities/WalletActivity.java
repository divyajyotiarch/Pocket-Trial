package com.example.pockettrial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pockettrial.MainActivity;
import com.example.pockettrial.R;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.core.errors.WalletPersistenceError;
import network.pocket.core.model.Wallet;
import android.widget.Toast;
public class WalletActivity extends AppCompatActivity {

    Wallet wallet;
    Context appContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        // Get app context
        this.appContext = WalletActivity.this;
        // Add buttons
        Button getStarted = (Button)findViewById(R.id.letsGetStarted);
        // Button actions
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cs) {
                loadInitialActivity();
            }
        } );

    }

    public void toastAsync(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }


    public void loadInitialActivity(){
        List<String> walletRecordKeys = Wallet.Companion.getWalletsRecordKeys(this.appContext);

        // Check if a wallet is already stored
        if (walletRecordKeys.size() > 0) {
            String[] addressInfo = walletRecordKeys.get(0).split("/");
            String address = addressInfo[2];
            this.showPassphraseDialog(address, "Please enter the wallet passphrase");
        }else {
          //  toastAsync("#record keys "+walletRecordKeys.size());
            this.loadCreateImportWalletActivity();
        }
    }

    protected void loadHomeActivity() {
        Intent messageScreenIntent = new Intent(this, MainActivity.class);
        startActivity(messageScreenIntent);
    }

    protected void loadCreateImportWalletActivity() {
        Intent iwIntent = new Intent(this, ImportWalletActivity.class);
        startActivity(iwIntent);
    }

    protected synchronized void getWallet(final String address, String passphrase) {
        Wallet.Companion.retrieve("ETH", "4", address, passphrase, appContext, new Function2<WalletPersistenceError, Wallet, Unit>() {
            @Override
            public Unit invoke(WalletPersistenceError walletPersistenceError, Wallet retrievedWallet) {
                if (retrievedWallet != null) {
                    WalletActivity.this.wallet = retrievedWallet;
                    WalletActivity.this.loadHomeActivity();

                }else{
                    WalletActivity.this.showPassphraseDialog(address, "Try again, wrong passphrase");
                }
                return null;
            }
        });
    }

    protected void showPassphraseDialog(final String address, String message) {
        // get passphrase_dialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(WalletActivity.this);
        View promptView = layoutInflater.inflate(R.layout.passphrase_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WalletActivity.this);
        alertDialogBuilder.setView(promptView);
        // Dialog elements
        final TextView editText = (TextView) promptView.findViewById(R.id.private_key_text);
        final TextView titleText = (TextView) promptView.findViewById(R.id.dialog_title);
        // Set title
        titleText.setText(message);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editText.getText().toString().trim().length() > 0){
                            WalletActivity.this.getWallet(address,editText.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
