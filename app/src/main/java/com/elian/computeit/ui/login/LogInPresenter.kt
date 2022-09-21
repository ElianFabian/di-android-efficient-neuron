package com.elian.computeit.ui.login

import com.elian.computeit.data.model.User

class LogInPresenter(private var view: LogInContract.View?) :
    LogInContract.Presenter,
    LogInContract.OnInteractorListener
{
    private var interactor: LogInContract.Interactor? = LogInInteractor(this)

    //region LoginListContract.Presenter

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    override fun logIn(user: User)
    {
        if (interactor!!.validateUser(user)) return

        interactor?.logIn(user)
    }

    //endregion

    //region LogInContract.OnInteractorListener

    override fun onEmailEmptyError()
    {
        view?.setEmailEmptyError()
    }

    override fun onPasswordEmptyError()
    {
        view?.setPasswordEmptyError()
    }

    override fun onLogInSuccess()
    {
        view?.onLogInSuccess()
    }

    override fun onLogInFailure()
    {
        view?.onLogInFailure()
    }

    //endregion
}
