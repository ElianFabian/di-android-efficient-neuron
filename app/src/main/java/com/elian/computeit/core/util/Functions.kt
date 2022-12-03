package com.elian.computeit.core.util

fun getDivisiblePairsInRange(start: Int, end: Int): List<Pair<Int, Int>>
{
	val divisiblePairs = mutableListOf<Pair<Int, Int>>()

	val reversedRange = end downTo start

	for (a in reversedRange)
	{
		for (b in (a - 1) downTo start)
		{
			if (b == 0 || a % b != 0) continue

			divisiblePairs += a to b
		}
	}

	return divisiblePairs.reversed()
}