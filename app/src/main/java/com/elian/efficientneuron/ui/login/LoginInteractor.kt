package com.elian.efficientneuron.ui.login

import com.elian.efficientneuron.data.model.User
import com.elian.efficientneuron.data.repository.FirebaseRepository

class LoginInteractor(private val listener: LoginContract.OnInteractorListener) :
    LoginContract.Interactor,
    LoginContract.OnRepositoryCallback
{
    private val repository: LoginContract.Repository = FirebaseRepository

    //region LoginContract.Interactor

    override fun validateUser(user: User): Boolean = when
    {
        user.email.isEmpty()    -> listener.onEmailEmptyError().run { true }
        user.password.isEmpty() -> listener.onPasswordEmptyError().run { true }

        else                    -> false
    }

    override fun login(user: User)
    {
        repository.login(this, user)
    }

    //endregion

    //region LoginContract.OnRepositoryCallback

    override fun onLoginSuccess()
    {
        listener.onLoginSuccess()
    }

    override fun onLoginFailure()
    {
        listener.onLoginFailure()
    }

    //endregion
}
