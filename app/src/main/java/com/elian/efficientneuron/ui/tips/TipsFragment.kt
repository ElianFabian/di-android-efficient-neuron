package com.elian.efficientneuron.ui.tips

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.FragmentTipsBinding
import com.elian.efficientneuron.databinding.ItemTipBinding
import com.elian.efficientneuron.model.Tip
import com.elian.efficientneuron.ui.tips.adapter.TipAdapter
import com.elian.efficientneuron.utils.RecyclerViewAdapter

class TipsFragment : Fragment(),
    RecyclerViewAdapter.RecyclerViewItemClickListener<Tip>
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
        tipAdapter = RecyclerViewAdapter(R.layout.item_tip, this)

        tipAdapter.load(listOf(
            Tip(id = 1,
                title = "Squares of numbers ending in 5",
                example = "35² = (3·4)25 = 1125"),
            Tip(id = 2,
                title = "Squares of numbers ending in 5",
                example = "35² = (3·4)25 = 1125")
        ))

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.rvTips.layoutManager = layoutManager
        binding.rvTips.adapter = tipAdapter
    }

    override fun onItemClick(v: View?, selectedItem: Tip, position: Int)
    {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(v: View?, selectedItem: Tip, position: Int): Boolean
    {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(view: View, item: Tip)
    {
        val binding = ItemTipBinding.bind(view)

        binding.tvTitle.text = item.title
        binding.tvExample.text = item.example
    }

    //endregion
}