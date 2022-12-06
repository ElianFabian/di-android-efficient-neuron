package com.elian.computeit.core.presentation.adapter

import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.databinding.ItemLabeledDataBinding

@Suppress("FunctionName", "UNCHECKED_CAST")
fun LabeledDataAdapter(list: List<LabeledData>) = GenericAdapter(
	inflate = ItemLabeledDataBinding::inflate,
	list = list,
) {
	tvLabel.text = it.label
	tvValue.text = it.value
}