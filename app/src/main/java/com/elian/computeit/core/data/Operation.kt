package com.elian.computeit.core.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class Operation(
    val symbol: String,
    val calculate: (a: Int, b: Int) -> Int,
) : Parcelable
{
    operator fun invoke(pair: Pair<Int, Int>) = calculate(pair.first, pair.second)
}

@Parcelize
object Sum : Operation(
    symbol = "+",
    calculate = { a, b -> a + b }
)

@Parcelize
object Subtract : Operation(
    symbol = "−",
    calculate = { a, b -> a - b }
)

@Parcelize
object Multiply : Operation(
    symbol = "×",
    calculate = { a, b -> a * b }
)

@Parcelize
object Divide : Operation(
    symbol = "÷",
    calculate = { a, b -> a / b }
)