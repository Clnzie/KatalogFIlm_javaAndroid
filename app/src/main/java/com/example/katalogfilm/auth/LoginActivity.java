package com.example.katalogfilm.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.example.katalogfilm.auth.RegisterActivity;
import com.example.katalogfilm.MainActivity;
import com.example.katalogfilm.databinding.ActivityLoginBinding;
import com.example.katalogfilm.data.response.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SupabaseAuthManager authManager;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new SupabaseAuthManager();
        sessionManager = new SessionManager(this);

        binding.btnLogin.setOnClickListener(v -> loginUser());

        // REGISTER LINK
        SpannableString registerText =
                new SpannableString("Belum punya akun? Daftar Sekarang");

        registerText.setSpan(
                new ForegroundColorSpan(Color.parseColor("#F4CE14")),
                18,
                registerText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        binding.tvRegisterLink.setText(registerText);

        binding.tvRegisterLink.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }

    private void loginUser() {

        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString();

        if (email.isEmpty()) {
            binding.edtEmail.setError("Email wajib diisi");
            binding.edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.edtPassword.setError("Password wajib diisi");
            binding.edtPassword.requestFocus();
            return;
        }

        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Logging in...");

        authManager.login(email, password,
                new Callback<AuthResponse>() {

                    @Override
                    public void onResponse(
                            Call<AuthResponse> call,
                            Response<AuthResponse> response
                    ) {

                        binding.btnLogin.setEnabled(true);
                        binding.btnLogin.setText("Login");

                        Log.d("SUPABASE_LOGIN",
                                "HTTP Code: " + response.code());

                        if (response.isSuccessful()
                                && response.body() != null) {

                            AuthResponse result = response.body();

                            if (result.getAccessToken() == null
                                    || result.getUser() == null) {

                                binding.tvLoginInfo.setText(
                                        "Session tidak lengkap"
                                );

                                return;
                            }

                            // SIMPAN SESSION
                            sessionManager.saveSession(result);

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login berhasil",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // BUKA MAIN ACTIVITY
                            Intent intent = new Intent(
                                    LoginActivity.this,
                                    MainActivity.class
                            );

                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            startActivity(intent);

                        } else {

                            try {

                                String errorBody =
                                        response.errorBody() != null
                                                ? response.errorBody().string()
                                                : "Unknown error";

                                Log.e("SUPABASE_LOGIN",
                                        "Error: " + errorBody);

                                binding.tvLoginInfo.setText(
                                        "Login gagal. HTTP "
                                                + response.code()
                                );

                            } catch (Exception e) {

                                Log.e("SUPABASE_LOGIN",
                                        "Error membaca response", e);
                            }

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login gagal",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<AuthResponse> call,
                            Throwable t
                    ) {

                        binding.btnLogin.setEnabled(true);
                        binding.btnLogin.setText("Login");

                        Log.e("SUPABASE_LOGIN",
                                "Network Error: " + t.getMessage(), t);

                        binding.tvLoginInfo.setText(
                                "Connection failed"
                        );
                    }
                });
    }
}