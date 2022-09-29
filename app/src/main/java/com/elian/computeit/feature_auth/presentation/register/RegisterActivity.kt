package com.elian.computeit.feature_auth.presentation.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.R
import com.elian.computeit.core.domain.util.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.asString
import com.elian.computeit.data.model.User
import com.elian.computeit.databinding.ActivityRegisterBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_auth.presentation.util.AuthError
import com.elian.computeit.util.extension.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    private val userFromFields: User
        get() = User(
            binding.tieEmail.text.toString(),
            binding.tiePassword.text.toString()
        )

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        subscribeToEvents()
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
            lifecycleScope.launchWhenStarted()
            {
                viewModel.onAction(RegisterAction.ReceivedEmail(binding.tieEmail.text.toString().trim()))
            }
            lifecycleScope.launchWhenStarted()
            {
                viewModel.onAction(RegisterAction.ReceivedPassword(binding.tiePassword.text.toString().trim()))
            }
            lifecycleScope.launchWhenStarted()
            {
                viewModel.onAction(RegisterAction.ReceivedConfirmPassword(binding.tieConfirmPassword.text.toString().trim()))
            }
            lifecycleScope.launchWhenStarted()
            {
                viewModel.onAction(RegisterAction.Register)
            }
        }
    }


    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is RegisterEvent.Register         -> goToLogin()
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
        collectLatestFlowWhenStarted(viewModel.loginState)
        {
            if (it.isLoading) showProgress()
            else hideProgress()
        }
    }

    private fun goToLogin()
    {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        onDestroy()
    }

    //endregion

    //region SignUpContract.View

    fun showProgress()
    {

    }

    fun hideProgress()
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

    private fun setConfirmPasswordError(text: String?)
    {
        binding.tilConfirmPassword.error = text
    }

    //endregion
}