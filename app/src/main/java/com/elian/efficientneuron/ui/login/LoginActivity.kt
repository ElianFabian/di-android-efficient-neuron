package com.elian.efficientneuron.ui.login

import android.content.Intent
import android.os.Bundle
import com.elian.efficientneuron.MainActivity
import com.elian.efficientneuron.R
import com.elian.efficientneuron.base.BaseActivity
import com.elian.efficientneuron.data.model.User
import com.elian.efficientneuron.databinding.ActivityLoginBinding
import com.elian.efficientneuron.ui.signup.SignupActivity
import com.elian.efficientneuron.util.extension.toast

class LoginActivity : BaseActivity(),
    LoginContract.View
{
    private lateinit var binding: ActivityLoginBinding

    override lateinit var presenter: LoginContract.Presenter

    private val userFromFields: User
        get() = User(
            email = binding.tieEmail.text.toString(),
            password = binding.tiePassword.text.toString()
        )

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter(this)

        initUI()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        binding.btnLogin.setOnClickListener()
        {
            presenter.login(userFromFields)
        }
        binding.btnSignup.setOnClickListener()
        {
            goToSignupActivity()
        }
    }

    private fun goToMainActivity()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        onDestroy()
    }

    private fun goToSignupActivity()
    {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
        onDestroy()
    }

    //endregion

    //region LoginContract.View

    override fun showProgress()
    {

    }

    override fun hideProgress()
    {

    }

    override fun setEmailEmptyError()
    {
        binding.tieEmail.error = getString(R.string.error_email_empty)
    }

    override fun setPasswordEmptyError()
    {
        binding.tiePassword.error = getString(R.string.error_password_empty)
    }

    override fun setInvalidCredentialsError()
    {
        toast(R.string.error_invalid_credentials)
    }

    override fun onLoginSuccess()
    {
        goToMainActivity()
    }

    override fun onLoginFailure()
    {

    }

    //endregion
}