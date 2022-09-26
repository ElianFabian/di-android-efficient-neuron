package com.elian.computeit.view_model

import com.elian.computeit.data.model.Tip

class TipListPresenter(private var view: TipListContract.View?) :
    TipListContract.Presenter,
    TipListContract.OnInteractorListener
{
    private var interactor: TipListContract.Interactor? = TipListInteractor(this)

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
    
    //region TipListContract.OnRepositoryCallback

    override fun onGetListSuccess(listFromRepository: List<Tip>)
    {
        view?.onGetListSuccess(listFromRepository)
    }

    override fun onGetListFailure()
    {
        view?.onGetListFailure()
    }

    override fun onNoData()
    {
        view?.onNoData()
    }

    //endregion
}