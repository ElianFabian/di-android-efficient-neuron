package com.elian.computeit.feature_auth.presentation.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.elian.computeit.R
import com.elian.computeit.core.domain.states.TextFieldError
import com.elian.computeit.core.presentation.MainActivity
import com.elian.computeit.core.presentation.util.extensions.*
import com.elian.computeit.core.presentation.util.getUsernameErrorMessage
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.ActivityRegisterBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.*
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnRegister
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnShowErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity()
{
    private val viewModel by viewModels<RegisterViewModel>()
    private val binding by viewBinding(ActivityRegisterBinding::inflate)


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        initUi()
        subscribeToEvents()
    }

    override fun onBackPressed()
    {
        navigateTo<LoginActivity>()
    }


    private fun initUi() = binding.apply2()
    {
        tietUsername.addTextChangedListener { viewModel.onAction(EnterUsername("$it".trim())) }
        tietPassword.addTextChangedListener { viewModel.onAction(EnterPassword("$it".trim())) }
        tietConfirmPassword.addTextChangedListener { viewModel.onAction(EnterConfirmPassword("$it".trim())) }

        btnRegister.setOnClickListener { viewModel.onAction(Register) }
    }

    private fun subscribeToEvents() = viewModel.apply2()
    {
        collectFlowWhenStarted(eventFlow)
        {
            when (it)
            {
                is OnRegister         -> navigateTo<MainActivity>()
                is OnShowErrorMessage -> toast(it.error.asString(this@RegisterActivity))
            }
        }
        collectLatestFlowWhenStarted(usernameState.map { it.error })
        {
            binding.tilUsername.error2 = getUsernameErrorMessage(this@RegisterActivity, it)
        }
        collectLatestFlowWhenStarted(passwordState.map { it.error })
        {
            binding.tilPassword.error2 = when (it)
            {
                is TextFieldError.Empty    -> getString(R.string.error_cant_be_empty)
                is TextFieldError.TooShort -> getString(R.string.error_too_short).format(it.minLength)
                is TextFieldError.Invalid  -> getString(R.string.error_password_invalid).format(it.minCharacterCount, it.validCharacters)
                is TextFieldError.TooLong  -> getString(R.string.error_too_long).format(it.maxLength)
                else                       -> null
            }
        }
        collectLatestFlowWhenStarted(confirmPasswordState.map { it.error })
        {
            binding.tilConfirmPassword.error2 = when (it)
            {
                is TextFieldError.Empty   -> getString(R.string.error_cant_be_empty)
                is TextFieldError.Invalid -> getString(R.string.error_passwords_dont_match)
                else                      -> null
            }
        }
        collectLatestFlowWhenStarted(loadingState) { binding.pbLoading.isVisible = it }
    }
}