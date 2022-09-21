package com.elian.computeit.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity()
{
    internal abstract val presenter: BasePresenter

    override fun onDestroy()
    {
        super.onDestroy()

        presenter.onDestroy()
    }
}