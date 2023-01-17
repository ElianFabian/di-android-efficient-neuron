package com.elian.computeit.feature_tests.domain.args

import com.elian.computeit.core.domain.models.OperationType
import com.elian.computeit.core.domain.models.Range
import com.elian.computeit.core.util.constants.Args
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestArgs(
	val operation: OperationType,
	val range: Range,
	val totalTimeInSeconds: Int,
) : Args