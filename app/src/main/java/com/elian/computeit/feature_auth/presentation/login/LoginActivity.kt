package com.elian.computeit.feature_auth.presentation.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.presentation.MainActivity
import com.elian.computeit.core.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.util.extensions.error2
import com.elian.computeit.core.util.extensions.navigateTo
import com.elian.computeit.core.util.extensions.toast
import com.elian.computeit.databinding.ActivityLoginBinding
import com.elian.computeit.feature_auth.presentation.register.RegisterActivity
import com.elian.computeit.feature_auth.presentation.util.AuthError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    private val userFromFields = object
    {
        val email get() = binding.tieEmail.text.toString().trim()
        val password get() = binding.tiePassword.text.toString().trim()
    }

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        subscribeToEvents()
    }

    //endregion

    //region Methods

    private fun initUi()
    {
        binding.tieEmail.addTextChangedListener { setEmailError(null) }
        binding.tiePassword.addTextChangedListener { setPasswordError(null) }

        binding.btnLogin.setOnClickListener()
        {
            onActionWhenStarted(LoginAction.EnteredEmail(userFromFields.email))
            onActionWhenStarted(LoginAction.EnteredPassword(userFromFields.password))
            onActionWhenStarted(LoginAction.Login)
        }
        binding.btnRegister.setOnClickListener { navigateTo<RegisterActivity>() }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is LoginEvent.OnLogin            ->
                {
                    viewModel.saveUserEmail(userFromFields.email)
                    navigateTo<MainActivity>()
                }
                is LoginEvent.OnShowErrorMessage -> toast(it.error.asString(this))
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
        collectLatestFlowWhenStarted(viewModel.loadingState)
        {
            binding.pbLoading.isVisible = it
        }
    }

    private fun setEmailError(text: String?)
    {
        binding.tilEmail.error2 = text
    }

    private fun setPasswordError(text: String?)
    {
        binding.tilPassword.error2 = text
    }

    private fun onActionWhenStarted(action: LoginAction)
    {
        lifecycleScope.launchWhenStarted { viewModel.onAction(action) }
    }

    //endregion
}