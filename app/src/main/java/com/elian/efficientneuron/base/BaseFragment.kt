package com.elian.efficientneuron.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment()
{
    internal abstract val presenter: BasePresenter

    override fun onDestroy()
    {
        super.onDestroy()

        presenter.onDestroy()
    }
}