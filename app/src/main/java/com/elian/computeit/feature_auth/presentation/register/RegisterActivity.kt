package com.elian.computeit.feature_auth.presentation.register

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
import com.elian.computeit.core.presentation.util.getUsernameErrorMessage
import com.elian.computeit.core.presentation.util.viewBinding
import com.elian.computeit.core.util.asString
import com.elian.computeit.core.util.using
import com.elian.computeit.databinding.ActivityRegisterBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.EnterConfirmPassword
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.EnterPassword
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.EnterUsername
import com.elian.computeit.feature_auth.presentation.register.RegisterAction.Register
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnRegister
import com.elian.computeit.feature_auth.presentation.register.RegisterEvent.OnShowErrorMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

	private val viewModel by viewModels<RegisterViewModel>()
	private val binding by viewBinding(ActivityRegisterBinding::inflate)


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(binding.root)

		subscribeToEvents()
		initializeUi()
	}

	override fun onBackPressed() {
		navigateTo<LoginActivity>()
	}


	private fun initializeUi() = using(binding) {
		tietUsername.allowMultilineAndDisableEnterNewLine()

		tietUsername.addTextChangedListener { viewModel.onAction(EnterUsername("$it".trim())) }
		tietPassword.addTextChangedListener { viewModel.onAction(EnterPassword("$it".trim())) }
		tietConfirmPassword.addTextChangedListener { viewModel.onAction(EnterConfirmPassword("$it".trim())) }

		btnRegister.setOnClickListener { viewModel.onAction(Register) }
	}

	private fun subscribeToEvents() = using(viewModel) {
		collectLatestFlowWhenStarted(state) {
			binding.tilUsername.error2 = getUsernameErrorMessage(this@RegisterActivity, it.usernameError)

			binding.tilPassword.error2 = when (val error = it.passwordError) {
				is TextFieldError.Empty    -> getString(R.string.Error_CantBeEmpty)
				is TextFieldError.TooShort -> getString(R.string.Error_TooShort).format(error.minLength)
				is TextFieldError.Invalid  -> getString(R.string.Error_PasswordInvalid).format(error.minCharacterCount, error.validCharacters)
				is TextFieldError.TooLong  -> getString(R.string.Error_TooLong).format(error.maxLength)
				else                       -> null
			}
			binding.tilConfirmPassword.error2 = when (it.confirmPasswordError) {
				is TextFieldError.Empty   -> getString(R.string.Error_CantBeEmpty)
				is TextFieldError.Invalid -> getString(R.string.Error_PasswordsDontMatch)
				else                      -> null
			}
			binding.pbIsLoading.isVisible = it.isLoading
		}
		collectFlowWhenStarted(eventFlow) {
			when (it) {
				is OnRegister         -> navigateTo<MainActivity>()
				is OnShowErrorMessage -> showToast(it.error.asString(this@RegisterActivity))
			}
		}
	}
}