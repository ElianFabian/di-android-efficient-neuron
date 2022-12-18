package com.elian.computeit.feature_tests.presentation.test_details.adapter

import com.elian.computeit.core.presentation.adapter.GenericAdapter
import com.elian.computeit.databinding.ItemFailedOperationBinding
import com.elian.computeit.feature_tests.domain.model.OperationInfo

@Suppress("FunctionName", "UNCHECKED_CAST")
fun FailedOperationAdapter(items: List<OperationInfo>) = GenericAdapter(
	inflate = ItemFailedOperationBinding::inflate,
	items = items,
) {
	tvFirstNumber.text = "${it.pairOfNumbers.first}"
	tvOperationSymbol.text = it.operationSymbol
	tvSecondNumber.text = "${it.pairOfNumbers.second}"
	tvExpectedResult.text = "${it.result}"
	tvInsertedResult.text = "${it.insertedResult}"
}