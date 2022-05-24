package com.elian.efficientneuron.ui.login

import com.elian.efficientneuron.base.BasePresenter
import com.elian.efficientneuron.data.model.User

interface LoginContract
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
         * @Returns true if there's any error.
         */
        fun validateUser(user: User): Boolean
    }

    interface Repository
    {
        fun login(callback: OnLoginCallback, user: User)
    }


    interface Actions
    {
        fun login(user: User)
    }

    interface OnInteractorListener : OnRepositoryCallback
    {
        fun onEmailEmptyError()
        fun onPasswordEmptyError()
    }

    interface OnRepositoryCallback :
        OnLoginCallback

    interface OnLoginCallback
    {
        fun onLoginSuccess()
        fun onLoginFailure()
    }
}