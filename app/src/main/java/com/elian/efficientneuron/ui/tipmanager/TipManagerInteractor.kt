package com.elian.efficientneuron.ui.tipmanager

import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.data.repository.TipsStaticRepository

class TipManagerInteractor(private val listener: TipManagerContract.OnInteractorListener) :
    TipManagerContract.Interactor,
    TipManagerContract.OnRepositoryCallback
{
    private val repository: TipManagerContract.Repository = TipsStaticRepository
    
    //region TipManagerContract.Interactor

    override fun validateFields(tip: Tip): Boolean = when
    {
        tip.title.isEmpty() -> listener.onTitleEmptyError().run { true }
        tip.example.isEmpty() -> listener.onExampleEmptyError().run { true }
        
        else -> false
    }

    override fun add(tip: Tip)
    {
        repository.add(this, tip)
    }

    override fun edit(editedTip: Tip, position: Int)
    {
        repository.edit(this, editedTip, position)
    }

    //endregion

    //region TipManagerContract.OnRepositoryCallback

    override fun onAddSuccess()
    {
        listener.onAddSuccess()
    }

    override fun onAddFailure()
    {
        listener.onAddFailure()
    }

    override fun onEditSuccess()
    {
        listener.onEditSuccess()
    }

    override fun onEditFailure()
    {
        listener.onEditFailure()
    }
    //endregion
}
