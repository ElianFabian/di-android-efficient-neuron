package com.elian.computeit.core.util

fun getDivisiblePairsInRange(
	start: Int,
	end: Int,
	ignoreSelfDivision: Boolean = false,
): List<Pair<Int, Int>>
{
	if (start == 0) throw IllegalArgumentException("start parameter can't be 0")

	val oneOrZero = if (ignoreSelfDivision) 1 else 0

	val divisiblePairs = mutableListOf<Pair<Int, Int>>()

	for (numerator in start..end)
	{
		for (denominator in start..(numerator - oneOrZero))
		{
			if (numerator % denominator != 0) continue

			divisiblePairs += numerator to denominator
		}
	}

	return divisiblePairs
}