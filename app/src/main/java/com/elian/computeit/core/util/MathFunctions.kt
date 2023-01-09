package com.elian.computeit.core.util

import kotlin.math.ceil
import kotlin.math.sqrt

// This function has been optimized thanks to this article: https://www.math.uh.edu/~minru/web/divis2.html#:~:text=The%20most%20basic%20method%20for,of%20positive%20divisors%20for%20n
fun getDivisiblePairsInRange(
	start: Int,
	end: Int,
	ignoreSelfDivision: Boolean = false,
): List<IntArray>
{
	if (start == 0) throw IllegalArgumentException("start parameter can't be 0")

	val oneOrZero = if (ignoreSelfDivision) 1.0 else 0.0

	val divisiblePairs = mutableListOf<IntArray>()

	for (numerator in start..end)
	{
		val optimizedEnd = ceil(sqrt(numerator - oneOrZero)).toInt()

		var divisorFromPreviousIteration = -1

		for (denominator in start..optimizedEnd)
		{
			if (numerator % denominator != 0 || denominator == divisorFromPreviousIteration) continue

			divisiblePairs += intArrayOf(numerator, denominator)

			val secondDivisor = numerator / denominator

			if (!(ignoreSelfDivision && numerator == secondDivisor) && denominator != secondDivisor) divisiblePairs += intArrayOf(numerator, secondDivisor)

			divisorFromPreviousIteration = secondDivisor
		}
	}

	return divisiblePairs
}

fun getDivisiblePairsInRangeCount(
	start: Int,
	end: Int,
	ignoreSelfDivision: Boolean = false,
): Int
{
	if (start == 0) throw IllegalArgumentException("start parameter can't be 0")

	val oneOrZero = if (ignoreSelfDivision) 1.0 else 0.0

	var divisiblePairCount = 0

	for (numerator in start..end)
	{
		val optimizedEnd = ceil(sqrt(numerator - oneOrZero)).toInt()

		var divisorFromPreviousIteration = -1

		for (denominator in start..optimizedEnd)
		{
			if (numerator % denominator != 0 || denominator == divisorFromPreviousIteration) continue

			divisiblePairCount++

			val secondDivisor = numerator / denominator

			if (!(ignoreSelfDivision && numerator == secondDivisor) && denominator != secondDivisor) divisiblePairCount++

			divisorFromPreviousIteration = secondDivisor
		}
	}

	return divisiblePairCount
}