package com.elian.computeit.core.presentation.util.simple_list_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class SimpleSingleItemListAdapter<VB : ViewBinding, TItem : Any>(
	private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	areItemsTheSame: (oldItem: TItem, newItem: TItem) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	areContentsTheSame: (oldItem: TItem, newItem: TItem) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	private inline val onBind: SimpleSingleItemListAdapter<VB, TItem>.(
		binding: VB,
		item: TItem,
		position: Int,
	) -> Unit,
) : ListAdapter<TItem, SimpleSingleItemListAdapter<VB, TItem>.ViewHolder>(
	object : DiffUtil.ItemCallback<TItem>() {
		override fun areItemsTheSame(oldItem: TItem, newItem: TItem) = areItemsTheSame(oldItem, newItem)

		override fun areContentsTheSame(oldItem: TItem, newItem: TItem) = areContentsTheSame(oldItem, newItem)
	}
) {
	inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)


	fun SimpleSingleItemListAdapter<VB, TItem>.getItem(position: Int): TItem = getItem(position)
	fun SimpleSingleItemListAdapter<VB, TItem>.getItemOrNull(position: Int): TItem? = kotlin.runCatching { getItem(position) }.getOrNull()

	inline fun <T> RecyclerView.setAdapterOrSubmitList(list: List<T>, getAdapter: () -> ListAdapter<T, *>) {
		val listAdapter = (adapter as? ListAdapter<T, RecyclerView.ViewHolder>) ?: getAdapter()

		if (adapter == null) adapter = listAdapter

		listAdapter.submitList(list)
	}


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)

		val binding = inflate(inflater, parent, false)

		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		onBind(holder.binding, getItem(position), position)
	}
}

@Suppress("FunctionName")
fun <VB : ViewBinding, TItem : Any> SimpleListAdapter(
	inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	areItemsTheSame: (oldItem: TItem, newItem: TItem) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	areContentsTheSame: (oldItem: TItem, newItem: TItem) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	onBind: SimpleSingleItemListAdapter<VB, TItem>.(
		binding: VB,
		item: TItem,
		position: Int,
	) -> Unit,
): ListAdapter<TItem, out RecyclerView.ViewHolder> = SimpleSingleItemListAdapter(
	inflate = inflate,
	areItemsTheSame = areItemsTheSame,
	areContentsTheSame = areContentsTheSame,
) { binding, item, position ->

	onBind(binding, item, position)
}

@Suppress("FunctionName")
fun <
	VB : ViewBinding,
	A : Any,
	B : Any,
	TItem : Pair<A, B>,
	> SimpleListAdapter(
	inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	areItemsTheSame: (oldItem: TItem, newItem: TItem) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	areContentsTheSame: (oldItem: TItem, newItem: TItem) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	onBind: SimpleSingleItemListAdapter<VB, TItem>.(
		binding: VB,
		A, B,
		position: Int,
	) -> Unit,
): ListAdapter<TItem, out RecyclerView.ViewHolder> = SimpleSingleItemListAdapter(
	inflate = inflate,
	areItemsTheSame = areItemsTheSame,
	areContentsTheSame = areContentsTheSame,
) { binding, item, position ->

	onBind(binding, item.first, item.second, position)
}