package com.tinf19.musicparty.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tinf19.musicparty.R;
import com.tinf19.musicparty.util.Constants;
import com.tinf19.musicparty.databinding.ActivityClientBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class ClientActivity extends AppCompatActivity {

    private static final String TAG = ClientActivity.class.getName();
    private final Random rand = new Random();
    private ActivityClientBinding binding;
    private IntentIntegrator qrScan;
    private ImageButton scanQRCodeImageButton;
    private EditText ipAddressEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ipAddressEditText = binding.etAddress;
        passwordEditText = binding.etPassword;

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        qrScan.setPrompt(getResources().getString(R.string.text_qrButtonHint));
        qrScan.setBeepEnabled(false);

        if(getIntent().getData() != null) {
            String password = getIntent().getData().getQueryParameter(Constants.PASSWORD);
            String ip = getIntent().getData().getQueryParameter(Constants.ADDRESS);

            if (password != null && ip != null) {
                ipAddressEditText.setText(ip);
                passwordEditText.setText(password);
            }
        }
        ImageButton infoUsernameToolboxImageButton = binding.infoUsernameSymbolImageButton;
        TextView usernameDesciptionTextView = binding.usernameDescriptionTextView;
        if(infoUsernameToolboxImageButton != null && usernameDesciptionTextView != null) {
            infoUsernameToolboxImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(usernameDesciptionTextView.getVisibility() == View.INVISIBLE)
                        usernameDesciptionTextView.setVisibility(View.VISIBLE);
                    else
                        usernameDesciptionTextView.setVisibility(View.INVISIBLE);
                }
            });
        }
        ImageButton infoIpToolboxImageButton = binding.infoIpSymbolImageButton;
        TextView loginDescriptionTextView = binding.loginDescriptionTextView;
        if(infoIpToolboxImageButton != null && loginDescriptionTextView != null) {
            infoIpToolboxImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(loginDescriptionTextView.getVisibility() == View.INVISIBLE)
                        loginDescriptionTextView.setVisibility(View.VISIBLE);
                    else
                        loginDescriptionTextView.setVisibility(View.INVISIBLE);
                }
            });
        }
        scanQRCodeImageButton = binding.loginViaQRCodeImageButton;
        if(scanQRCodeImageButton != null) {
            scanQRCodeImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qrScan.initiateScan();
                }
            });
        }
        scanQRCodeImageButton = binding.loginViaQRCodeImageButton;
        scanQRCodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, PartyActivity.class);
        intent.putExtra(Constants.PASSWORD, binding.etPassword.getText().toString());
        intent.putExtra(Constants.ADDRESS, binding.etAddress.getText().toString());
        if (!binding.usernameEditText.getText().toString().equals(""))
            intent.putExtra(Constants.USERNAME, binding.usernameEditText.getText().toString());
        else {
            intent.putExtra(Constants.USERNAME, randomIdentifier());
        }
        view.getContext().startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, getString(R.string.text_qrCodeNoResult), Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    if(ipAddressEditText != null) {
                        ipAddressEditText.setText(obj.getString("ipaddress"));
                        passwordEditText.setText(obj.getString("password"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(Constants.LEXICON.charAt(rand.nextInt(Constants.LEXICON.length())));
            }
        }
        return builder.toString();
    }


}