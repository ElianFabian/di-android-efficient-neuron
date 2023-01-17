package com.elian.computeit.feature_tests.presentation.test_details.adapter

import com.elian.computeit.core.presentation.adapter.GenericAdapter
import com.elian.computeit.databinding.ItemFailedOperationBinding
import com.elian.computeit.core.domain.models.OperationInfo

@Suppress("FunctionName")
fun FailedOperationAdapter(items: List<OperationInfo>) = GenericAdapter(
	inflate = ItemFailedOperationBinding::inflate,
	items = items,
) { item, _ ->

	item.apply()
	{
		tvFirstNumber.text = "${pairOfNumbers.first}"
		tvOperationSymbol.text = operationSymbol
		tvSecondNumber.text = "${pairOfNumbers.second}"
		tvExpectedResult.text = "$result"
		tvInsertedResult.text = "$insertedResult"
	}
}