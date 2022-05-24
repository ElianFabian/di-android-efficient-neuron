package com.elian.efficientneuron.ui.login

import com.elian.efficientneuron.base.BasePresenter
import com.elian.efficientneuron.data.model.User

interface LogInContract
{
    interface View : OnRepositoryCallback
    {
        fun showProgress()
        fun hideProgress()

        fun setEmailEmptyError()
        fun setPasswordEmptyError()

        fun setInvalidCredentialsError()
    }

    interface Presenter : BasePresenter, Actions

    interface Interactor : Actions
    {
        /**
         * Returns true if there's any error.
         */
        fun validateUser(user: User): Boolean
    }

    interface Repository
    {
        fun logIn(callback: OnLogInCallback, user: User)
    }


    interface Actions
    {
        fun logIn(user: User)
    }

    interface OnInteractorListener : OnRepositoryCallback
    {
        fun onEmailEmptyError()
        fun onPasswordEmptyError()
    }

    interface OnRepositoryCallback :
        OnLogInCallback

    interface OnLogInCallback
    {
        fun onLogInSuccess()
        fun onLogInFailure()
    }
}