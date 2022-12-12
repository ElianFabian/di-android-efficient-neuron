package com.elian.computeit.core.presentation.adapter

import com.elian.computeit.core.presentation.model.LabeledData
import com.elian.computeit.databinding.ItemMainLabeledDataBinding

@Suppress("FunctionName", "UNCHECKED_CAST")
fun MainLabeledDataAdapter(
	items: List<LabeledData>,
) = BaseLabeledDataAdapter(
	items = items,
	inflate = ItemMainLabeledDataBinding::inflate,
	getLabel = { tvLabel },
	getValue = { tvValue },
)