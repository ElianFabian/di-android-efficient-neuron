package com.elian.computeit.core.presentation

data class HomeState(
	val selectedTestIndex: Int = -1,
	val isLoading: Boolean = false,
	val speedRangeLength: Int? = null,
)

//data class HomeTestsHistoryChartState(
//	val zoomScaleX: Float = 1F,
//	val zoomScaleY: Float = 1F,
//	val positionX: Float = 0F,
//	val positionY: Float = 0F,
//)