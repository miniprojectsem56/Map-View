package com.example.mapview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.ContextParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapview.R;

public class BiometricActivity extends AppCompatActivity {


    TextView tvMessage;
    Button btLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        tvMessage = findViewById(R.id.tv_message);
        btLogin = findViewById(R.id.tv_button);

        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                tvMessage.setText("You can use Biometric to login.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                tvMessage.setText("This device doesn't have Biometric Sensor.");
                btLogin.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                tvMessage.setText("This device doesn't have Fingerprint Saved" +
                        "Please check your Security Settings.");
                btLogin.setVisibility(View.GONE);
                break;

        }
        BiometricPrompt prompt = new BiometricPrompt(this,
                ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Login Successful.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        BiometricPrompt.PromptInfo info = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric")
                .setNegativeButtonText("Cancel")
                .build();
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prompt.authenticate(info);
            }
        });

    }
}