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

package com.jaimequeralt.popularmovies.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.jaimequeralt.popularmovies.core.data.DefaultPopularMoviesRepository
import com.jaimequeralt.popularmovies.core.database.PopularMovies
import com.jaimequeralt.popularmovies.core.database.PopularMoviesDao

/**
 * Unit tests for [DefaultPopularMoviesRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultPopularMoviesRepositoryTest {

    @Test
    fun popularMoviess_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultPopularMoviesRepository(FakePopularMoviesDao())

        repository.add("Repository")

        assertEquals(repository.popularMoviess.first().size, 1)
    }

}

private class FakePopularMoviesDao : PopularMoviesDao {

    private val data = mutableListOf<PopularMovies>()

    override fun getPopularMoviess(): Flow<List<PopularMovies>> = flow {
        emit(data)
    }

    override suspend fun insertPopularMovies(item: PopularMovies) {
        data.add(0, item)
    }
}
