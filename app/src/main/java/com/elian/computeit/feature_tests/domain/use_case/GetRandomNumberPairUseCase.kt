package com.elian.computeit.feature_tests.domain.use_case

import androidx.lifecycle.SavedStateHandle
import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.NumberPair
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_NUMBER_RANGE
import com.elian.computeit.core.util.constants.EXTRA_OPERATION_TYPE
import com.elian.computeit.core.util.getDivisiblePairsInRange
import javax.inject.Inject
import kotlin.random.Random

class GetRandomNumberPairUseCase @Inject constructor(
    savedState: SavedStateHandle,
)
{
    private val _rangeBounds = savedState.get<Range>(EXTRA_OPERATION_NUMBER_RANGE)!!
    private val _range = _rangeBounds.min.._rangeBounds.max

    private val _operation = savedState.get<Operation>(EXTRA_OPERATION_TYPE)!!
    private var _divisiblePairs: List<NumberPair> = if (_operation == Operation.DIVIDE)
    {
        getDivisiblePairsInRange(_range).map { NumberPair(it.first, it.second) }
    }
    else emptyList()


    operator fun invoke(oldPair: NumberPair? = null): NumberPair
    {
        if (oldPair == null) return getRandomPair()

        while (true)
        {
            val newPair = getRandomPair()

            val isDifferent = newPair.first != oldPair.first || newPair.second != oldPair.second
            val isDifferentInReverse = newPair.first != oldPair.second || newPair.second != oldPair.first

            if (isDifferent && isDifferentInReverse) return newPair
        }
    }

    private fun getRandomPair(): NumberPair
    {
        if (_operation == Operation.DIVIDE) return _divisiblePairs.random(seed)

        return getRandomNumberPair(_range)
    }
}


private val seed = Random(System.currentTimeMillis())

private fun getRandomNumberPair(range: IntRange): NumberPair
{
    val first = range.random(seed)
    val second = range.random(seed)

    return NumberPair(first, second)
}