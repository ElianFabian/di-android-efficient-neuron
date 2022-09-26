package com.elian.computeit.data.repository

import com.elian.computeit.data.database.AppDatabase
import com.elian.computeit.data.model.Tip
import com.elian.computeit.view_model.TipManagerContract
import com.elian.computeit.view_model.TipListContract
import java.util.concurrent.Callable
import java.util.concurrent.Future

object TipRoomRepository :
    TipListContract.Repository,
    TipManagerContract.Repository
{
    private val tipDAO get() = AppDatabase.instance.tipDAO

    private fun execute(runnable: Runnable) = AppDatabase.executorService.execute(runnable) 

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
