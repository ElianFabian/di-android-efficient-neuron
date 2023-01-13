package com.elian.computeit.feature_tests.domain.use_case

import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.util.getDivisiblePairsInRange
import javax.inject.Inject
import kotlin.random.Random

class GetRandomNumberPairUseCase @Inject constructor()
{
	private val randomSeed = Random(System.currentTimeMillis())

	private var _isUseCaseInitialized = false

	private lateinit var _range: IntRange
	private lateinit var _operation: Operation
	private lateinit var _divisiblePairs: List<NumberPair>


	/**
	 * Returns a random number pair.
	 * @param oldPair if isn't null the returned pair will always be different from the old one.
	 */
	operator fun invoke(oldPair: NumberPair? = null): NumberPair
	{
		if (!_isUseCaseInitialized) error("You forgot to call the 'initialize' function.")

		if (oldPair == null) return getRandomPairFromCurrentOperation()

		while (true)
		{
			val newPair = getRandomPairFromCurrentOperation()

			val arePairsDifferent = newPair.first != oldPair.first || newPair.second != oldPair.second
			val arePairsDifferentInReverse = newPair.first != oldPair.second || newPair.second != oldPair.first

			if (arePairsDifferent && arePairsDifferentInReverse) return newPair
		}
	}

	fun initialize(
		range: Range,
		operation: Operation,
	)
	{
		_range = range.min..range.max
		_operation = operation

		_divisiblePairs = if (operation == Operation.Division)
		{
			getDivisiblePairsInRange(
				start = _range.first,
				end = _range.last,
				ignoreSelfDivision = true,
			).map { NumberPair(it[0], it[1]) }
		}
		else emptyList()

		_isUseCaseInitialized = true
	}

	private fun getRandomPairFromCurrentOperation() = when (_operation)
	{
		Operation.Addition       -> getRandomPairExceptIf { it.first == 0 && it.second == 0 }
		Operation.Subtraction    -> getRandomPairExceptIf { it.first == it.second }
		Operation.Multiplication -> getRandomPairExceptIf { it.first == 0 || it.second == 0 }
		Operation.Division       -> _divisiblePairs.random(randomSeed)
	}

	private fun getRandomPairExceptIf(condition: (pair: NumberPair) -> Boolean): NumberPair
	{
		while (true)
		{
			val newPair = getRandomNumberPair(_range)

			if (condition(newPair)) continue

			return newPair
		}
	}

	private fun getRandomNumberPair(range: IntRange): NumberPair
	{
		val first = range.random(randomSeed)
		val second = range.random(randomSeed)

		return NumberPair(first, second)
	}
}