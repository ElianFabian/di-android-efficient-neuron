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
	private val onBind: VB.(ItemT) -> Unit,
) : ListAdapter<ItemT, GenericAdapter<ItemT, VB>.ViewHolder>(
	object : DiffUtil.ItemCallback<ItemT>()
	{
		override fun areItemsTheSame(oldItem: ItemT, newItem: ItemT) = oldItem == newItem

		@SuppressLint("DiffUtilEquals")
		override fun areContentsTheSame(oldItem: ItemT, newItem: ItemT) = oldItem == newItem
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

//class GenericAdapter<ItemT : Any, VB : ViewBinding>(
//	@LayoutRes private val layoutRes: Int,
//	private val bind: (View) -> VB,
//	private val onBind: VB.(ItemT) -> Unit,
//) : ListAdapter<ItemT, GenericAdapter<ItemT, VB>.ViewHolder>(
//	object : DiffUtil.ItemCallback<ItemT>()
//	{
//		override fun areItemsTheSame(oldItem: ItemT, newItem: ItemT) = oldItem == newItem
//
//		@SuppressLint("DiffUtilEquals")
//		override fun areContentsTheSame(oldItem: ItemT, newItem: ItemT) = oldItem == newItem
//	}
//)
//{
//	inner class ViewHolder(view: View, val binding: VB) : RecyclerView.ViewHolder(view)
//
//
//	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
//	{
//		val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
//
//		val binding = bind(view)
//
//		return ViewHolder(binding.root, binding)
//	}
//
//	override fun onBindViewHolder(holder: ViewHolder, position: Int)
//	{
//		val item = getItem(position)
//
//		onBind(holder.binding, item)
//	}
//}