package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.domain.models.OperationData
import com.elian.computeit.core.domain.models.TestData

fun TestData.getValuePerSecond(
	getCountSinceStartFromCondition: (OperationData) -> Boolean = { true },
	getValue: (currentSecond: Int, countSinceStart: Int) -> Float,
): Map<Int, Int>
{
	val start = 1
	val end = timeInSeconds

	return (start..end).associateWith { currentSecond ->

		val countSinceStart = listOfOperationData.count()
		{
			getCountSinceStartFromCondition(it) && it.millisSinceStart < currentSecond * 1000
		}

		val value = getValue(currentSecond, countSinceStart)

		value.ifNaNReturnZero().toInt()
	}
}