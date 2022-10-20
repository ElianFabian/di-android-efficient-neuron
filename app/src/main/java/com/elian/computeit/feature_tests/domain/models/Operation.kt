package com.elian.computeit.feature_tests.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class Operation(
    val symbol: String,
    private val calculate: (a: Int, b: Int) -> Int,
) : Parcelable
{
    operator fun invoke(pair: Pair<Int, Int>) = calculate(pair.first, pair.second)
}

//enum class Op(
//    val symbol: String,
//    val calculate: (a: Int, b: Int) -> Int,
//)
//{
//    Sum(
//        symbol = "+",
//        calculate = { a, b -> a + b }
//    ),
//    Subtract(
//        symbol = "-",
//        calculate = { a, b -> a - b }
//    ),
//    Multiply(
//        symbol = "×",
//        calculate = { a, b -> a * b }
//    ),
//    Divide(
//        symbol = "÷",
//        calculate = { a, b -> a / b }
//    );
//
//    operator fun invoke(pair: Pair<Int, Int>) = calculate(pair.first, pair.second)
//}

@Parcelize
object Sum : Operation(
    symbol = "+",
    calculate = { a, b -> a + b }
)

@Parcelize
object Subtract : Operation(
    symbol = "-",
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