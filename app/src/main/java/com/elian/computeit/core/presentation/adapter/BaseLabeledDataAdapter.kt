package com.elian.computeit.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.elian.computeit.core.presentation.model.LabeledData

@Suppress("FunctionName", "UNCHECKED_CAST")
fun <VB : ViewBinding> BaseLabeledDataAdapter(
	items: List<LabeledData>,
	inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	getLabel: VB.() -> TextView,
	getValue: VB.() -> TextView,
	beforeBind: (VB.() -> Unit)? = null,
	afterBind: (VB.() -> Unit)? = null,
) = GenericAdapter(
	inflate = inflate,
	items = items,
) {
	beforeBind?.invoke(this)

	getLabel().text = root.context!!.getString(it.labelResId)
	getValue().text = "${it.value}"

	afterBind?.invoke(this)
}