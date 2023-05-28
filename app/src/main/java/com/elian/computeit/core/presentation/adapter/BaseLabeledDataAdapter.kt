package com.elian.computeit.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.core.presentation.util.simple_list_adapter.SimpleListAdapter

@Suppress("FunctionName")
fun <VB : ViewBinding> BaseLabeledDataAdapter(
	items: List<LabeledData>,
	inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	getLabel: VB.() -> TextView,
	getValue: VB.() -> TextView,
	beforeBind: (VB.() -> Unit)? = null,
	afterBind: (VB.() -> Unit)? = null,
) = SimpleListAdapter(
	inflate = inflate,
) { binding, item: LabeledData, _ ->

	binding.apply {
		beforeBind?.invoke(this)

		getLabel().text = root.context!!.getString(item.labelResId)
		getValue().text = "${item.value}"

		afterBind?.invoke(this)
	}
}.apply { submitList(items) }