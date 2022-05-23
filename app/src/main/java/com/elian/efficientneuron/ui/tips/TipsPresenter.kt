package com.elian.efficientneuron.ui.tips

import com.elian.efficientneuron.data.model.Tip

class TipsPresenter(private var view: TipsContract.View?) :
    TipsContract.Presenter,
    TipsContract.OnInteractorListener
{
    private var interactor: TipsContract.Interactor? = TipsInteractor(this)

    //region TipListContract.IPresenter

    override fun onDestroy()
    {
        view = null
        interactor = null
    }

    override fun getList()
    {
        interactor?.getList()
    }

    //endregion
    
    //region TipsContract.OnRepositoryCallback

    override fun onGetListSuccess(listFromRepository: List<Tip>)
    {
        view?.onGetListSuccess(listFromRepository)
    }

    override fun onGetListFailure()
    {
        view?.onGetListFailure()
    }
    
    //endregion
}