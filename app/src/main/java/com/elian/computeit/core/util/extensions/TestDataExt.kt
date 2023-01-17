package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.data.model.OperationData
import com.elian.computeit.core.data.model.TestData

fun TestData.getValuePerSecond(
	countSinceStartCondition: (OperationData) -> Boolean = { true },
	getValue: (currentSecond: Int, countSinceStart: Int) -> Float,
): Map<Int, Int>
{
	val start = 1
	val end = timeInSeconds

	return (start..end).associateWith { currentSecond ->

		val countSinceStart = listOfOperationData.count()
		{
			countSinceStartCondition(it) && it.millisSinceStart < currentSecond * 1000
		}

		val value = getValue(currentSecond, countSinceStart)

		value.ifNaNReturnZero().toInt()
	}
}