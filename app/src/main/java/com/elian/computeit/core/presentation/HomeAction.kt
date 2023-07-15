package com.elian.computeit.core.presentation

sealed interface HomeAction {
	class SelectTest(val testIndex: Int) : HomeAction
	object UnSelectTest : HomeAction
	object GoToTestDetail : HomeAction
	class ChangeRangeLength(val rangeLength: Int) : HomeAction
	class ChangeVerticalScroll(val scrollY: Int) : HomeAction

	// I didn't found a way to properly get the scale and position of the chart
	//class ChangeTestHistoryChartZoomScale(val scaleX: Float, val scaleY: Float) : HomeAction
	//class ChangeTestHistoryChartPosition(val positionX: Float, val positionY: Float) : HomeAction
}