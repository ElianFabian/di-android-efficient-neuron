package com.elian.computeit.core.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elian.computeit.R
import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.databinding.ItemLabeledDataBinding

class LabeledDataAdapter : ListAdapter<LabeledData, LabeledDataAdapter.ViewHolder>(callback)
{
	class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

	companion object
	{
		private val callback = object : DiffUtil.ItemCallback<LabeledData>()
		{
			override fun areItemsTheSame(oldItem: LabeledData, newItem: LabeledData) = oldItem == newItem

			override fun areContentsTheSame(oldItem: LabeledData, newItem: LabeledData) = oldItem == newItem
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_labeled_data, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		val item = getItem(position)

		val binding = ItemLabeledDataBinding.bind(holder.itemView)

		binding.tvLabel.text = item.label
		binding.tvValue.text = item.value
	}
}