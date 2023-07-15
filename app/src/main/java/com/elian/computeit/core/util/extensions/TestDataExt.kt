package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.data.model.OperationData
import com.elian.computeit.core.data.model.TestData

fun TestData.getListOfValuePerSecond(
	countCondition: (OperationData) -> Boolean = { true },
	getValue: (currentSecond: Int, countSinceStart: Int) -> Float,
): Map<Int, Float> {
	return (1..timeInSeconds).associateWith { currentSecond ->

		val countSinceStart = listOfOperationData.count {
			countCondition(it) && it.millisSinceStart < currentSecond * 1000
		}

		val value = getValue(currentSecond, countSinceStart)

		value.ifNaNReturnZero()
	}
}