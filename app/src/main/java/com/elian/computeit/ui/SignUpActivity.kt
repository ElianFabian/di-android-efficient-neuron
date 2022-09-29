package com.elian.computeit.ui

import android.content.Intent
import android.os.Bundle
import com.elian.computeit.base.BaseActivity
import com.elian.computeit.data.model.User
import com.elian.computeit.databinding.ActivityRegisterBinding
import com.elian.computeit.feature_auth.presentation.login.LoginActivity
import com.elian.computeit.view_model.SignUpContract
import com.elian.computeit.view_model.SignUpPresenter
import com.elian.computeit.util.extension.toast

class SignUpActivity : BaseActivity(),
    SignUpContract.View
{
    private lateinit var binding: ActivityRegisterBinding

    override lateinit var presenter: SignUpContract.Presenter

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

        presenter = SignUpPresenter(this)

        initUI()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        binding.btnRegister.setOnClickListener()
        {
            presenter.signUp(userFromFields, binding.tieConfirmPassword.text.toString())
        }
    }

    private fun goToLoginActivity()
    {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        onDestroy()
    }

    //endregion

    //region SignUpContract.View

    override fun showProgress()
    {
        
    }

    override fun hideProgress()
    {
        
    }

    override fun setPasswordsDontMatchError()
    {
        toast("Passwords don't match")
    }

    override fun setEmailInvalidError()
    {
        toast("Email is invalid")
    }

    override fun setPasswordInvalidError()
    {
        toast("setPasswordInvalidError")
    }

    override fun onSignUpSuccess()
    {
        goToLoginActivity()
    }

    override fun onSignUpFailure()
    {
        toast("Signup failed")
    }

    //endregion
}