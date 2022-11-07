package com.elian.computeit.feature_auth.presentation.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.ActivityRegisterBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.*
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnRegister
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnShowErrorMessage
import com.elian.computeit.feature_auth.presentation.util.AuthError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        subscribeToEvents()
    }

    override fun onBackPressed()
    {
        navigateTo<LoginActivity>()
    }


    private fun initUi()
    {
        binding.apply()
        {
            tietEmail.onTextChangedClearError2To(tilEmail)
            tietPassword.onTextChangedClearError2To(tilPassword)
            tietConfirmPassword.onTextChangedClearError2To(tilConfirmPassword)

            btnRegister.setOnClickListener()
            {
                viewModel onAction EnterEmail(tietEmail.text.toString().trim())
                viewModel onAction EnterPassword(tietPassword.text.toString().trim())
                viewModel onAction EnterConfirmPassword(tietConfirmPassword.text.toString().trim())
                viewModel onAction Register
            }
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is OnRegister         -> navigateTo<LoginActivity>()
                is OnShowErrorMessage -> toast(it.error.asString(this))
            }
        }
        collectLatestFlowWhenStarted(viewModel.emailState)
        {
            binding.tilEmail.error2 = when (it.error)
            {
                is AuthError.ValueEmpty   -> getString(R.string.error_email_empty)
                is AuthError.ValueInvalid -> String.format(
                    getString(R.string.error_email_invalid), it.error.example
                )
                else                      -> null
            }
        }
        collectLatestFlowWhenStarted(viewModel.passwordState)
        {
            binding.tilPassword.error2 = when (it.error)
            {
                is AuthError.ValueEmpty    -> getString(R.string.error_password_empty)
                is AuthError.ValueTooShort -> String.format(
                    getString(R.string.error_too_short), it.error.minLength
                )
                is AuthError.ValueInvalid  -> String.format(
                    getString(R.string.error_password_invalid), it.error.minCharacterCount, it.error.validCharacters
                )
                is AuthError.ValueTooLong  -> String.format(
                    getString(R.string.error_too_long), it.error.maxLength
                )
                else                       -> null
            }
        }
        collectLatestFlowWhenStarted(viewModel.confirmPasswordState)
        {
            binding.tilConfirmPassword.error2 = when (it.error)
            {
                is AuthError.ValueEmpty   -> getString(R.string.error_password_empty)
                is AuthError.ValueInvalid -> getString(R.string.error_passwords_dont_match)
                else                      -> null
            }
        }
        collectLatestFlowWhenStarted(viewModel.loadingState)
        {
            binding.pbLoading.isVisible = it
        }
    }
}