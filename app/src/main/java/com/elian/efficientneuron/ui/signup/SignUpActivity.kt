package com.elian.efficientneuron.ui.signup

import android.content.Intent
import android.os.Bundle
import com.elian.efficientneuron.base.BaseActivity
import com.elian.efficientneuron.data.model.User
import com.elian.efficientneuron.databinding.ActivitySignupBinding
import com.elian.efficientneuron.ui.login.LogInActivity
import com.elian.efficientneuron.util.extension.toast

class SignUpActivity : BaseActivity(),
    SignUpContract.View
{
    private lateinit var binding: ActivitySignupBinding

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

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = SignUpPresenter(this)

        initUI()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        binding.btnSignup.setOnClickListener()
        {
            presenter.signUp(userFromFields, binding.tieRepeatedPassword.text.toString())
        }
    }

    private fun goToLoginActivity()
    {
        val intent = Intent(this, LogInActivity::class.java)
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