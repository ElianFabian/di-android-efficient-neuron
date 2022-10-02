package com.elian.computeit.feature_auth.presentation.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.elian.computeit.MainActivity
import com.elian.computeit.R
import com.elian.computeit.core.domain.repository.AppSettingsRepository
import com.elian.computeit.core.domain.util.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.asString
import com.elian.computeit.core.presentation.util.extensions.error2
import com.elian.computeit.core.presentation.util.extensions.navigateTo
import com.elian.computeit.databinding.ActivityLoginBinding
import com.elian.computeit.feature_auth.presentation.util.AuthError
import com.elian.computeit.util.extension.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity()
{
    @Inject
    lateinit var settings: AppSettingsRepository
    
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

        tryLogInUser()

        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.btnLogin.setOnClickListener()
        {
            onActionWhenStarted(LoginAction.EnteredEmail(userFromFields.email))
            onActionWhenStarted(LoginAction.EnteredPassword(userFromFields.password))
            onActionWhenStarted(LoginAction.Login)
        }
        binding.btnRegister.setOnClickListener()
        {
            navigateTo<MainActivity>()
        }
    }

    private fun subscribeToEvents()
    {
        collectLatestFlowWhenStarted(viewModel.eventFlow)
        {
            when (it)
            {
                is LoginEvent.Login            ->
                {
                    settings.saveUserEmail(userFromFields.email)
                    navigateTo<MainActivity>()
                }
                is LoginEvent.ShowErrorMessage -> toast(it.error.asString(applicationContext))
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
            if (it) showProgress() else hideProgress()
        }
    }

    private fun tryLogInUser()
    {
        lifecycleScope.launch(Dispatchers.IO)
        {
            val isUserLoggedIn = settings.getUserEmail() != null

            if (isUserLoggedIn)
            {
                navigateTo<MainActivity>()
            }
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

    private fun onActionWhenStarted(action: LoginAction)
    {
        lifecycleScope.launchWhenStarted { viewModel.onAction(action) }
    }

    //endregion
}