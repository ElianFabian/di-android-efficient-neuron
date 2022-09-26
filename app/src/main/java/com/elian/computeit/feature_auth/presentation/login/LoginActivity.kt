package com.elian.computeit.feature_auth.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.MainActivity
import com.elian.computeit.R
import com.elian.computeit.core.domain.util.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.asString
import com.elian.computeit.data.model.User
import com.elian.computeit.databinding.ActivityLoginBinding
import com.elian.computeit.feature_auth.presentation.register.RegisterActivity
import com.elian.computeit.feature_auth.presentation.util.AuthError
import com.elian.computeit.util.extension.toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    private val userFromFields: User
        get() = User(
            email = binding.tieEmail.text.toString(),
            password = binding.tiePassword.text.toString()
        )

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        logInIfUserExists()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        subscribeToEvents()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        binding.btnLogin.setOnClickListener()
        {
            lifecycleScope.launchWhenStarted { viewModel.onAction(LoginAction.Login) }
        }
        binding.btnSignup.setOnClickListener()
        {
            goToRegister()
        }
        binding.tieEmail.addTextChangedListener()
        {
            lifecycleScope.launchWhenStarted { viewModel.onAction(LoginAction.EnteredEmail(it.toString().trim())) }
        }
        binding.tiePassword.addTextChangedListener()
        {
            lifecycleScope.launchWhenStarted { viewModel.onAction(LoginAction.EnteredPassword(it.toString().trim())) }
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is LoginEvent.Login            -> goToMainActivity()
                is LoginEvent.ShowErrorMessage -> toast(it.error.asString(applicationContext))
            }
        }
        collectLatestFlowWhenStarted(viewModel.emailState)
        {
            setEmailError(when (it.error)
            {
                is AuthError.ValueEmpty -> getString(R.string.error_email_empty)
                else                    -> null
            })
        }
        collectLatestFlowWhenStarted(viewModel.passwordState)
        {
            setPasswordError(when (it.error)
            {
                is AuthError.ValueEmpty -> getString(R.string.error_password_empty)
                else                    -> null
            })
        }
        collectLatestFlowWhenStarted(viewModel.loginState)
        {
            if (it.isLoading) showProgress() else hideProgress()
        }
    }

    private fun goToMainActivity()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToRegister()
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun logInIfUserExists()
    {
        Firebase.auth.addAuthStateListener { auth ->

            if (auth.currentUser != null)
            {
                // TODO: recordar usuario con sesión iniciada
            }
        }
    }

    //endregion

    //region LogInContract.View

    private fun showProgress()
    {

    }

    private fun hideProgress()
    {

    }

    private fun setEmailError(text: String?)
    {
        binding.tilEmail.error = text
    }

    private fun setPasswordError(text: String?)
    {
        binding.tilPassword.error = text
    }

    //endregion
}