package com.elian.efficientneuron.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elian.efficientneuron.databinding.ActivitySignUpBinding
import com.elian.efficientneuron.ui.login.LoginActivity

class SignupActivity : AppCompatActivity()
{
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            onDestroy()
        }
    }
}