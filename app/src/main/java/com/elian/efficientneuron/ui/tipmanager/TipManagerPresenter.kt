package com.elian.efficientneuron.ui.tipmanager

import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.util.extension.toast

class TipManagerPresenter(private var view: TipManagerContract.View?) :
    TipManagerContract.Presenter,
    TipManagerContract.OnInteractorListener
{
    private var interactor: TipManagerContract.Interactor? = TipManagerInteractor(this)

    //region TipManagerListContract.Presenter

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    override fun add(tip: Tip)
    {
        if (interactor!!.validateFields(tip))
        {
            view?.onAddFailure()
        }
        else interactor?.add(tip)
    }

    override fun edit(editedTip: Tip, position: Int)
    {
        if (interactor!!.validateFields(editedTip))
        {
            view?.onEditFailure()
        }
        else interactor?.edit(editedTip, position)
    }

    //endregion

    //region TipManagerContract.OnInteractorListener

    override fun onTitleEmptyError()
    {
        view?.setTitleEmptyError()
    }

    override fun onExampleEmptyError()
    {
        view?.setExampleEmptyError()
    }

    override fun onAddSuccess()
    {
        view?.onAddSuccess()
    }

    override fun onAddFailure()
    {
        view?.onAddFailure()
    }

    override fun onEditSuccess()
    {
        view?.onEditSuccess()
    }

    override fun onEditFailure()
    {
        view?.onEditFailure()
    }

    //endregion
}
