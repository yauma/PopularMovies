/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaimequeralt.popularmovies.feature.popularmovies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.jaimequeralt.popularmovies.core.data.PopularMoviesRepository
import com.jaimequeralt.popularmovies.feature.popularmovies.ui.PopularMoviesUiState.Error
import com.jaimequeralt.popularmovies.feature.popularmovies.ui.PopularMoviesUiState.Loading
import com.jaimequeralt.popularmovies.feature.popularmovies.ui.PopularMoviesUiState.Success
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val popularMoviesRepository: PopularMoviesRepository
) : ViewModel() {

    val uiState: StateFlow<PopularMoviesUiState> = popularMoviesRepository
        .popularMoviess.map { Success(data = it) }
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addPopularMovies(name: String) {
        viewModelScope.launch {
            popularMoviesRepository.add(name)
        }
    }
}

sealed interface PopularMoviesUiState {
    object Loading : PopularMoviesUiState
    data class Error(val throwable: Throwable) : PopularMoviesUiState
    data class Success(val data: List<String>) : PopularMoviesUiState
}
