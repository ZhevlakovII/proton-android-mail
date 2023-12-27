/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton Technologies AG and Proton Mail.
 *
 * Proton Mail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Mail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Mail. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.protonmail.android.mailmailbox.presentation.paging

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ch.protonmail.android.mailmailbox.presentation.mailbox.MailboxScreenState
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxItemUiModel
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxSearchMode
import ch.protonmail.android.mailmailbox.presentation.paging.exception.DataErrorException
import ch.protonmail.android.mailmailbox.presentation.paging.search.mapToUiStatesInSearch
import ch.protonmail.android.test.annotations.suite.SmokeTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

@SmokeTest
class SearchPagingLoadingStateMapperKtTest {

    private val nonEmptyPagingItemsNotRefreshLoading = createPagingItems(isRefreshLoading = false, itemCount = 5)

    private val transitionsFromNewSearchState = listOf(
        // when searchMode is NewSearch
        TestItem(
            lazyPagingItems = mockk(),
            searchMode = MailboxSearchMode.NewSearch,
            currentScreenState = mockk(),
            expectedScreenState = MailboxScreenState.NewSearch
        ),
        // When LazyPagingItems is in refresh loading state
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = true),
            searchMode = MailboxSearchMode.NewSearchLoading,
            currentScreenState = MailboxScreenState.NewSearch,
            expectedScreenState = MailboxScreenState.SearchLoading
        ),
        // When LazyPagingItems is in refresh error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(refreshError = true),
            searchMode = MailboxSearchMode.NewSearchLoading,
            currentScreenState = MailboxScreenState.NewSearch,
            expectedScreenState = MailboxScreenState.Error
        ),
        // When LazyPagingItems is in append error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(appendError = true),
            searchMode = MailboxSearchMode.NewSearchLoading,
            currentScreenState = MailboxScreenState.NewSearch,
            expectedScreenState = MailboxScreenState.AppendError
        )
    )

    private val transitionsFromSearchLoadingState = listOf(
        // when LazyPagingItems is loading and is empty
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = true),
            currentScreenState = MailboxScreenState.SearchLoading,
            expectedScreenState = MailboxScreenState.SearchLoading
        ),
        // when LazyPagingItems is loading and is not empty (Disallowed state transition, first loading should complete
        // and we should first switch to SearchData state
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = true, itemCount = 5),
            currentScreenState = MailboxScreenState.SearchLoading,
            expectedScreenState = MailboxScreenState.SearchLoading
        ),
        // when LazyPagingItems is not loading and is empty
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = false, itemCount = 0),
            currentScreenState = MailboxScreenState.SearchLoading,
            expectedScreenState = MailboxScreenState.SearchNoData
        ),
        // when LazyPagingItems is not loading and is not empty
        TestItem(
            lazyPagingItems = nonEmptyPagingItemsNotRefreshLoading,
            currentScreenState = MailboxScreenState.SearchLoading,
            expectedScreenState = MailboxScreenState.SearchData(
                nonEmptyPagingItemsNotRefreshLoading
            )
        ),
        // When LazyPagingItems is in refresh error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(refreshError = true),
            currentScreenState = MailboxScreenState.SearchLoading,
            expectedScreenState = MailboxScreenState.Error
        ),
        // When LazyPagingItems is in append error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(appendError = true),
            currentScreenState = MailboxScreenState.SearchLoading,
            expectedScreenState = MailboxScreenState.AppendError
        )
    )

    private val transitionsFromSearchNoDataState = listOf(
        // TestItem for when LazyPagingItems is loading
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = true),
            currentScreenState = MailboxScreenState.SearchNoData,
            expectedScreenState = MailboxScreenState.SearchLoading
        ),
        // TestItem for when LazyPagingItems is not loading and is empty
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = false, itemCount = 0),
            currentScreenState = MailboxScreenState.SearchNoData,
            expectedScreenState = MailboxScreenState.SearchNoData
        ),
        // TestItem for when LazyPagingItems is not loading and is not empty. This is not an expected behavior,
        // we expect a loading state before we switch to SearchData state. However, this is a valid state transition
        // since we do not want to stay in no data state when there is data available
        TestItem(
            lazyPagingItems = nonEmptyPagingItemsNotRefreshLoading,
            currentScreenState = MailboxScreenState.SearchNoData,
            expectedScreenState = MailboxScreenState.SearchData(nonEmptyPagingItemsNotRefreshLoading)
        ),
        // When LazyPagingItems is in refresh error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(refreshError = true),
            currentScreenState = MailboxScreenState.SearchLoading,
            expectedScreenState = MailboxScreenState.Error
        ),
        // When LazyPagingItems is in append error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(appendError = true),
            currentScreenState = MailboxScreenState.SearchLoading,
            expectedScreenState = MailboxScreenState.AppendError
        )
    )

    private val transitionsFromSearchLoadingWithDataState = listOf(
        // when LazyPagingItems is loading
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = true),
            currentScreenState = MailboxScreenState.SearchLoadingWithData,
            expectedScreenState = MailboxScreenState.SearchLoadingWithData
        ),
        // when LazyPagingItems is not loading and is empty
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = false, itemCount = 0),
            currentScreenState = MailboxScreenState.SearchLoadingWithData,
            expectedScreenState = MailboxScreenState.SearchNoData
        ),
        //  when LazyPagingItems is not loading and is not empty
        TestItem(
            lazyPagingItems = nonEmptyPagingItemsNotRefreshLoading,
            currentScreenState = MailboxScreenState.SearchLoadingWithData,
            expectedScreenState = MailboxScreenState.SearchData(nonEmptyPagingItemsNotRefreshLoading)
        ),
        // When LazyPagingItems is in refresh error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(refreshError = true),
            currentScreenState = MailboxScreenState.SearchLoadingWithData,
            expectedScreenState = MailboxScreenState.Error
        ),
        // When LazyPagingItems is in append error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(appendError = true),
            currentScreenState = MailboxScreenState.SearchLoadingWithData,
            expectedScreenState = MailboxScreenState.AppendError
        )
    )

    private val transitionsFromRefreshErrorState = listOf(
        // When LazyPagingItems is in refresh error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(refreshError = true),
            currentScreenState = MailboxScreenState.Error,
            expectedScreenState = MailboxScreenState.Error
        ),
        // When LazyPagingItems is in append error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(appendError = true),
            currentScreenState = MailboxScreenState.Error,
            expectedScreenState = MailboxScreenState.AppendError
        ),
        // when searchMode is NewSearch
        TestItem(
            lazyPagingItems = mockk(),
            searchMode = MailboxSearchMode.NewSearch,
            currentScreenState = MailboxScreenState.Error,
            expectedScreenState = MailboxScreenState.NewSearch
        ),
        // When LazyPagingItems is in refresh loading state
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = true),
            searchMode = MailboxSearchMode.NewSearchLoading,
            currentScreenState = MailboxScreenState.Error,
            expectedScreenState = MailboxScreenState.SearchLoading
        ),
        // When LazyPagingItems is in refresh not loading state and is not empty
        TestItem(
            lazyPagingItems = nonEmptyPagingItemsNotRefreshLoading,
            searchMode = MailboxSearchMode.NewSearchLoading,
            currentScreenState = MailboxScreenState.Error,
            expectedScreenState = MailboxScreenState.SearchData(nonEmptyPagingItemsNotRefreshLoading)
        )
    )

    private val transitionsFromAppendErrorState = listOf(
        // When LazyPagingItems is in refresh error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(refreshError = true),
            currentScreenState = MailboxScreenState.AppendError,
            expectedScreenState = MailboxScreenState.Error
        ),
        // When LazyPagingItems is in append error state
        TestItem(
            lazyPagingItems = createPagingItemsInError(appendError = true),
            currentScreenState = MailboxScreenState.AppendError,
            expectedScreenState = MailboxScreenState.AppendError
        ),
        // when searchMode is NewSearch
        TestItem(
            lazyPagingItems = mockk(),
            searchMode = MailboxSearchMode.NewSearch,
            currentScreenState = MailboxScreenState.AppendError,
            expectedScreenState = MailboxScreenState.NewSearch
        ),
        // When LazyPagingItems is in refresh not loading state and is empty
        TestItem(
            lazyPagingItems = createPagingItems(isRefreshLoading = false, itemCount = 0),
            searchMode = MailboxSearchMode.NewSearchLoading,
            currentScreenState = MailboxScreenState.AppendError,
            expectedScreenState = MailboxScreenState.SearchNoData
        ),
        // When LazyPagingItems is in refresh not loading state and is not empty
        TestItem(
            lazyPagingItems = nonEmptyPagingItemsNotRefreshLoading,
            searchMode = MailboxSearchMode.NewSearchLoading,
            currentScreenState = MailboxScreenState.AppendError,
            expectedScreenState = MailboxScreenState.SearchData(nonEmptyPagingItemsNotRefreshLoading)
        )
    )

    @Test
    fun testTransitionsFromNewSearchState() {
        transitionsFromNewSearchState.forEach { testItem ->
            // Given
            val lazyPagingItems = testItem.lazyPagingItems
            val searchMode = testItem.searchMode
            val currentScreenState = testItem.currentScreenState

            // When
            val result = lazyPagingItems.mapToUiStatesInSearch(searchMode, currentScreenState)

            // Then
            assertEquals(testItem.expectedScreenState, result)
        }
    }


    @Test
    fun testTransitionsFromSearchLoadingState() {
        transitionsFromSearchLoadingState.forEach { testItem ->
            // Given
            val lazyPagingItems = testItem.lazyPagingItems
            val searchMode = testItem.searchMode
            val currentScreenState = testItem.currentScreenState

            // When
            val result = lazyPagingItems.mapToUiStatesInSearch(searchMode, currentScreenState)

            // Then
            assertEquals(testItem.expectedScreenState, result)
        }
    }

    @Test
    fun testTransitionsFromSearchNoDataState() {
        transitionsFromSearchNoDataState.forEach { testItem ->
            // Given
            val lazyPagingItems = testItem.lazyPagingItems
            val searchMode = testItem.searchMode
            val currentScreenState = testItem.currentScreenState

            // When
            val result = lazyPagingItems.mapToUiStatesInSearch(searchMode, currentScreenState)

            // Then
            assertEquals(testItem.expectedScreenState, result)
        }
    }

    @Test
    fun testTransitionsFromSearchLoadingWithDataState() {
        transitionsFromSearchLoadingWithDataState.forEach { testItem ->
            // Given
            val lazyPagingItems = testItem.lazyPagingItems
            val searchMode = testItem.searchMode
            val currentScreenState = testItem.currentScreenState

            // When
            val result = lazyPagingItems.mapToUiStatesInSearch(searchMode, currentScreenState)

            // Then
            assertEquals(testItem.expectedScreenState, result)
        }
    }

    @Test
    fun testTransitionsFromRefreshErrorState() {
        transitionsFromRefreshErrorState.forEach { testItem ->
            // Given
            val lazyPagingItems = testItem.lazyPagingItems
            val searchMode = testItem.searchMode
            val currentScreenState = testItem.currentScreenState

            // When
            val result = lazyPagingItems.mapToUiStatesInSearch(searchMode, currentScreenState)

            // Then
            assertEquals(testItem.expectedScreenState, result)
        }
    }

    @Test
    fun testTransitionsFromAppendErrorState() {
        transitionsFromAppendErrorState.forEach { testItem ->
            // Given
            val lazyPagingItems = testItem.lazyPagingItems
            val searchMode = testItem.searchMode
            val currentScreenState = testItem.currentScreenState

            // When
            val result = lazyPagingItems.mapToUiStatesInSearch(searchMode, currentScreenState)

            // Then
            assertEquals(testItem.expectedScreenState, result)
        }
    }

    private fun createPagingItems(
        isRefreshLoading: Boolean,
        isAppendLoading: Boolean = false,
        itemCount: Int = 0
    ): LazyPagingItems<MailboxItemUiModel> {
        val loadState =
            CombinedLoadStates(
                refresh = if (isRefreshLoading) LoadState.Loading else LoadState.NotLoading(true),
                append = if (isAppendLoading) LoadState.Loading else LoadState.NotLoading(true),
                prepend = mockk(),
                source = mockk()
            )

        val mockPaging: LazyPagingItems<MailboxItemUiModel> = mockk(relaxed = true)
        every { mockPaging.loadState } returns loadState
        every { mockPaging.itemCount } returns itemCount
        return mockPaging
    }

    private fun createPagingItemsInError(
        refreshError: Boolean = false,
        appendError: Boolean = false,
        itemCount: Int = 0
    ): LazyPagingItems<MailboxItemUiModel> {
        val loadState =
            CombinedLoadStates(
                refresh = if (refreshError) LoadState.Error(DataErrorException(mockk()))
                else LoadState.NotLoading(true),
                append = if (appendError) LoadState.Error(DataErrorException(mockk()))
                else LoadState.NotLoading(true),
                prepend = mockk(),
                source = mockk()
            )

        val mockPaging: LazyPagingItems<MailboxItemUiModel> = mockk(relaxed = true)
        every { mockPaging.loadState } returns loadState
        every { mockPaging.itemCount } returns itemCount
        return mockPaging
    }

    data class TestItem(
        val lazyPagingItems: LazyPagingItems<MailboxItemUiModel>,
        val searchMode: MailboxSearchMode = MailboxSearchMode.SearchData,
        val currentScreenState: MailboxScreenState,
        val expectedScreenState: MailboxScreenState
    )

}
