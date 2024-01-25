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

package ch.protonmail.android.mailcontact.presentation.contactform

import ch.protonmail.android.mailcommon.presentation.Effect
import ch.protonmail.android.mailcommon.presentation.model.TextUiModel
import ch.protonmail.android.mailcontact.presentation.model.ContactFormUiModel

sealed interface ContactFormState {

    val close: Effect<Unit>
    val isSaveEnabled: Boolean

    data class Loading(
        override val close: Effect<Unit> = Effect.empty(),
        override val isSaveEnabled: Boolean = false,
        val errorLoading: Effect<TextUiModel> = Effect.empty()
    ) : ContactFormState

    sealed interface Data : ContactFormState {

        val contact: ContactFormUiModel
        val closeWithSuccess: Effect<TextUiModel>
        val showErrorSnackbar: Effect<TextUiModel>

        data class Create(
            override val close: Effect<Unit> = Effect.empty(),
            override val isSaveEnabled: Boolean = false,
            override val closeWithSuccess: Effect<TextUiModel> = Effect.empty(),
            override val showErrorSnackbar: Effect<TextUiModel> = Effect.empty(),
            override val contact: ContactFormUiModel,
            val displayCreateLoader: Boolean = false
        ) : Data

        data class Update(
            override val close: Effect<Unit> = Effect.empty(),
            override val isSaveEnabled: Boolean = true,
            override val closeWithSuccess: Effect<TextUiModel> = Effect.empty(),
            override val showErrorSnackbar: Effect<TextUiModel> = Effect.empty(),
            override val contact: ContactFormUiModel
        ) : Data
    }
}

