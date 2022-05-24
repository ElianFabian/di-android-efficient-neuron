package com.elian.efficientneuron.ui.signup

import com.elian.efficientneuron.data.model.User

class SignUpPresenter(private var view: SignUpContract.View?) :
    SignUpContract.Presenter,
    SignUpContract.OnInteractorListener
{
    private var interactor: SignUpContract.Interactor? = SignUpInteractor(this)

    //region SignupListContract.Presenter

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    override fun signUp(user: User, repeatedPassword: String)
    {
        if (interactor!!.validateFields(user, repeatedPassword)) return

        interactor?.signUp(user)
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

    //region SignUpContract.OnInteractorListener

    override fun onSignUpSuccess()
    {
        view?.onSignUpSuccess()
    }

    override fun onSignUpFailure()
    {
        view?.onSignUpFailure()
    }

    //endregion
}