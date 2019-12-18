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

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import network.pocket.core.errors.WalletPersistenceError;
import network.pocket.core.model.Wallet;
import network.pocket.eth.PocketEth;

public class ImportWalletActivity extends AppCompatActivity {

    PocketEth pocketEth;
    Wallet wallet;
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);
        Button import_W = (Button)findViewById(R.id.import_wallet_btn);
        Button create_W = (Button)findViewById(R.id.create_wallet_btn);

        import_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View importView) {
                importWallet();
            }
        } );

        create_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View cw) {
                showCreateWallet();
            }
        });

        this.appContext = ImportWalletActivity.this;
        // Instantiate PocketAion
        List<String> netIds = new ArrayList<>();
        netIds.add(PocketEth.Networks.RINKEBY.getNetID());
        this.pocketEth = new PocketEth(this.appContext,"", netIds,5,50000,"4");
    }
    protected void importWallet() {
        TextView private_key_text = (TextView)findViewById(R.id.private_key_text);
        String privateKey = private_key_text.getText().toString();

        if (privateKey.trim().length() > 0) {
            this.wallet = this.pocketEth.getRinkeby().importWallet(privateKey);

            if (wallet != null) {
                showPassphraseDialog(this.wallet);
            }else{
                showDialog("Failed to import the wallet, please try again.");
            }
        }else{
            showDialog("The private key field is empty, please add the private key.");
        }

    }
    protected void loadHomeActivity() {
        Intent messageScreenIntent = new Intent(this, MainActivity.class);
        startActivity(messageScreenIntent);
    }

    protected synchronized void getWallet(final String address, String passphrase) {
        Wallet.Companion.retrieve("ETH", "4", address, passphrase, appContext, new Function2<WalletPersistenceError, Wallet, Unit>() {
            @Override
            public Unit invoke(WalletPersistenceError walletPersistenceError, Wallet retrievedWallet) {
                if (retrievedWallet != null) {
                    ImportWalletActivity.this.wallet = retrievedWallet;
                    ImportWalletActivity.this.loadHomeActivity();

                }else{
                  //  ImportWalletActivity.this.showPassphraseDialog("Try again, wrong passphrase");
                }
                return null;
            }
        });
    }

    protected void showPassphraseDialog(final Wallet wallet) {
        // get passphrase_dialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ImportWalletActivity.this);
        View promptView = layoutInflater.inflate(R.layout.passphrase_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImportWalletActivity.this);
        alertDialogBuilder.setView(promptView);

        final TextView editText = (TextView) promptView.findViewById(R.id.private_key_text);
        final TextView titleText = (TextView) promptView.findViewById(R.id.dialog_title);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editText.getText().toString().trim().length() > 0){
                            String passphrase = editText.getText().toString();

                            wallet.save(passphrase, ImportWalletActivity.this, new Function1<WalletPersistenceError, Unit>() {
                                @Override
                                public Unit invoke(WalletPersistenceError walletPersistenceError) {
                                    ImportWalletActivity.this.loadHomeActivity();
                                    return null;
                                }

                            });

                        }else{
                            ImportWalletActivity.this.showPassphraseDialog(wallet);
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
    public void showDialog(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this.appContext);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public void showCreateWallet() {
        Intent create_wallet_intent = new Intent(this, CreateWalletActivity.class);
        startActivity(create_wallet_intent);
    }
}
