package com.elian.computeit.feature_auth.presentation.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.elian.computeit.R
import com.elian.computeit.core.domain.errors.TextFieldError
import com.elian.computeit.core.presentation.MainActivity
import com.elian.computeit.core.presentation.util.extensions.allowMultilineAndDisableEnterNewLine
import com.elian.computeit.core.presentation.util.extensions.collectFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.collectLatestFlowWhenStarted
import com.elian.computeit.core.presentation.util.extensions.error2
import com.elian.computeit.core.presentation.util.extensions.navigateTo
import com.elian.computeit.core.presentation.util.extensions.showToast
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.Error
import com.elian.computeit.core.util.asString
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.ActivityLoginBinding
import com.elian.computeit.feature_auth.presentation.login.LoginAction.EnterPassword
import com.elian.computeit.feature_auth.presentation.login.LoginAction.EnterUsername
import com.elian.computeit.feature_auth.presentation.login.LoginAction.Login
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.OnLogin
import com.elian.computeit.feature_auth.presentation.login.LoginEvent.OnShowErrorMessage
import com.elian.computeit.feature_auth.presentation.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

	private val viewModel by viewModels<LoginViewModel>()
	private val binding by viewBinding(ActivityLoginBinding::inflate)


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(binding.root)

		subscribeToEvents()
		initializeUi()
	}


	private fun initializeUi() = using(binding) {
		tietUsername.allowMultilineAndDisableEnterNewLine()

		tietUsername.addTextChangedListener { viewModel.onAction(EnterUsername("$it".trim())) }
		tietPassword.addTextChangedListener { viewModel.onAction(EnterPassword("$it".trim())) }

		btnLogin.setOnClickListener { viewModel.onAction(Login) }
		btnRegister.setOnClickListener { navigateTo<RegisterActivity>() }
	}

	private fun subscribeToEvents() = using(viewModel) {
		collectLatestFlowWhenStarted(state) {
			binding.tilUsername.error2 = getFieldError(it.usernameError)
			binding.tilPassword.error2 = getFieldError(it.passwordError)
			binding.pbIsLoading.isVisible = it.isLoading
		}
		collectFlowWhenStarted(eventFlow) {
			when (it) {
				is OnLogin            -> navigateTo<MainActivity>()
				is OnShowErrorMessage -> showToast(it.error.asString(this@LoginActivity))
			}
		}
	}

	private fun getFieldError(error: Error?) = when (error) {
		is TextFieldError.Empty -> getString(R.string.error_cant_be_empty)
		else                    -> null
	}
}