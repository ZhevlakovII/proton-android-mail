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

package ch.protonmail.android.testdata.mailbox

import ch.protonmail.android.mailconversation.domain.entity.ConversationId
import ch.protonmail.android.mailconversation.domain.entity.Recipient
import ch.protonmail.android.mailmailbox.domain.model.MailboxItem
import ch.protonmail.android.mailmailbox.domain.model.MailboxItemType
import ch.protonmail.android.testdata.label.LabelTestData.buildLabel
import me.proton.core.domain.entity.UserId

object MailboxTestData {

    fun buildMailboxItem(
        userId: UserId,
        id: String,
        time: Long,
        labelIds: List<String>,
        type: MailboxItemType,
    ) = MailboxItem(
        type = type,
        id = id,
        conversationId = ConversationId(id),
        userId = userId,
        time = time,
        size = 1000,
        order = 1000,
        read = true,
        subject = "subject",
        senders = listOf(Recipient("address", "name")),
        recipients = emptyList(),
        labels = labelIds.map { buildLabel(userId = userId, id = it) }
    )
}
