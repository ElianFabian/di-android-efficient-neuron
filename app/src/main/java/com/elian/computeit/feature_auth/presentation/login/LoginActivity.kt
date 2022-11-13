package com.elian.computeit.feature_auth.presentation.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.elian.computeit.R
import com.elian.computeit.core.presentation.MainActivity
import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.constants.EXTRA_EMAIL
import com.elian.computeit.core.util.constants.EXTRA_PASSWORD
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.ActivityLoginBinding
import com.elian.computeit.feature_auth.presentation.login.LoginAction.*
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.OnLogin
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.OnShowErrorMessage
import com.elian.computeit.feature_auth.presentation.register.RegisterActivity
import com.elian.computeit.feature_auth.presentation.util.AuthError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class LoginActivity : AppCompatActivity()
{
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding


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
            tietEmail.addTextChangedListener { viewModel.onAction(EnterEmail(it.toString().trim())) }
            tietPassword.addTextChangedListener { viewModel.onAction(EnterPassword(it.toString().trim())) }

            btnLogin.setOnClickListener { viewModel.onAction(Login) }
            btnRegister.setOnClickListener { navigateTo<RegisterActivity>() }

            intent.extras?.apply()
            {
                tietEmail.setText(getString(EXTRA_EMAIL))
                tietPassword.setText(getString(EXTRA_PASSWORD))
            }
        }
    }

    private fun subscribeToEvents() = viewModel.apply()
    {
        collectFlowWhenStarted(eventFlow)
        {
            when (it)
            {
                is OnLogin            -> navigateTo<MainActivity>()
                is OnShowErrorMessage -> toast(it.error.asString(this@LoginActivity))
            }
        }
        collectLatestFlowWhenStarted(emailState.map { it.error })
        {
            binding.tilEmail.error2 = getFieldError(it)
        }
        collectLatestFlowWhenStarted(passwordState.map { it.error })
        {
            binding.tilPassword.error2 = getFieldError(it)
        }
        collectLatestFlowWhenStarted(loadingState) { binding.pbLoading.isVisible = it }
    }

    private fun getFieldError(error: Error?) = when (error)
    {
        is AuthError.Empty -> getString(R.string.error_cant_be_empty)
        else               -> null
    }
}