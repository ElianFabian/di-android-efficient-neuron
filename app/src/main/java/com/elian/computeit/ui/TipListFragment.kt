package com.elian.computeit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.computeit.R
import com.elian.computeit.base.BaseFragment
import com.elian.computeit.core.util.extensions.goToFragment
import com.elian.computeit.core.util.extensions.toast
import com.elian.computeit.data.model.Tip
import com.elian.computeit.databinding.FragmentTipListBinding
import com.elian.computeit.databinding.ItemTipBinding
import com.elian.computeit.util.RecyclerViewAdapter
import com.elian.computeit.view_model.TipListContract
import com.elian.computeit.view_model.TipListPresenter

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

        binding.fab.setOnClickListener()
        {
            activity?.goToFragment(TipManagerFragment())
        }

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
        activity?.goToFragment(TipManagerFragment(), bundleOf(
            getString(R.string.bundleKey_selectedTip) to tip,
            getString(R.string.bundleKey_selectedTip_position) to position
        ))

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

    override fun onNoData()
    {

    }

    //endregion
}