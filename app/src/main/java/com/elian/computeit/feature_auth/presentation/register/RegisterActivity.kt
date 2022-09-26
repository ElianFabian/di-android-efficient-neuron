package com.elian.computeit.feature_auth.presentation.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.elian.computeit.R
import com.elian.computeit.core.domain.util.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.asString
import com.elian.computeit.data.model.User
import com.elian.computeit.databinding.ActivityRegisterBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_auth.presentation.util.AuthError
import com.elian.computeit.util.extension.toast

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
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        binding.btnSignup.setOnClickListener()
        {
            //presenter.signUp(userFromFields, binding.tieConfirmPassword.text.toString())
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
                is AuthError.ValueEmpty -> getString(R.string.error_email_empty)
                else                    -> null
            })
        }
        collectLatestFlowWhenStarted(viewModel.passwordState)
        {
            setPasswordError(when (it.error)
            {
                is AuthError.ValueEmpty    -> getString(R.string.error_password_empty)
                is AuthError.ValueTooShort -> "Password too short"
                is AuthError.ValueInvalid  -> getString(R.string.error_password_invalid)
                is AuthError.ValueTooLong  -> "Password too long"

                else                       -> null
            })
        }
        collectLatestFlowWhenStarted(viewModel.confirmPasswordState)
        {
            setConfirmPasswordError(when (it.error)
            {
                is AuthError.ValueEmpty   -> getString(R.string.error_password_empty)
                is AuthError.ValueInvalid -> "Passwords don't match"
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