package com.example.katalogfilm.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import androidx.appcompat.app.AppCompatActivity;

import com.example.katalogfilm.databinding.ActivityRegisterBinding;
import com.example.katalogfilm.data.response.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    private ActivityRegisterBinding binding;
    private SupabaseAuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new SupabaseAuthManager();

        binding.btnRegister.setOnClickListener(v -> registerUser());

        // LINK LOGIN
        String loginText = "Sudah punya akun? Masuk Sekarang";

        SpannableString spannableString =
                new SpannableString(loginText);

        int start = loginText.indexOf("Masuk Sekarang");

        spannableString.setSpan(
                new ForegroundColorSpan(
                        Color.parseColor("#F4CE14")
                ),
                start,
                loginText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        binding.tvLoginLink.setText(spannableString);

        binding.tvLoginLink.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
        });
    }

    private void registerUser() {

        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString();
        String confirmPassword =
                binding.edtConfirmPassword.getText().toString();

        // Validasi input
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

        if (password.length() < 6) {
            binding.edtPassword.setError(
                    "Password minimal 6 karakter"
            );
            binding.edtPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            binding.edtConfirmPassword.setError(
                    "Password tidak sama"
            );
            binding.edtConfirmPassword.requestFocus();
            return;
        }

        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setText("Registering...");

        authManager.register(email, password,
                new Callback<AuthResponse>() {

                    @Override
                    public void onResponse(
                            Call<AuthResponse> call,
                            Response<AuthResponse> response
                    ) {

                        binding.btnRegister.setEnabled(true);
                        binding.btnRegister.setText("Register");

                        // Log HTTP status
                        Log.d("SUPABASE_REGISTER", "HTTP Code: " + response.code());

                        if (response.isSuccessful()) {

                            AuthResponse result = response.body();

                            if (result != null) {

                                Log.d("SUPABASE_REGISTER", "Response berhasil");

                                if (result.getUser() != null) {
                                    Log.d("SUPABASE_REGISTER",
                                            "User ID: " + result.getUser().getId());

                                    Log.d("SUPABASE_REGISTER",
                                            "Email: " + result.getUser().getEmail());
                                } else {
                                    Log.d("SUPABASE_REGISTER", "User object null");
                                }

                                Log.d("SUPABASE_REGISTER",
                                        "Access Token tersedia: "
                                                + (result.getAccessToken() != null));

                                String message = "Register berhasil!";

                                if (result.getAccessToken() == null
                                        || result.getAccessToken().isEmpty()) {

                                    message = "Register berhasil! Cek email verifikasi.";
                                }

                                binding.tvRegisterInfo.setText(message);

                                Toast.makeText(
                                        RegisterActivity.this,
                                        message,
                                        Toast.LENGTH_LONG
                                ).show();

                            } else {

                                Log.e("SUPABASE_REGISTER", "Response body null");

                                binding.tvRegisterInfo.setText(
                                        "Response kosong dari server"
                                );
                            }

                        } else {

                            try {

                                String errorBody = response.errorBody() != null
                                        ? response.errorBody().string()
                                        : "Error body kosong";

                                Log.e("SUPABASE_REGISTER",
                                        "HTTP Code: " + response.code());

                                Log.e("SUPABASE_REGISTER",
                                        "Error Body: " + errorBody);

                                binding.tvRegisterInfo.setText(
                                        "Register gagal: HTTP " + response.code()
                                );

                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Register gagal: HTTP " + response.code(),
                                        Toast.LENGTH_LONG
                                ).show();

                            } catch (Exception e) {

                                Log.e("SUPABASE_REGISTER",
                                        "Gagal membaca error", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<AuthResponse> call,
                            Throwable t
                    ) {

                        binding.btnRegister.setEnabled(true);
                        binding.btnRegister.setText("Register");

                        Log.e("SUPABASE_REGISTER",
                                "Network Error: " + t.getMessage(), t);

                        binding.tvRegisterInfo.setText(
                                "Connection failed: " + t.getMessage()
                        );

                        Toast.makeText(
                                RegisterActivity.this,
                                "Connection failed",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
