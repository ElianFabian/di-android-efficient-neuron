package com.elian.computeit.feature_tests.presentation.test_details.adapter

import com.elian.computeit.core.domain.models.OperationInfo
import com.elian.computeit.core.presentation.util.simple_list_adapter.SimpleListAdapter
import com.elian.computeit.databinding.ItemFailedOperationBinding

@Suppress("FunctionName")
fun FailedOperationAdapter(items: List<OperationInfo>) = SimpleListAdapter(
	inflate = ItemFailedOperationBinding::inflate,
) { binding, operation: OperationInfo, _ ->

	binding.apply {
		tvFirstNumber.text = "${operation.pairOfNumbers.first}"
		tvOperationSymbol.text = operation.operationSymbol
		tvSecondNumber.text = "${operation.pairOfNumbers.second}"
		tvExpectedResult.text = "${operation.result}"
		tvInsertedResult.text = "${operation.insertedResult}"
	}
}.apply { submitList(items) }