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

package ch.protonmail.android.mailconversation.data

import ch.protonmail.android.mailconversation.data.remote.resource.ConversationLabelResource
import ch.protonmail.android.mailconversation.data.remote.resource.ConversationResource
import me.proton.core.domain.entity.UserId

fun getConversationResource(
    id: String = "1",
    order: Long = 1000,
    labels: List<ConversationLabelResource> = listOf(getConversationLabelResource(id)),
) = ConversationResource(
    id = id,
    order = order,
    subject = "subject",
    expirationTime = 1000,
    labels = labels,
    numAttachments = 0,
    numMessages = 0,
    numUnread = 0,
    recipients = emptyList(),
    senders = emptyList()
)

fun getConversationLabelResource(
    id: String,
    contextTime: Long = 1000,
) = ConversationLabelResource(
    id = id,
    contextNumUnread = 0,
    contextNumMessages = 0,
    contextTime = 0,
    contextSize = contextTime,
    contextNumAttachments = 0,
)

fun getConversation(
    userId: UserId = UserId("1"),
    id: String = "1",
    order: Long = 1000,
    time: Long = 1000,
    labelIds: List<String> = listOf("0"),
) = getConversationResource(
    id = id,
    order = order,
    labels = labelIds.map { getConversationLabelResource(id, contextTime = time) },
).toConversation(userId)
