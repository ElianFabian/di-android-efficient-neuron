package com.elian.computeit.core.presentation.util

import androidx.lifecycle.ViewModel
import com.elian.computeit.core.domain.repository.TestDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val testDataRepository: TestDataRepository,
) : ViewModel()
{
    suspend fun getTestDataList() = testDataRepository.getTestDataList()
}