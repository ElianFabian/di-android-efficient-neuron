package com.elian.computeit.core.util

import kotlin.math.ceil
import kotlin.math.sqrt

// This function has been optimized thanks to this article: https://www.math.uh.edu/~minru/web/divis2.html#:~:text=The%20most%20basic%20method%20for,of%20positive%20divisors%20for%20n
fun getDivisiblePairsInRange(
	start: Int,
	end: Int,
	ignoreSelfDivision: Boolean = false,
): List<Pair<Int, Int>>
{
	if (start == 0) throw IllegalArgumentException("start parameter can't be 0")

	val oneOrZero = if (ignoreSelfDivision) 1.0 else 0.0

	val divisiblePairs = mutableSetOf<Pair<Int, Int>>()

	for (numerator in start..end)
	{
		val optimizedEnd = ceil(sqrt(numerator - oneOrZero)).toInt()

		for (denominator in start..optimizedEnd)
		{
			if (numerator % denominator != 0) continue

			divisiblePairs += numerator to denominator

			val secondDivisor = numerator / denominator

			if (!(ignoreSelfDivision && numerator == secondDivisor)) divisiblePairs += numerator to secondDivisor
		}
	}

	return divisiblePairs.toList()
}