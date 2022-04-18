package com.elian.efficientneuron.ui.tips

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elian.efficientneuron.databinding.FragmentTipsBinding
import com.elian.efficientneuron.ui.tips.adapter.TipAdapter

class TipsFragment : Fragment()
{
    private lateinit var binding: FragmentTipsBinding

    private lateinit var adapter: TipAdapter

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
        adapter = TipAdapter(listOf())
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.rvTips.layoutManager = layoutManager
        binding.rvTips.adapter = adapter
    }

    //endregion

}