package com.elian.efficientneuron.ui.login

import com.elian.efficientneuron.data.model.User
import com.elian.efficientneuron.data.repository.FirebaseRepository

class LogInInteractor(private val listener: LogInContract.OnInteractorListener) :
    LogInContract.Interactor,
    LogInContract.OnRepositoryCallback
{
    private val repository: LogInContract.Repository = FirebaseRepository

    //region LogInContract.Interactor

    override fun validateUser(user: User): Boolean = when
    {
        user.email.isEmpty()    -> listener.onEmailEmptyError().run { true }
        user.password.isEmpty() -> listener.onPasswordEmptyError().run { true }

        else                    -> false
    }

    override fun logIn(user: User)
    {
        repository.logIn(this, user)
    }

    //endregion

    //region LogInContract.OnRepositoryCallback

    override fun onLogInSuccess()
    {
        listener.onLogInSuccess()
    }

    override fun onLogInFailure()
    {
        listener.onLogInFailure()
    }

    //endregion
}
