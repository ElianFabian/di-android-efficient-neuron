package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.domain.models.TestData

val TestData.isError get() = expectedResult != insertedResult