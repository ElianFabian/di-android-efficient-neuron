package com.elian.computeit.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.elian.computeit.MainActivity
import com.elian.computeit.R
import com.elian.computeit.base.BaseActivity
import com.elian.computeit.data.model.User
import com.elian.computeit.databinding.ActivityLoginBinding
import com.elian.computeit.ui.signup.SignUpActivity
import com.elian.computeit.util.extension.NotificationUtil
import com.elian.computeit.util.extension.toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : BaseActivity(),
    LogInContract.View
{
    private lateinit var binding: ActivityLoginBinding

    override lateinit var presenter: LogInContract.Presenter

    private val notificationUtil = NotificationUtil(this, "loginId", "loginName")

    private val userFromFields: User
        get() = User(
            email = binding.tieEmail.text.toString(),
            password = binding.tiePassword.text.toString()
        )

    //region Activity Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        logInIfUserExists()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LogInPresenter(this)

        initUI()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        notificationUtil.createNotification()
        {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(getString(R.string.actLogin_notification_contentTitle))
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        notificationUtil.createNotificationChannel()
        {
            enableLights(true)
            lightColor = Color.BLUE
        }

        binding.btnLogin.setOnClickListener()
        {
            presenter.logIn(userFromFields)
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
        finish()
    }

    private fun goToSignupActivity()
    {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun logInIfUserExists()
    {
        Firebase.auth.addAuthStateListener { auth ->

            if (auth.currentUser != null)
            {
                onLogInSuccess()
            }
        }
    }

    //endregion

    //region LogInContract.View

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

    override fun onLogInSuccess()
    {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)

        val disableNotifications = sp.getBoolean("disable_notifications", false)

        if (!disableNotifications)
        {
            notificationUtil.showNotification()
        }

        goToMainActivity()
    }

    override fun onLogInFailure()
    {

    }

    //endregion
}