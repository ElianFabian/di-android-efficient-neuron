package com.elian.computeit.core.util

fun getDivisiblePairsInRange(minValue: Int, maxValue: Int): List<Pair<Int, Int>>
{
	val divisiblePairs = mutableListOf<Pair<Int, Int>>()

	val range = minValue..maxValue

	val reversedRange = range.reversed()

	for (a in reversedRange)
	{
		for (b in reversedRange)
		{
			if (b == 0 || a % b != 0 || a == b) continue

			divisiblePairs += a to b
		}
	}

	return divisiblePairs.reversed()
}