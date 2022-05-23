package com.elian.efficientneuron.ui.tiplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.efficientneuron.R
import com.elian.efficientneuron.base.BaseFragment
import com.elian.efficientneuron.databinding.ItemTipBinding
import com.elian.efficientneuron.data.model.Tip
import com.elian.efficientneuron.databinding.FragmentTipListBinding
import com.elian.efficientneuron.ui.tipmanager.TipManagerFragment
import com.elian.efficientneuron.util.RecyclerViewAdapter
import com.elian.efficientneuron.util.extension.goToFragment
import com.elian.efficientneuron.util.extension.toast

class TipListFragment : BaseFragment(),
    TipListContract.View,
    RecyclerViewAdapter.OnBindViewHolderListener<Tip>,
    RecyclerViewAdapter.OnItemClickListener<Tip>,
    RecyclerViewAdapter.OnItemLongClickListener<Tip>
{
    private lateinit var binding: FragmentTipListBinding
    private val tipAdapter = RecyclerViewAdapter<Tip>(itemLayout = R.layout.item_tip)
    override lateinit var presenter: TipListContract.Presenter

    //region Fragment Methods

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        
        presenter = TipListPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View
    {
        binding = FragmentTipListBinding.inflate(inflater)
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

    private fun sendSelectedTip_To_TaskEditFragment(tip: Tip, position: Int)
    {
        activity?.goToFragment(TipManagerFragment(), Bundle().apply()
        {
            putSerializable(getString(R.string.bundleKey_selectedTip), tip)
            putInt(getString(R.string.bundleKey_selectedTip_position), position)
        })
        
        activity?.supportFragmentManager
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
        sendSelectedTip_To_TaskEditFragment(selectedItem, position)
    }

    //endregion

    //region RecyclerViewAdapter.OnItemLongClickListener<>

    override fun onItemLongClick(v: View?, selectedItem: Tip, position: Int): Boolean
    {
        toast("You long clicked a tip", Toast.LENGTH_SHORT)

        return true
    }

    //endregion

    //region TipListContract.View

    override fun onGetListSuccess(listFromRepository: List<Tip>)
    {
        tipAdapter.replaceList(listFromRepository)
    }

    override fun onGetListFailure()
    {
    }

    //endregion
}