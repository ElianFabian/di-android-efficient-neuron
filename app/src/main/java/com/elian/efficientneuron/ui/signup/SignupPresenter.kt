package com.elian.efficientneuron.ui.signup

import com.elian.efficientneuron.data.model.User

class SignupPresenter(private var view: SignupContract.View?) :
    SignupContract.Presenter,
    SignupContract.OnInteractorListener
{
    private var interactor: SignupContract.Interactor? = SignupInteractor(this)

    //region SignupListContract.Presenter

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    override fun signup(user: User, repeatedPassword: String)
    {
        if (interactor!!.validateFields(user, repeatedPassword)) return

        interactor?.signup(user)
    }

    override fun onPasswordsDontMatchError()
    {
        view?.setPasswordsDontMatchError()
    }

    override fun onEmailInvalidError()
    {
        view?.setEmailInvalidError()
    }

    override fun onPasswordInvalidError()
    {
        view?.setPasswordInvalidError()
    }

    //endregion

    //region SignupContract.OnInteractorListener

    override fun onSignupSuccess()
    {
        view?.onSignupSuccess()
    }

    override fun onSignupFailure()
    {
        view?.onSignupFailure()
    }

    //endregion
}