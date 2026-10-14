package com.dungtien.amthucbavi.member1_auth_home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dungtien.amthucbavi.databinding.ActivityAuthBinding
import com.dungtien.amthucbavi.services.AuthService
import com.google.android.material.tabs.TabLayout

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    binding.formLogin.visibility = View.VISIBLE
                    binding.formRegister.visibility = View.GONE
                } else {
                    binding.formLogin.visibility = View.GONE
                    binding.formRegister.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.btnQuickFill.setOnClickListener {
            binding.etLoginEmail.setText("khachhang@gmail.com")
            binding.etLoginPassword.setText("123456")
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val pass = binding.etLoginPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = AuthService.getInstance(this).login(email, pass)
            if (user != null) {
                navigateToHome()
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu chưa chính xác.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etRegName.text.toString().trim()
            val email = binding.etRegEmail.text.toString().trim()
            val phone = binding.etRegPhone.text.toString().trim()
            val pass = binding.etRegPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                AuthService.getInstance(this).register(name, email, phone, pass)
                navigateToHome()
            } catch (e: Exception) {
                val msg = if (e.message?.contains("UNIQUE") == true) "Email này đã được đăng ký." else "Lỗi: ${e.message}"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
