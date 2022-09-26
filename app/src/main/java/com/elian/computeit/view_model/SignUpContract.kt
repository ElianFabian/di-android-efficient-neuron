package com.elian.computeit.view_model

import com.elian.computeit.base.BasePresenter
import com.elian.computeit.data.model.User

interface SignUpContract
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
        fun signUp(user: User, repeatedPassword: String)
    }

    interface Interactor
    {
        fun signUp(user: User)

        /**
         * Returns true if there are any error.
         */
        fun validateFields(user: User, repeatedPassword: String): Boolean
    }

    interface Repository
    {
        fun signUp(callback: OnSignUpCallback, user: User)
    }


    interface OnInteractorListener : OnRepositoryCallback
    {
        fun onPasswordsDontMatchError()

        fun onEmailInvalidError()
        fun onPasswordInvalidError()
    }

    interface OnRepositoryCallback :
        OnSignUpCallback

    interface OnSignUpCallback
    {
        fun onSignUpSuccess()
        fun onSignUpFailure()
    }
}