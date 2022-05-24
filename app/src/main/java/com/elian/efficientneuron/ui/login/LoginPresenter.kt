package com.elian.efficientneuron.ui.login

import com.elian.efficientneuron.data.model.User

class LoginPresenter(private var view: LoginContract.View?) :
    LoginContract.Presenter,
    LoginContract.OnInteractorListener
{
    private var interactor: LoginContract.Interactor? = LoginInteractor(this)

    //region LoginListContract.Presenter

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    override fun login(user: User)
    {
        if (interactor!!.validateUser(user)) return

        interactor?.login(user)
    }

    //endregion

    //region LoginContract.OnInteractorListener

    override fun onEmailEmptyError()
    {
        view?.setEmailEmptyError()
    }

    override fun onPasswordEmptyError()
    {
        view?.setPasswordEmptyError()
    }

    override fun onLoginSuccess()
    {
        view?.onLoginSuccess()
    }

    override fun onLoginFailure()
    {
        view?.onLoginFailure()
    }

    //endregion
}
