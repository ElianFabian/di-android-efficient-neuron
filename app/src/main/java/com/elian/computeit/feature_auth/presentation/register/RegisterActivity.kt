package com.elian.computeit.feature_auth.presentation.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.*
import com.elian.computeit.databinding.ActivityRegisterBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.*
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnRegister
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnShowErrorMessage
import com.elian.computeit.feature_auth.presentation.util.AuthError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity()
{
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var binding: ActivityRegisterBinding


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
            tietEmail.addTextChangedListener { viewModel.onAction(EnterEmail(it.toString().trim())) }
            tietUsername.addTextChangedListener { viewModel.onAction(EnterUsername(it.toString().trim())) }
            tietPassword.addTextChangedListener { viewModel.onAction(EnterPassword(it.toString().trim())) }
            tietConfirmPassword.addTextChangedListener { viewModel.onAction(EnterConfirmPassword(it.toString().trim())) }

            btnRegister.setOnClickListener { viewModel.onAction(Register) }
        }
    }

    private fun subscribeToEvents() = viewModel.apply()
    {
        collectFlowWhenStarted(eventFlow)
        {
            when (it)
            {
                is OnRegister         -> navigateTo<LoginActivity>(bundleOf(*it.args.toTypedArray()))
                is OnShowErrorMessage -> toast(it.error.asString(this@RegisterActivity))
            }
        }
        collectLatestFlowWhenStarted(emailState.map { it.error })
        {
            binding.tilEmail.error2 = when (it)
            {
                is AuthError.Empty   -> getString(R.string.error_cant_be_empty)
                is AuthError.Invalid -> getString(R.string.error_email_invalid).format(it.example)
                else                 -> null
            }
        }
        collectLatestFlowWhenStarted(usernameState.map { it.error })
        {
            binding.tilUsername.error2 = when (it)
            {
                is AuthError.Invalid  -> getString(R.string.error_username_invalid).format(it.validCharacters)
                is AuthError.Empty    -> getString(R.string.error_cant_be_empty)
                is AuthError.TooShort -> getString(R.string.error_too_short).format(it.minLength)
                is AuthError.TooLong  -> getString(R.string.error_too_long).format(it.maxLength)
                else                  -> null
            }
        }
        collectLatestFlowWhenStarted(passwordState.map { it.error })
        {
            binding.tilPassword.error2 = when (it)
            {
                is AuthError.Empty    -> getString(R.string.error_cant_be_empty)
                is AuthError.TooShort -> getString(R.string.error_too_short).format(it.minLength)
                is AuthError.Invalid  -> getString(R.string.error_password_invalid).format(it.minCharacterCount, it.validCharacters)
                is AuthError.TooLong  -> getString(R.string.error_too_long).format(it.maxLength)
                else                  -> null
            }
        }
        collectLatestFlowWhenStarted(confirmPasswordState.map { it.error })
        {
            binding.tilConfirmPassword.error2 = when (it)
            {
                is AuthError.Empty   -> getString(R.string.error_cant_be_empty)
                is AuthError.Invalid -> getString(R.string.error_passwords_dont_match)
                else                 -> null
            }
        }
        collectLatestFlowWhenStarted(loadingState) { binding.pbLoading.isVisible = it }
    }
}