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

package ch.protonmail.android.composer.data.repository

import ch.protonmail.android.composer.data.remote.SyncDraftWorker
import ch.protonmail.android.mailcommon.data.worker.Enqueuer
import ch.protonmail.android.mailcommon.domain.sample.UserIdSample
import ch.protonmail.android.mailmessage.domain.sample.MessageIdSample
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class DraftRepositoryImplTest {

    private val enqueuer = mockk<Enqueuer>()

    private val draftRepository = DraftRepositoryImpl(enqueuer)

    @Test
    fun `enqueue sync draft work`() = runTest {
        // Given
        val userId = UserIdSample.Primary
        val messageId = MessageIdSample.LocalDraft
        val expectedParams = SyncDraftWorker.params(userId, messageId)
        givenEnqueuerSucceeds(expectedParams)

        // When
        draftRepository.sync(userId, messageId)

        // Then
        verify { enqueuer.enqueue<SyncDraftWorker>(expectedParams) }
    }

    private fun givenEnqueuerSucceeds(expectedParams: Map<String, String>) {
        every { enqueuer.enqueue<SyncDraftWorker>(expectedParams) } returns Unit
    }
}
