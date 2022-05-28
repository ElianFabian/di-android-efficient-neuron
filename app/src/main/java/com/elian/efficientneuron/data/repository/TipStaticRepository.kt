package com.elian.efficientneuron.data.repository

import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.ui.tipmanager.TipManagerContract
import com.elian.efficientneuron.ui.tiplist.TipListContract

object TipStaticRepository :
    TipListContract.Repository,
    TipManagerContract.Repository
{
    private val tipList = arrayListOf(
        Tip(title = "Squares of numbers ending in 5",
            example = "35² = (3·4)25 = 1125"),
        Tip(title = "Squares of numbers ending in 5",
            example = "35² = (3·4)25 = 1125")
    )

    override fun getList(callback: TipListContract.OnGetListCallback)
    {
        callback.onGetListSuccess(tipList)
    }

    override fun add(callback: TipManagerContract.OnAddCallback, tip: Tip)
    {
        tipList.add(tip)
        callback.onAddSuccess()
    }

    override fun edit(callback: TipManagerContract.OnEditCallback, editedTip: Tip, position: Int)
    {
        tipList[position] = editedTip
        callback.onEditSuccess()
    }
}
