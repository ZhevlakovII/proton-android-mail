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

package ch.protonmail.android.mailmailbox.presentation.mailbox.reducer

import ch.protonmail.android.mailcommon.presentation.Effect
import ch.protonmail.android.mailcommon.presentation.model.TextUiModel
import ch.protonmail.android.mailcommon.presentation.model.TextUiModel.PluralisedText
import ch.protonmail.android.mailmailbox.presentation.R
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxEvent
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxOperation
import me.proton.core.mailsettings.domain.entity.ViewMode
import javax.inject.Inject

class MailboxActionMessageReducer @Inject constructor() {

    internal fun newStateFrom(operation: MailboxOperation.AffectingActionMessage): Effect<TextUiModel> {
        return when (operation) {
            is MailboxEvent.Trash ->
                Effect.of(TextUiModel(R.plurals.mailbox_action_trash, operation.numAffectedMessages))

            is MailboxEvent.DeleteConfirmed -> {
                val resource = when (operation.viewMode) {
                    ViewMode.ConversationGrouping -> R.plurals.mailbox_action_delete_conversation
                    ViewMode.NoConversationGrouping -> R.plurals.mailbox_action_delete_message
                }
                Effect.of(PluralisedText(resource, operation.numAffectedMessages))
            }
        }
    }
}
