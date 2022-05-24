package com.elian.efficientneuron.ui.signup

import com.elian.efficientneuron.base.BasePresenter
import com.elian.efficientneuron.data.model.User

interface SignupContract
{
    interface View : OnRepositoryCallback
    {
        fun showProgress()
        fun hideProgress()

        fun setPasswordsDontMatchError()

        fun setEmailInvalidError()
        fun setPasswordInvalidError()
    }

    interface Presenter : BasePresenter
    {
        fun signup(user: User, repeatedPassword: String)
    }

    interface Interactor
    {
        fun signup(user: User)

        /**
         * @Returns true if there are any error.
         */
        fun validateFields(user: User, repeatedPassword: String): Boolean
    }

    interface Repository
    {
        fun signup(callback: OnSignupCallback, user: User)
    }


    interface OnInteractorListener : OnRepositoryCallback
    {
        fun onPasswordsDontMatchError()

        fun onEmailInvalidError()
        fun onPasswordInvalidError()
    }

    interface OnRepositoryCallback :
        OnSignupCallback

    interface OnSignupCallback
    {
        fun onSignupSuccess()
        fun onSignupFailure()
    }
}