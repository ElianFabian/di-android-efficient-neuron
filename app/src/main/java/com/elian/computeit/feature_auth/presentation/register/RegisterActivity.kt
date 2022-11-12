package com.elian.computeit.feature_auth.presentation.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.elian.computeit.R
import com.elian.computeit.core.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.util.extensions.error2
import com.elian.computeit.core.util.extensions.navigateTo
import com.elian.computeit.core.util.extensions.toast
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
            tietEmail.addTextChangedListener { viewModel.onAction(EnterEmail(it.toString().trim())) }
            tietPassword.addTextChangedListener { viewModel.onAction(EnterPassword(it.toString().trim())) }
            tietConfirmPassword.addTextChangedListener { viewModel.onAction(EnterConfirmPassword(it.toString().trim())) }

            btnRegister.setOnClickListener { viewModel.onAction(Register) }
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
        collectLatestFlowWhenStarted(viewModel.emailState.map { it.error })
        {
            binding.tilEmail.error2 = when (it)
            {
                is AuthError.Empty   -> getString(R.string.error_email_empty)
                is AuthError.Invalid -> getString(R.string.error_email_invalid).format(it.example)
                else                 -> null
            }
        }
        collectLatestFlowWhenStarted(viewModel.passwordState.map { it.error })
        {
            binding.tilPassword.error2 = when (it)
            {
                is AuthError.Empty    -> getString(R.string.error_password_empty)
                is AuthError.TooShort -> getString(R.string.error_too_short).format(it.minLength)
                is AuthError.Invalid  -> getString(R.string.error_password_invalid).format(it.minCharacterCount, it.validCharacters)
                is AuthError.TooLong  -> getString(R.string.error_too_long).format(it.maxLength)
                else                  -> null
            }
        }
        collectLatestFlowWhenStarted(viewModel.confirmPasswordState.map { it.error })
        {
            binding.tilConfirmPassword.error2 = when (it)
            {
                is AuthError.Empty   -> getString(R.string.error_password_empty)
                is AuthError.Invalid -> getString(R.string.error_passwords_dont_match)
                else                 -> null
            }
        }
        collectLatestFlowWhenStarted(viewModel.loadingState)
        {
            binding.pbLoading.isVisible = it
        }
    }
}