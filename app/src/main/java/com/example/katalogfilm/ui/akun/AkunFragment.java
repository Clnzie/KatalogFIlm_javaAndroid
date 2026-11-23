package com.example.katalogfilm.ui.akun;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.katalogfilm.auth.LoginActivity;
import com.example.katalogfilm.auth.SessionManager;
import com.example.katalogfilm.databinding.FragmentAkunBinding;

public class AkunFragment extends Fragment {

    private FragmentAkunBinding binding;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentAkunBinding.inflate(
                inflater,
                container,
                false
        );

        sessionManager = new SessionManager(requireContext());

        // TAMPILKAN EMAIL USER
        String email = sessionManager.getEmail();

        if (email != null && !email.isEmpty()) {
            binding.tvAccountEmail.setText(email);
        } else {
            binding.tvAccountEmail.setText("Email tidak tersedia");
        }

        // LOGOUT
        binding.btnKeluar.setOnClickListener(v -> logoutUser());

        return binding.getRoot();
    }

    private void logoutUser() {

        // HAPUS SESSION
        sessionManager.logout();

        Toast.makeText(
                requireContext(),
                "Logout berhasil",
                Toast.LENGTH_SHORT
        ).show();

        // KEMBALI KE LOGIN
        Intent intent = new Intent(
                requireActivity(),
                LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}