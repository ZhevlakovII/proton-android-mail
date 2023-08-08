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

package ch.protonmail.android.mailcommon.data.worker

import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EnqueuerTest {

    private val workManager = mockk<WorkManager>()

    private val enqueuer = Enqueuer(workManager)

    @Test
    fun `keep the existing enqueued work when trying to enqueue again some unique work`() = runTest {
        // Given
        val workId = "SyncDraftWork-test-message-id"
        val params = mapOf("messageId" to "test-message-id")
        givenEnqueueWorkSucceeds(workId)

        // When
        enqueuer.enqueueUniqueWork<ListenableWorker>(workId, params)

        // Then
        val workPolicySlot = slot<ExistingWorkPolicy>()
        coVerify { workManager.enqueueUniqueWork(workId, capture(workPolicySlot), any<OneTimeWorkRequest>()) }
        assertEquals(ExistingWorkPolicy.KEEP, workPolicySlot.captured)
    }

    private fun givenEnqueueWorkSucceeds(workId: String) {
        every {
            workManager.enqueueUniqueWork(workId, ExistingWorkPolicy.KEEP, any<OneTimeWorkRequest>())
        } returns mockk()
    }

}
