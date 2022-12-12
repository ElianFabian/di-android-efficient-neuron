package com.elian.computeit.core.presentation.adapter

import android.view.ViewGroup
import com.elian.computeit.R
import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.core.util.extensions.apply2
import com.elian.computeit.databinding.ItemLabeledDataBinding


private fun bindBaseStyle(binding: ItemLabeledDataBinding) = binding.apply2()
{
	tvLabel.setTextAppearance(R.style.TextAppearance_Label_Main)
	tvValue.setTextAppearance(R.style.TextAppearance_Value_Main)
	root.layoutParams = ViewGroup.MarginLayoutParams(root.layoutParams).apply()
	{
		topMargin = 25
		bottomMargin = 25
	}
}

@Suppress("FunctionName", "UNCHECKED_CAST")
fun LabeledDataAdapter(
	list: List<LabeledData>,
	setStyle: ItemLabeledDataBinding.() -> Unit = ::bindBaseStyle,
	beforeBind: (ItemLabeledDataBinding.() -> Unit)? = null,
	afterBind: (ItemLabeledDataBinding.() -> Unit)? = null,
) = GenericAdapter(
	inflate = ItemLabeledDataBinding::inflate,
	list = list,
) {
	beforeBind?.invoke(this)

	tvLabel.text = root.context!!.getString(it.labelResId)
	tvValue.text = "${it.value}"

	afterBind?.invoke(this)

	setStyle(this)
}