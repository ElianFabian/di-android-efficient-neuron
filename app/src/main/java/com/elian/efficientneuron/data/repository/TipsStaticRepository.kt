package com.elian.efficientneuron.data.repository

import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.ui.tips.TipsContract

object TipsStaticRepository :
    TipsContract.Repository
{
    private val tipLisst = arrayListOf(
        Tip(id = 1,
            title = "Squares of numbers ending in 5",
            example = "35² = (3·4)25 = 1125"),
        Tip(id = 2,
            title = "Squares of numbers ending in 5",
            example = "35² = (3·4)25 = 1125")
    )

    //region TaskListContract.Repository

    override fun getList(callback: TipsContract.OnGetListCallback)
    {
        callback.onGetListSuccess(tipLisst)
    }

    //endregion
}
