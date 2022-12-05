package com.elian.computeit.core.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericAdapter<ItemT : Any, VB : ViewBinding>(
	private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	list: List<ItemT> = emptyList(),
	areItemsTheSame: ((oldItem: ItemT, newItem: ItemT) -> Boolean)? = null,
	areContentsTheSame: ((oldItem: ItemT, newItem: ItemT) -> Boolean)? = null,
	private val onBind: VB.(item: ItemT) -> Unit,
) : ListAdapter<ItemT, GenericAdapter<ItemT, VB>.ViewHolder>(
	object : DiffUtil.ItemCallback<ItemT>()
	{
		override fun areItemsTheSame(oldItem: ItemT, newItem: ItemT) = areItemsTheSame?.invoke(oldItem, newItem) ?: (oldItem == newItem)

		@SuppressLint("DiffUtilEquals")
		override fun areContentsTheSame(oldItem: ItemT, newItem: ItemT) = areContentsTheSame?.invoke(oldItem, newItem) ?: (oldItem == newItem)
	}
)
{
	init
	{
		submitList(list)
	}

	inner class ViewHolder(view: View, val binding: VB) : RecyclerView.ViewHolder(view)


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		val inflater = LayoutInflater.from(parent.context)

		val binding = inflate(inflater, parent, false)

		return ViewHolder(binding.root, binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		val item = getItem(position)

		onBind(holder.binding, item)
	}
}