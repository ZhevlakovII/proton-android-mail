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

package ch.protonmail.android.mailmailbox.domain.usecase

import ch.protonmail.android.mailconversation.domain.repository.ConversationRepository
import ch.protonmail.android.mailmailbox.domain.mapper.ConversationMailboxItemMapper
import ch.protonmail.android.mailmailbox.domain.mapper.MessageMailboxItemMapper
import ch.protonmail.android.mailmailbox.domain.model.MailboxItem
import ch.protonmail.android.mailmailbox.domain.model.MailboxItemType
import ch.protonmail.android.mailmessage.domain.repository.MessageRepository
import ch.protonmail.android.mailpagination.domain.model.PageKey
import me.proton.core.domain.entity.UserId
import me.proton.core.label.domain.entity.LabelType
import me.proton.core.label.domain.repository.LabelRepository
import javax.inject.Inject

/**
 * Get MailboxItems for a user, according a [PageKey].
 *
 * @see GetMultiUserMailboxItems
 */
class GetMailboxItems @Inject constructor(
    private val labelRepository: LabelRepository,
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository,
    private val messageMailboxItemMapper: MessageMailboxItemMapper,
    private val conversationMailboxItemMapper: ConversationMailboxItemMapper
) {
    suspend operator fun invoke(
        userId: UserId,
        type: MailboxItemType,
        pageKey: PageKey = PageKey()
    ): List<MailboxItem> {
        val folders = labelRepository.getLabels(userId, LabelType.MessageFolder)
        val labels = labelRepository.getLabels(userId, LabelType.MessageLabel)
        val labelsMaps = (labels + folders).associateBy { it.labelId }
        return when (type) {
            MailboxItemType.Message -> messageRepository.getMessages(userId, pageKey).map {
                messageMailboxItemMapper.toMailboxItem(it, labelsMaps)
            }
            MailboxItemType.Conversation -> conversationRepository.getConversations(userId, pageKey).map {
                conversationMailboxItemMapper.toMailboxItem(it, labelsMaps)
            }
        }
    }

    companion object {
        /**
         * Define which DB Tables are involved to invalidate [invoke] for [Message].
         */
        private val messageTables = arrayOf(
            "MessageEntity",
            "MessageLabelEntity",
            "LabelEntity"
        )

        /**
         * Define which DB Tables are involved to invalidate [invoke] for [Conversation].
         */
        private val conversationTables = arrayOf(
            "ConversationEntity",
            "ConversationLabelEntity",
            "LabelEntity"
        )

        /**
         * Return DB Tables involved to invalidate [invoke] according [type].
         */
        fun getInvolvedTables(type: MailboxItemType) = when (type) {
            MailboxItemType.Message -> messageTables
            MailboxItemType.Conversation -> conversationTables
        }
    }
}
