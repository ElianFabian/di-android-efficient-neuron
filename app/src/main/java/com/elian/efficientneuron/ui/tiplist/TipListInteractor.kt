package com.elian.efficientneuron.ui.tiplist

import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.data.repository.TipRoomRepository
import com.elian.efficientneuron.data.repository.TipStaticRepository

class TipListInteractor(private val listener: TipListContract.OnInteractorListener) :
    TipListContract.Interactor,
    TipListContract.OnRepositoryCallback
{
    private val repository: TipListContract.Repository = TipRoomRepository
    
    //region TipListContract.Interactor

    override fun getList()
    {
        repository.getList(this)
    }

    //endregion

    //region TipListContract.OnRepositoryCallback

    override fun onGetListSuccess(listFromRepository: List<Tip>)
    {
        listener.onGetListSuccess(listFromRepository)
    }

    override fun onGetListFailure()
    {
        listener.onGetListFailure()
    }

    override fun onNoData()
    {
        listener.onNoData()
    }

    //endregion
}
