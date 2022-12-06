package com.elian.computeit.feature_tests.presentation.test_end.adapter

import com.elian.computeit.core.presentation.adapter.GenericAdapter
import com.elian.computeit.databinding.ItemFailedOperationBinding
import com.elian.computeit.feature_tests.domain.model.OperationInfo

@Suppress("FunctionName", "UNCHECKED_CAST")
fun FailedOperationAdapter(list: List<OperationInfo>) = GenericAdapter(
	inflate = ItemFailedOperationBinding::inflate,
	list = list,
) {
	tvFirstNumber.text = "${it.pairOfNumbers.first}"
	tvOperationSymbol.text = it.operationSymbol
	tvSecondNumber.text = "${it.pairOfNumbers.second}"
	tvInsertedResult.text = "${it.insertedResult}"
	tvExpectedResult.text = "${it.result}"
}