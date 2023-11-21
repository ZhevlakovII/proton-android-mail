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

package ch.protonmail.android.maildetail.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ch.protonmail.android.mailcommon.presentation.AdaptivePreviews
import ch.protonmail.android.mailcommon.presentation.NO_CONTENT_DESCRIPTION
import ch.protonmail.android.maildetail.presentation.R.color
import ch.protonmail.android.maildetail.presentation.R.drawable
import ch.protonmail.android.maildetail.presentation.R.plurals
import ch.protonmail.android.maildetail.presentation.R.string
import ch.protonmail.android.maildetail.presentation.model.SubjectHeaderTransform
import ch.protonmail.android.maildetail.presentation.previewdata.DetailsScreenTopBarPreview
import ch.protonmail.android.maildetail.presentation.previewdata.DetailsScreenTopBarPreviewProvider
import me.proton.core.compose.theme.ProtonDimens
import me.proton.core.compose.theme.ProtonTheme
import me.proton.core.compose.theme.ProtonTheme3
import me.proton.core.compose.theme.captionNorm
import me.proton.core.compose.theme.defaultStrongNorm
import me.proton.core.compose.theme.headlineNorm

@Composable
fun DetailScreenTopBar(
    modifier: Modifier = Modifier,
    title: String,
    isStarred: Boolean?,
    messageCount: Int?,
    actions: DetailScreenTopBar.Actions,
    subjectHeaderSizeCallback: (Int) -> Unit,
    subjectHeaderTransform: SubjectHeaderTransform
) {
    ProtonTheme3 {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            CustomSingleLineTopAppBar(
                actions = actions,
                messageCount = messageCount,
                title = title,
                isStarred = isStarred,
                subjectLineAlpha = 1f - subjectHeaderTransform.alpha,
                modifier = modifier
                    .fillMaxWidth()
                    .zIndex(1f)
                    .graphicsLayer {
                        translationY = -subjectHeaderTransform.yOffsetPx
                    }
            )

            // Subject text should not exceed the screen height otherwise it will not be possible
            // to scroll message(s). So the subject text will cover at most 60% of the screen height
            val maxSubjectHeightDp = with(LocalDensity.current) {
                LocalContext.current.resources.displayMetrics.heightPixels / density * 0.6f
            }

            SubjectHeader(
                modifier = modifier
                    .background(ProtonTheme.colors.backgroundNorm)
                    .onGloballyPositioned { layoutCoordinates ->
                        subjectHeaderSizeCallback(layoutCoordinates.size.height)
                    }
                    .heightIn(
                        min = subjectHeaderMinHeight,
                        max = maxSubjectHeightDp.dp
                    ),
                subject = title,
                subjectTextAlpha = subjectHeaderTransform.alpha
            )
        }
    }
}

@Composable
fun CustomSingleLineTopAppBar(
    modifier: Modifier = Modifier,
    actions: DetailScreenTopBar.Actions,
    messageCount: Int?,
    title: String,
    isStarred: Boolean?,
    subjectLineAlpha: Float
) {
    Row(
        modifier = modifier
            .background(ProtonTheme.colors.backgroundNorm)
            .height(ProtonDimens.DefaultBottomSheetHeaderMinHeight)
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = actions.onBackClick
        ) {
            Icon(
                modifier = Modifier
                    .testTag(DetailScreenTopBarTestTags.BackButton),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = string.presentation_back),
                tint = ProtonTheme.colors.textNorm
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            messageCount?.let { count ->
                Text(
                    modifier = Modifier
                        .testTag(DetailScreenTopBarTestTags.MessageCount)
                        .fillMaxWidth(),
                    text = pluralStringResource(plurals.message_count_label_text, count, count),
                    style = ProtonTheme.typography.captionNorm,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = subjectLineAlpha
                    }
                    .fillMaxWidth()
                    .testTag(DetailScreenTopBarTestTags.Subject),
                maxLines = 1,
                text = title,
                overflow = TextOverflow.Ellipsis,
                style = ProtonTheme.typography.defaultStrongNorm,
                textAlign = TextAlign.Center
            )
        }

        if (isStarred != null) {
            val onStarIconClick = {
                when (isStarred) {
                    true -> actions.onUnStarClick()
                    false -> actions.onStarClick()
                }
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                onClick = onStarIconClick
            ) {
                Icon(
                    modifier = Modifier.size(ProtonDimens.DefaultIconSize),
                    painter = getStarredIcon(isStarred),
                    contentDescription = NO_CONTENT_DESCRIPTION,
                    tint = getStarredIconColor(isStarred)
                )
            }
        }
    }
}

val subjectHeaderMinHeight = 56.dp

@Composable
private fun SubjectHeader(
    subject: String,
    modifier: Modifier = Modifier,
    subjectTextAlpha: Float
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = ProtonDimens.DefaultSpacing,
                end = ProtonDimens.DefaultSpacing,
                top = ProtonDimens.SmallSpacing,
                bottom = ProtonDimens.SmallSpacing
            )
    ) {

        Text(
            modifier = Modifier
                .graphicsLayer {
                    alpha = subjectTextAlpha
                }
                .testTag(DetailScreenTopBarTestTags.Subject)
                .fillMaxWidth()
                .align(Alignment.Center),
            text = subject,
            overflow = TextOverflow.Ellipsis,
            style = ProtonTheme.typography.headlineNorm,
            textAlign = TextAlign.Center
        )

    }
}

@Composable
private fun getStarredIconColor(isStarred: Boolean) = if (isStarred) {
    colorResource(id = color.notification_warning)
} else {
    ProtonTheme.colors.textNorm
}

@Composable
private fun getStarredIcon(isStarred: Boolean) = painterResource(
    id = if (isStarred) {
        drawable.ic_proton_star_filled
    } else {
        drawable.ic_proton_star
    }
)

object DetailScreenTopBar {

    /**
     * Using an empty String for a Text inside LargeTopAppBar causes a crash.
     */
    const val NoTitle = " "

    data class Actions(
        val onBackClick: () -> Unit,
        val onStarClick: () -> Unit,
        val onUnStarClick: () -> Unit
    ) {

        companion object {

            val Empty = Actions(
                onBackClick = {},
                onStarClick = {},
                onUnStarClick = {}
            )
        }
    }
}

@Composable
@AdaptivePreviews
private fun DetailScreenTopBarPreview(
    @PreviewParameter(DetailsScreenTopBarPreviewProvider::class) preview: DetailsScreenTopBarPreview
) {
    ProtonTheme3 {
        DetailScreenTopBar(
            title = preview.title,
            isStarred = preview.isStarred,
            messageCount = preview.messageCount,
            actions = DetailScreenTopBar.Actions.Empty,
            subjectHeaderSizeCallback = {},
            subjectHeaderTransform = SubjectHeaderTransform(
                headerHeightPx = 0f,
                yOffsetPx = 0f,
                minOffsetPxForAlphaChange = 0f
            )
        )
    }
}

object DetailScreenTopBarTestTags {

    const val RootItem = "DetailScreenTopBarRootItem"
    const val BackButton = "BackButton"
    const val MessageCount = "MessageCount"
    const val Subject = "Subject"
}
