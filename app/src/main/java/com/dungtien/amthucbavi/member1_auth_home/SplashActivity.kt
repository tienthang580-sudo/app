package com.dungtien.amthucbavi.member1_auth_home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dungtien.amthucbavi.databinding.ActivitySplashBinding
import com.dungtien.amthucbavi.services.AuthService

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val user = AuthService.getInstance(this).restoreSession()
            val targetIntent = if (user != null) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, AuthActivity::class.java)
            }
            startActivity(targetIntent)
            finish()
        }, 1200)
    }
}
