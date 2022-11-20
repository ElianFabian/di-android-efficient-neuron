package com.elian.computeit.feature_tips.presentation.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.computeit.R
import com.elian.computeit.core.presentation.util.extensions.toast
import com.elian.computeit.core.util.RecyclerViewAdapter
import com.elian.computeit.databinding.FragmentTipsBinding
import com.elian.computeit.databinding.ItemTipBinding
import com.elian.computeit.feature_tips.data.models.Tip

class TipsFragment : Fragment(),
    RecyclerViewAdapter.OnBindViewHolderListener<Tip>,
    RecyclerViewAdapter.OnItemClickListener<Tip>,
    RecyclerViewAdapter.OnItemLongClickListener<Tip>
{
    private lateinit var binding: FragmentTipsBinding
    private val tipAdapter = RecyclerViewAdapter<Tip>(itemLayout = R.layout.item_tip)


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


    private fun initUI()
    {
        initRecyclerViewAdapter()
    }

    private fun initRecyclerViewAdapter()
    {
        binding.rvTips.adapter = tipAdapter
        binding.rvTips.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        tipAdapter.setOnBindViewHolderListener(this)
        tipAdapter.setOnItemClickListener(this)
        tipAdapter.setOnItemLongClickListener(this)
    }

    override fun onBindViewHolder(view: View, item: Tip, position: Int)
    {
        ItemTipBinding.bind(view).apply()
        {
            tvTitle.text = item.title
            tvExample.text = item.example
        }
    }

    override fun onItemClick(v: View?, selectedItem: Tip, position: Int)
    {
        // TODO:
    }

    override fun onItemLongClick(v: View?, selectedItem: Tip, position: Int): Boolean
    {
        toast("You long clicked a tip", Toast.LENGTH_SHORT)

        return true
    }
}