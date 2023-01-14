package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.util.getDivisiblePairsInRange
import javax.inject.Inject
import kotlin.random.Random

class GetRandomNumberPairFromOperationUseCase @Inject constructor()
{
	private val _randomSeed = Random(System.currentTimeMillis())

	private lateinit var _previousRange: IntRange
	private lateinit var _divisiblePairs: List<NumberPair>


	/**
	 * Returns a random number pair.
	 * @param oldPair if isn't null the returned pair will always be different from the old one.
	 */
	operator fun invoke(
		operation: Operation,
		range: IntRange,
		oldPair: NumberPair? = null,
	): NumberPair
	{
		if (oldPair == null) return getRandomPairFromCurrentOperationInRange(operation, range)

		while (true)
		{
			val newPair = getRandomPairFromCurrentOperationInRange(operation, range)

			val arePairsDifferent = newPair.first != oldPair.first || newPair.second != oldPair.second
			val arePairsDifferentInReverse = newPair.first != oldPair.second || newPair.second != oldPair.first

			if (arePairsDifferent && arePairsDifferentInReverse) return newPair
		}
	}


	private fun getRandomPairFromCurrentOperationInRange(
		operation: Operation,
		range: IntRange,
	): NumberPair
	{
		if (!::_divisiblePairs.isInitialized)
		{
			_divisiblePairs = getDivisiblePairs(operation, range)

			_previousRange = range
		}
		else if (range != _previousRange)
		{
			_divisiblePairs = getDivisiblePairs(operation, range)
		}

		return when (operation)
		{
			Operation.Addition       -> getRandomPairExceptIf(range) { it.first == 0 && it.second == 0 }
			Operation.Subtraction    -> getRandomPairExceptIf(range) { it.first == it.second }
			Operation.Multiplication -> getRandomPairExceptIf(range) { it.first == 0 || it.second == 0 }
			Operation.Division       -> _divisiblePairs.random(_randomSeed)
		}
	}

	private fun getRandomPairExceptIf(
		range: IntRange,
		condition: (pair: NumberPair) -> Boolean,
	): NumberPair
	{
		while (true)
		{
			val newPair = getRandomNumberPair(range)

			if (condition(newPair)) continue

			return newPair
		}
	}

	private fun getRandomNumberPair(range: IntRange): NumberPair
	{
		val first = range.random(_randomSeed)
		val second = range.random(_randomSeed)

		return NumberPair(first, second)
	}

	private fun getDivisiblePairs(
		operation: Operation,
		range: IntRange,
	): List<NumberPair>
	{
		return if (operation == Operation.Division)
		{
			getDivisiblePairsInRange(
				start = range.first,
				end = range.last,
				ignoreSelfDivision = true,
			).map { NumberPair(it[0], it[1]) }
		}
		else emptyList()
	}
}