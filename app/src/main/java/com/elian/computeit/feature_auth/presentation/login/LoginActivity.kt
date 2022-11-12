package com.elian.computeit.feature_auth.presentation.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.elian.computeit.R
import com.elian.computeit.core.presentation.MainActivity
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.ActivityLoginBinding
import com.elian.computeit.feature_auth.presentation.login.LoginAction.*
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.*
import com.elian.computeit.feature_auth.presentation.register.RegisterActivity
import com.elian.computeit.feature_auth.presentation.util.AuthError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class LoginActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        subscribeToEvents()
    }


    private fun initUi()
    {
        binding.apply()
        {
            tietEmail.onTextChangedClearError2To(tilEmail)
            tietPassword.onTextChangedClearError2To(tilPassword)

            btnLogin.setOnClickListener()
            {
                viewModel.onAction(EnterEmail(tietEmail.text.toString().trim()))
                viewModel.onAction(EnterPassword(tietPassword.text.toString().trim()))
                viewModel.onAction(Login)
            }
            btnRegister.setOnClickListener { navigateTo<RegisterActivity>() }
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is OnLogin            -> navigateTo<MainActivity>()
                is OnShowErrorMessage -> toast(it.error.asString(this))
            }
        }
        collectLatestFlowWhenStarted(viewModel.emailState.map { it.error })
        {
            binding.tilEmail.error2 = when (it)
            {
                is AuthError.Empty -> getString(R.string.error_email_empty)
                else               -> null
            }
        }
        collectLatestFlowWhenStarted(viewModel.passwordState.map { it.error })
        {
            binding.tilPassword.error2 = when (it)
            {
                is AuthError.Empty -> getString(R.string.error_password_empty)
                else               -> null
            }
        }
        collectLatestFlowWhenStarted(viewModel.loadingState)
        {
            binding.pbLoading.isVisible = it
        }
    }
}