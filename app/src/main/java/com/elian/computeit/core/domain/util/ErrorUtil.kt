package com.elian.computeit.core.domain.util

import com.elian.computeit.core.util.Error

/**
 * Returns true if any error is different from null.
 */
fun checkErrors(vararg args: Error?) = args.any { it != null }