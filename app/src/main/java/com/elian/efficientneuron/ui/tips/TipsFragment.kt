package com.elian.efficientneuron.ui.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.efficientneuron.R
import com.elian.efficientneuron.base.BaseFragment
import com.elian.efficientneuron.databinding.FragmentTipsBinding
import com.elian.efficientneuron.databinding.ItemTipBinding
import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.util.RecyclerViewAdapter
import com.elian.efficientneuron.util.extension.toast

class TipsFragment : BaseFragment(),
    TipsContract.View,
    RecyclerViewAdapter.OnBindViewHolderListener<Tip>,
    RecyclerViewAdapter.OnItemClickListener<Tip>,
    RecyclerViewAdapter.OnItemLongClickListener<Tip>
{
    private lateinit var binding: FragmentTipsBinding
    private val tipAdapter = RecyclerViewAdapter<Tip>(itemLayout = R.layout.item_tip)
    override lateinit var presenter: TipsContract.Presenter

    //region Fragment Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        
        presenter = TipsPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTipsBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart()
    {
        super.onStart()

        initUI()
    }

    //endregion

    //region Methods

    private fun initUI()
    {
        initRecyclerViewAdapter()
        
        presenter.getList()
    }

    private fun initRecyclerViewAdapter()
    {
        binding.rvTips.adapter = tipAdapter
        binding.rvTips.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        tipAdapter.setOnBindViewHolderListener(this)
        tipAdapter.setOnItemClickListener(this)
        tipAdapter.setOnItemLongClickListener(this)
    }

    //endregion

    //region RecyclerViewAdapter.OnBindViewHolderListener<>

    override fun onBindViewHolder(view: View, item: Tip, position: Int)
    {
        ItemTipBinding.bind(view).apply()
        {
            tvTitle.text = item.title
            tvExample.text = item.example
        }
    }

    //endregion

    //region RecyclerViewAdapter.OnItemClickListener<>

    override fun onItemClick(v: View?, selectedItem: Tip, position: Int)
    {
        toast("You clicked a tip")
    }

    //endregion

    //region RecyclerViewAdapter.OnItemLongClickListener<>

    override fun onItemLongClick(v: View?, selectedItem: Tip, position: Int): Boolean
    {
        toast("You long clicked a tip", Toast.LENGTH_SHORT)

        return true
    }

    //endregion

    //region TipsContract.View

    override fun onGetListSuccess(listFromRepository: List<Tip>)
    {
        tipAdapter.replaceList(listFromRepository)
    }

    override fun onGetListFailure()
    {
    }

    //endregion
}