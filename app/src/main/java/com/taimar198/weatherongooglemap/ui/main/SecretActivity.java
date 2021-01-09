package com.taimar198.weatherongooglemap.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.taimar198.weatherongooglemap.R;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SecretActivity extends AppCompatActivity {

    private static final String KEY_NAME = "hihi";
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(SecretActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                finish();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                // NullPointerException is unhandled; use Objects.requireNonNull().
//                byte[] encryptedInfo = new byte[0];
//                try {
//                    encryptedInfo = result.getCryptoObject().getCipher().doFinal(
//                            "plaintext-string".getBytes(Charset.defaultCharset()));
//                    Log.d("MY_APP_TAG", "Encrypted information: " +
//                            Arrays.toString(encryptedInfo));
//                    Intent intent = new Intent(SecretActivity.this, MainActivity.class);
//                    startActivity(intent);
//                } catch (BadPaddingException e) {
//                    e.printStackTrace();
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                }
                Intent intent = new Intent(SecretActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

//        promptInfo = new BiometricPrompt.PromptInfo.Builder()
//                .setTitle("WEATHER ON GOOGLE")
//                .setSubtitle("Use fingerprint to unlock ")
//                .setNegativeButtonText("Use account password")
//                .build();
//        try {
//            generateSecretKey(new KeyGenParameterSpec.Builder(
//                    KEY_NAME,
//                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
//                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                    .setUserAuthenticationRequired(true)
//                    // Invalidate the keys if the user has registered a new biometric
//                    // credential, such as a new fingerprint. Can call this method only
//                    // on Android 7.0 (API level 24) or higher. The variable
//                    // "invalidatedByBiometricEnrollment" is true by default.
//                    .setInvalidatedByBiometricEnrollment(true)
//                    .build());
//        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        }
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("WEATHER ON GOOGLE")
                .setSubtitle("Use fingerprint to unlock ")
                .setNegativeButtonText("Or Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
//        Button biometricLoginButton = findViewById(R.id.biometric_login);
//        biometricLoginButton.setOnClickListener(view -> {
//            // Exceptions are unhandled within this snippet.
//            Cipher cipher = null;
//            try {
//                cipher = getCipher();
//                SecretKey secretKey = getSecretKey();
//                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//                biometricPrompt.authenticate(promptInfo,
//                        new BiometricPrompt.CryptoObject(cipher));
//            } catch (NoSuchPaddingException | NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException | InvalidKeyException | UnrecoverableKeyException e) {
//                e.printStackTrace();
//            }
//
//        });
//        Button biometricLoginButton = findViewById(R.id.biometric_login);
//        biometricLoginButton.setOnClickListener(view -> {
//            biometricPrompt.authenticate(promptInfo);
//        });
        biometricPrompt.authenticate(promptInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateSecretKey(KeyGenParameterSpec keyGenParameterSpec) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }

    private SecretKey getSecretKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null);
        return ((SecretKey)keyStore.getKey(KEY_NAME, null));
    }

    private Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7);
    }

}