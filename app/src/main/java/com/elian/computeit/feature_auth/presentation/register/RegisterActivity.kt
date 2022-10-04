package com.elian.computeit.feature_auth.presentation.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.util.extensions.error2
import com.elian.computeit.core.util.extensions.navigateTo
import com.elian.computeit.databinding.ActivityRegisterBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_auth.presentation.util.AuthError
import com.elian.computeit.core.util.extensions.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    private val userFromFields = object
    {
        val email get() = binding.tieEmail.text.toString().trim()
        val password get() = binding.tiePassword.text.toString().trim()
        val confirmPassword get() = binding.tieConfirmPassword.text.toString().trim()
    }

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        subscribeToEvents()
    }

    override fun onBackPressed()
    {
        navigateTo<LoginActivity>()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        binding.tieEmail.addTextChangedListener { setEmailError(null) }
        binding.tiePassword.addTextChangedListener { setPasswordError(null) }
        binding.tieConfirmPassword.addTextChangedListener { setConfirmPasswordError(null) }

        binding.btnRegister.setOnClickListener()
        {
            onActionWhenStarted(RegisterAction.ReceivedEmail(userFromFields.email))
            onActionWhenStarted(RegisterAction.ReceivedPassword(userFromFields.password))
            onActionWhenStarted(RegisterAction.ReceivedConfirmPassword(userFromFields.confirmPassword))
            onActionWhenStarted(RegisterAction.Register)
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is RegisterEvent.Register         -> navigateTo<LoginActivity>()
                is RegisterEvent.ShowErrorMessage -> toast(it.error.asString(applicationContext))
            }
        }
        collectLatestFlowWhenStarted(viewModel.emailState)
        {
            setEmailError(when (it.error)
            {
                is AuthError.ValueEmpty   -> getString(R.string.error_email_empty)
                is AuthError.ValueInvalid -> String.format(
                    getString(R.string.error_email_invalid), it.error.example
                )
                else                      -> null
            })
        }
        collectLatestFlowWhenStarted(viewModel.passwordState)
        {
            setPasswordError(when (it.error)
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
            })
        }
        collectLatestFlowWhenStarted(viewModel.confirmPasswordState)
        {
            setConfirmPasswordError(when (it.error)
            {
                is AuthError.ValueEmpty   -> getString(R.string.error_password_empty)
                is AuthError.ValueInvalid -> getString(R.string.error_passwords_dont_match)
                else                      -> null
            })
        }
        collectLatestFlowWhenStarted(viewModel.loadingState)
        {
            if (it) showProgress() else hideProgress()
        }
    }

    private fun showProgress()
    {
        binding.pbLoading.isVisible = true
    }

    private fun hideProgress()
    {
        binding.pbLoading.isVisible = false
    }

    private fun setEmailError(text: String?)
    {
        binding.tilEmail.error2 = text
    }

    private fun setPasswordError(text: String?)
    {
        binding.tilPassword.error2 = text
    }

    private fun setConfirmPasswordError(text: String?)
    {
        binding.tilConfirmPassword.error2 = text
    }

    private fun onActionWhenStarted(action: RegisterAction)
    {
        lifecycleScope.launchWhenStarted { viewModel.onAction(action) }
    }

    //endregion
}