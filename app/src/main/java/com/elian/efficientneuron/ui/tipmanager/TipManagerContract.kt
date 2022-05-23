package com.elian.efficientneuron.ui.tipmanager

import com.elian.efficientneuron.base.BasePresenter
import com.elian.efficientneuron.data.model.Tip

interface TipManagerContract
{
    interface View : OnRepositoryCallback
    {
        fun setTitleEmptyError()
        fun setExampleEmptyError()

        fun cleanInputFieldsErrors()
    }

    interface Presenter : BasePresenter, Actions

    interface Interactor : Actions
    {
        /**
         * Validates the fields from the view given in a Task.
         *
         * @return true if there's no error.
         */
        fun validateFields(tip: Tip): Boolean
    }

    interface Repository
    {
        fun add(callback: OnAddCallback, tip: Tip)
        fun edit(callback: OnEditCallback, editedTip: Tip, position: Int)
    }


    interface Actions
    {
        fun add(tip: Tip)
        fun edit(editedTip: Tip, position: Int)
    }

    interface OnInteractorListener : OnRepositoryCallback
    {
        fun onTitleEmptyError()
        fun onExampleEmptyError()
    }

    interface OnRepositoryCallback :
        OnAddCallback,
        OnEditCallback

    interface OnAddCallback
    {
        fun onAddSuccess()
        fun onAddFailure()
    }

    interface OnEditCallback
    {
        fun onEditSuccess()
        fun onEditFailure()
    }
}
