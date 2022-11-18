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

package ch.protonmail.android.maildetail.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ch.protonmail.android.mailcommon.domain.model.Action
import ch.protonmail.android.mailcommon.presentation.Effect
import ch.protonmail.android.mailcommon.presentation.R
import ch.protonmail.android.mailcommon.presentation.model.ActionUiModel
import ch.protonmail.android.mailcommon.presentation.model.AvatarUiModel
import ch.protonmail.android.mailcommon.presentation.model.BottomBarState
import ch.protonmail.android.mailcommon.presentation.model.TextUiModel
import ch.protonmail.android.mailcommon.presentation.model.contentDescription
import ch.protonmail.android.mailcommon.presentation.model.iconDrawable
import ch.protonmail.android.maildetail.presentation.model.MessageDetailHeaderUiModel
import ch.protonmail.android.maildetail.presentation.model.MessageMetadataState
import ch.protonmail.android.maildetail.presentation.model.MessageDetailState
import ch.protonmail.android.maildetail.presentation.model.MessageLocationUiModel
import ch.protonmail.android.maildetail.presentation.model.ParticipantUiModel

object MessageDetailsPreviewData {

    private val messageDetailHeaderUiModel = MessageDetailHeaderUiModel(
        avatar = AvatarUiModel.ParticipantInitial("S"),
        sender = ParticipantUiModel("Sender", "sender@pm.com", R.drawable.ic_proton_lock),
        shouldShowTrackerProtectionIcon = false,
        shouldShowAttachmentIcon = true,
        shouldShowStar = true,
        location = MessageLocationUiModel("Archive", R.drawable.ic_proton_archive_box),
        time = TextUiModel.Text("08/11/2022"),
        extendedTime = TextUiModel.Text("08/11/2022, 17:16"),
        shouldShowUndisclosedRecipients = false,
        allRecipients = TextUiModel.Text("Recipient1, Recipient2, Recipient3"),
        toRecipients = listOf(
            ParticipantUiModel("Recipient1", "recipient1@pm.com", R.drawable.ic_proton_lock),
            ParticipantUiModel("Recipient2", "recipient2@pm.com", R.drawable.ic_proton_lock)
        ),
        ccRecipients = listOf(ParticipantUiModel("Recipient3", "recipient3@pm.com", R.drawable.ic_proton_lock)),
        bccRecipients = emptyList(),
        labels = emptyList(),
        size = "12 MB",
        encryptionPadlock = R.drawable.ic_proton_lock,
        encryptionInfo = "End-to-end encrypted and signed message"
    )

    val Message = MessageDetailState(
        messageMetadataState = MessageMetadataState.Data(
            messageDetailActionBar = MessageDetailActionBarUiModelPreviewData.FirstWeekOfAugWeatherForecast,
            messageDetailHeader = messageDetailHeaderUiModel
        ),
        bottomBarState = BottomBarState.Data(
            listOf(
                ActionUiModel(Action.Reply, Action.Reply.iconDrawable(), Action.Reply.contentDescription()),
                ActionUiModel(Action.Archive, Action.Archive.iconDrawable(), Action.Archive.contentDescription())
            )
        ),
        dismiss = Effect.empty(),
        error = Effect.empty()
    )

    val Loading = MessageDetailState(
        messageMetadataState = MessageMetadataState.Loading,
        bottomBarState = BottomBarState.Loading,
        dismiss = Effect.empty(),
        error = Effect.empty()
    )
}

class MessageDetailsPreviewProvider : PreviewParameterProvider<MessageDetailState> {

    override val values = sequenceOf(
        MessageDetailsPreviewData.Message,
        MessageDetailsPreviewData.Loading
    )
}
