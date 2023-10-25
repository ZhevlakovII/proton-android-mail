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

package ch.protonmail.android.mailcomposer.presentation.usecase

import android.content.Context
import arrow.core.Either
import arrow.core.raise.either
import ch.protonmail.android.mailcommon.domain.model.DataError
import ch.protonmail.android.mailcommon.domain.usecase.IsPaidUser
import ch.protonmail.android.mailcomposer.presentation.R
import dagger.hilt.android.qualifiers.ApplicationContext
import me.proton.core.domain.entity.UserId
import javax.inject.Inject

class GetMobileFooter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val isPaidUser: IsPaidUser
) {

    /**
     * @return localized String with mobile footer and necessary separator or empty String in case we shouldn't show it.
     */
    suspend operator fun invoke(userId: UserId): Either<DataError, String> = either {
        if (isPaidUser(userId).bind()) {
            ""
        } else {
            "\n\n${context.getString(R.string.message_footer_text)}"
        }
    }

}
