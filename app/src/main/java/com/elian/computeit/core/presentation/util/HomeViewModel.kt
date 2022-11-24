package com.elian.computeit.core.presentation.util

import androidx.lifecycle.ViewModel
import com.elian.computeit.core.domain.use_case.GetTestListInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTestListInfoUseCase: GetTestListInfoUseCase,
) : ViewModel()
{
    fun getTestListInfo() = getTestListInfoUseCase()
}