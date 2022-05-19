package com.elian.efficientneuron.ui.tips

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentTipsBinding
import com.elian.efficientneuron.databinding.ItemTipBinding
import com.elian.efficientneuron.model.Tip
import com.elian.efficientneuron.util.RecyclerViewAdapter
import com.elian.efficientneuron.util.extension.toast

class TipsFragment : Fragment(),
    RecyclerViewAdapter.OnBindViewHolderListener<Tip>,
    RecyclerViewAdapter.OnItemClickListener<Tip>,
    RecyclerViewAdapter.OnItemLongClickListener<Tip>
{
    private lateinit var binding: FragmentTipsBinding
    private lateinit var tipAdapter: RecyclerViewAdapter<Tip>

    //region Fragment Methods

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
        initAdapter()
    }

    private fun initAdapter()
    {
        tipAdapter = RecyclerViewAdapter(
            itemLayout = R.layout.item_tip,
            list = arrayListOf(
                Tip(id = 1,
                    title = "Squares of numbers ending in 5",
                    example = "35² = (3·4)25 = 1125"),
                Tip(id = 2,
                    title = "Squares of numbers ending in 5",
                    example = "35² = (3·4)25 = 1125")
            )
        )

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
}