package com.elian.efficientneuron.data.repository

import com.elian.efficientneuron.data.database.AppDatabase
import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.ui.tipmanager.TipManagerContract
import com.elian.efficientneuron.ui.tiplist.TipListContract
import java.util.concurrent.Callable
import java.util.concurrent.Future

object TipRoomRepository :
    TipListContract.Repository,
    TipManagerContract.Repository
{
    private val tipDAO get() = AppDatabase.instance.tipDAO

    private val execute = AppDatabase.executorService::execute

    private fun <T> submit(callable: Callable<T>): Future<T>
    {
        return AppDatabase.executorService.submit(callable)
    }

    override fun getList(callback: TipListContract.OnGetListCallback)
    {
        val list = submit { tipDAO.selectAll() }.get()

        if (list.isEmpty())
        {
            callback.onNoData()
        }
        else callback.onGetListSuccess(list)
    }

    override fun add(callback: TipManagerContract.OnAddCallback, tip: Tip)
    {
        execute { tipDAO.insert(tip) }

        callback.onAddSuccess()
    }

    override fun edit(callback: TipManagerContract.OnEditCallback, editedTip: Tip, position: Int)
    {
        execute { tipDAO.update(editedTip) }

        callback.onEditSuccess()
    }
}
