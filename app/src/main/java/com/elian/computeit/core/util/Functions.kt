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

	val range = start..end

	for (a in range)
	{
		for (b in start..(a - oneOrZero))
		{
			if (a % b != 0) continue

			divisiblePairs += a to b
		}
	}

	return divisiblePairs
}