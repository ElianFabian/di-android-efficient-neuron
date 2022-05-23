package com.elian.efficientneuron.ui.tips

import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.data.repository.TipsStaticRepository

class TipsInteractor(private val listener: TipsContract.OnInteractorListener) :
    TipsContract.Interactor,
    TipsContract.OnRepositoryCallback
{
    private val repository: TipsContract.Repository = TipsStaticRepository
    
    //region TipsContract.Interactor

    override fun getList()
    {
        repository.getList(this)
    }

    //endregion

    //region TipsContract.OnRepositoryCallback

    override fun onGetListSuccess(listFromRepository: List<Tip>)
    {
        listener.onGetListSuccess(listFromRepository)
    }

    override fun onGetListFailure()
    {
        listener.onGetListFailure()
    }

    //endregion
}
