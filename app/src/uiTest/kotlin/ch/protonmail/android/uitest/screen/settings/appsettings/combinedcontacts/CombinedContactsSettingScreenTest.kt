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

package ch.protonmail.android.uitest.screen.settings.appsettings.combinedcontacts

import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import ch.protonmail.android.mailsettings.presentation.settings.combinedcontacts.TEST_TAG_COMBINED_CONTACTS_TOGGLE_ITEM
import ch.protonmail.android.mailsettings.presentation.settings.combinedcontacts.CombinedContactsSettingScreen
import ch.protonmail.android.mailsettings.presentation.settings.combinedcontacts.CombinedContactsSettingState
import me.proton.core.compose.theme.ProtonTheme
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class CombinedContactsSettingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSwitchIsCheckedIfCombinedContactsSettingIsEnabled() {
        setupScreenWithState(
            CombinedContactsSettingState.Data(isEnabled = true)
        )

        composeTestRule
            .onNodeWithTag(TEST_TAG_COMBINED_CONTACTS_TOGGLE_ITEM)
            .assertIsOn()
    }

    @Test
    fun testSwitchIsNotCheckedIfCombinedContactsSettingIsNotEnabled() {
        setupScreenWithState(
            CombinedContactsSettingState.Data(isEnabled = false)
        )

        composeTestRule
            .onNodeWithTag(TEST_TAG_COMBINED_CONTACTS_TOGGLE_ITEM)
            .assertIsOff()
    }

    @Test
    fun testCallbackIsInvokedWhenSwitchIsToggled() {
        var isEnabled = false
        setupScreenWithState(
            state = CombinedContactsSettingState.Data(isEnabled = false),
            onToggle = { isEnabled = !isEnabled }
        )

        composeTestRule
            .onNodeWithTag(TEST_TAG_COMBINED_CONTACTS_TOGGLE_ITEM)
            .performClick()

        assertEquals(true, isEnabled)
    }

    private fun setupScreenWithState(
        state: CombinedContactsSettingState.Data,
        onBackClick: () -> Unit = {},
        onToggle: (Boolean) -> Unit = {}
    ) {
        composeTestRule.setContent {
            ProtonTheme {
                CombinedContactsSettingScreen(
                    onBackClick = onBackClick,
                    onToggle = onToggle,
                    state = state
                )
            }
        }
    }
}
