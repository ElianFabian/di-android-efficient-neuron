package com.elian.efficientneuron.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elian.efficientneuron.MainActivity
import com.elian.efficientneuron.databinding.ActivityLoginBinding
import com.elian.efficientneuron.ui.signup.SignupActivity

class LoginActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI()
    {
        binding.btnLogin.setOnClickListener()
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            onDestroy()
        }
        binding.btnSignup.setOnClickListener()
        {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            onDestroy()
        }
    }
}