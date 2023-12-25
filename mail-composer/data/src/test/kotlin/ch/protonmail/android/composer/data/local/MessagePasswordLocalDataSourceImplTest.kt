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

package ch.protonmail.android.composer.data.local

import java.io.IOException
import arrow.core.left
import arrow.core.right
import ch.protonmail.android.composer.data.local.dao.MessagePasswordDao
import ch.protonmail.android.composer.data.local.entity.toEntity
import ch.protonmail.android.mailcommon.domain.model.DataError
import ch.protonmail.android.mailcomposer.domain.model.MessagePassword
import ch.protonmail.android.mailmessage.domain.sample.MessageIdSample
import ch.protonmail.android.testdata.user.UserIdTestData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MessagePasswordLocalDataSourceImplTest {

    val userId = UserIdTestData.userId
    val messageId = MessageIdSample.NewDraftWithSubjectAndBody

    private val messagePasswordDao = mockk<MessagePasswordDao>()
    private val database = mockk<DraftStateDatabase> {
        every { messagePasswordDao() } returns messagePasswordDao
    }

    private val messagePasswordLocalDataSource = MessagePasswordLocalDataSourceImpl(database)

    @Test
    fun `should return unit when saving message password was successful`() = runTest {
        // Given
        val password = "password"
        val hint = "hint"
        val messagePassword = MessagePassword(userId, messageId, password, hint)
        coEvery { messagePasswordDao.insertOrUpdate(messagePassword.toEntity()) } just runs

        // When
        val actual = messagePasswordLocalDataSource.save(messagePassword)

        // Then
        assertEquals(Unit.right(), actual)
    }

    @Test
    fun `should return error when saving message password throws an exception`() = runTest {
        // Given
        val password = "password"
        val hint = "hint"
        val messagePassword = MessagePassword(userId, messageId, password, hint)
        coEvery { messagePasswordDao.insertOrUpdate(messagePassword.toEntity()) } throws IOException()

        // When
        val actual = messagePasswordLocalDataSource.save(messagePassword)

        // Then
        assertEquals(DataError.Local.Unknown.left(), actual)
    }
}
