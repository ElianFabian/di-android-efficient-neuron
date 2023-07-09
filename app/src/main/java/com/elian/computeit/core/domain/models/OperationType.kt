package com.elian.computeit.core.domain.models

enum class OperationType(
	val symbol: String,
	inline val calculate: (a: Int, b: Int) -> Int,
) {
	Addition(
		symbol = "+",
		calculate = { a, b -> a + b },
	),
	Subtraction(
		symbol = "−",
		calculate = { a, b -> a - b },
	),
	Multiplication(
		symbol = "×",
		calculate = { a, b -> a * b },
	),
	Division(
		symbol = "÷",
		calculate = { a, b -> a / b },
	);

	companion object {
		fun fromName(name: String) = nameToOperation[name]!!

		private val nameToOperation = values().associateBy { it.name }
		val nameToSymbol = values().associate { it.name to it.symbol }
	}

	operator fun invoke(firstNumber: Int, secondNumber: Int) = calculate(firstNumber, secondNumber)
}