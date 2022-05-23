package com.elian.efficientneuron.ui.tips

import com.elian.efficientneuron.base.BasePresenter
import com.elian.efficientneuron.data.model.Tip


interface TipsContract
{
    interface View : OnRepositoryCallback

    interface Presenter : BasePresenter, Actions

    interface Interactor : Actions

    interface Repository
    {
        fun getList(callback: OnGetListCallback)
    }


    interface Actions
    {
        fun getList()
    }

    interface OnInteractorListener : OnRepositoryCallback

    interface OnRepositoryCallback :
        OnGetListCallback

    interface OnGetListCallback
    {
        fun onGetListSuccess(listFromRepository: List<Tip>)
        fun onGetListFailure()
    }
}
