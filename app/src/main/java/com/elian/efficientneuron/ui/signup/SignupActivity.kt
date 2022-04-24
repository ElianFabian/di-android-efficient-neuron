package com.elian.efficientneuron.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elian.efficientneuron.databinding.ActivitySignupBinding
import com.elian.efficientneuron.ui.login.LoginActivity

class SignupActivity : AppCompatActivity()
{
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI()
    {
        binding.btnSignup.setOnClickListener()
        {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            onDestroy()
        }
    }
}