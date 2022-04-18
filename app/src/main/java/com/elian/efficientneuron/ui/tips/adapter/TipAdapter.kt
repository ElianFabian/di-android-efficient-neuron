package com.elian.efficientneuron.ui.tips.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elian.efficientneuron.R
import com.elian.efficientneuron.databinding.ItemTipBinding
import com.elian.efficientneuron.model.Tip

class TipAdapter(
    private val tips: List<Tip>,
) :
    RecyclerView.Adapter<TipAdapter.TipViewHolder>()
{
    //region RecyclerView.Adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)

        return TipViewHolder(layoutInflater.inflate(R.layout.item_tip, parent, false))
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int)
    {
        val tipItem = tips[position]

        holder.render(tipItem)
    }

    override fun getItemCount(): Int = tips.size

    //endregion

    class TipViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private val binding = ItemTipBinding.bind(view)

        fun render(tip: Tip) = with(binding)
        {
            tvTitle.text = tip.title
            tvExample.text = tip.example
        }
    }
}