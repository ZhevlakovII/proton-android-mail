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

package ch.protonmail.android.uitest.e2e.mailbox.detail.conversation

import androidx.test.filters.SdkSuppress
import ch.protonmail.android.di.ServerProofModule
import ch.protonmail.android.networkmocks.mockwebserver.requests.MockPriority
import ch.protonmail.android.networkmocks.mockwebserver.requests.ignoreQueryParams
import ch.protonmail.android.networkmocks.mockwebserver.requests.matchWildcards
import ch.protonmail.android.networkmocks.mockwebserver.requests.respondWith
import ch.protonmail.android.networkmocks.mockwebserver.requests.serveOnce
import ch.protonmail.android.networkmocks.mockwebserver.requests.withPriority
import ch.protonmail.android.networkmocks.mockwebserver.requests.withStatusCode
import ch.protonmail.android.test.annotations.suite.RegressionTest
import ch.protonmail.android.uitest.MockedNetworkTest
import ch.protonmail.android.uitest.helpers.core.TestId
import ch.protonmail.android.uitest.helpers.core.navigation.Destination
import ch.protonmail.android.uitest.helpers.core.navigation.navigator
import ch.protonmail.android.uitest.helpers.login.LoginStrategy
import ch.protonmail.android.uitest.helpers.network.mockNetworkDispatcher
import ch.protonmail.android.uitest.robot.detail.ConversationDetailRobot
import ch.protonmail.android.uitest.robot.mailbox.inbox.InboxRobot
import ch.protonmail.android.uitest.util.UiDeviceHolder.uiDevice
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import me.proton.core.auth.domain.usecase.ValidateServerProof
import org.junit.Test

@RegressionTest
@HiltAndroidTest
@SdkSuppress(minSdkVersion = 28)
@UninstallModules(ServerProofModule::class)
internal class ConversationDetailBottomSheetTests : MockedNetworkTest(loginStrategy = LoginStrategy.LoggedOut) {

    private val inboxRobot = InboxRobot(composeTestRule)
    private val conversationDetailRobot = ConversationDetailRobot(composeTestRule)

    @JvmField
    @BindValue
    val serverProofValidation: ValidateServerProof = mockk(relaxUnitFun = true)

    @Test
    @TestId("79353")
    fun checkConversationMoveToBottomSheetDismissalWithBackButton() {
        mockWebServer.dispatcher = mockNetworkDispatcher(useDefaultMailSettings = false) {
            addMockRequests(
                "/mail/v4/settings"
                    respondWith "/mail/v4/settings/mail-v4-settings_79353.json"
                    withStatusCode 200,
                "/mail/v4/conversations"
                    respondWith "/mail/v4/conversations/conversations_79353.json"
                    withStatusCode 200 ignoreQueryParams true,
                "/mail/v4/conversations/*"
                    respondWith "/mail/v4/conversations/conversation-id/conversation-id_79353.json"
                    withStatusCode 200 matchWildcards true,
                "/mail/v4/messages/*"
                    respondWith "/mail/v4/messages/message-id/message-id_79353.json"
                    withStatusCode 200 matchWildcards true serveOnce true,
                "/mail/v4/messages/read"
                    respondWith "/mail/v4/messages/read/read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest,
                "/mail/v4/conversations/read"
                    respondWith "/mail/v4/conversations/read/conversations_read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest
            )
        }

        navigator {
            navigateTo(Destination.Inbox)
        }

        inboxRobot.clickMessageByPosition(0)

        conversationDetailRobot
            .waitUntilMessageIsShown()
            .openMoveToBottomSheet()
            .verify { moveToBottomSheetExists() }

        // Physical/soft key press is required by this test case.
        uiDevice.pressBack()

        conversationDetailRobot
            .verify { moveToBottomSheetIsDismissed() }
            .verify { conversationDetailScreenIsShown() }
    }

    @Test
    @TestId("79353/2")
    fun checkConversationLabelAsBottomSheetDismissalWithBackButton() {
        mockWebServer.dispatcher = mockNetworkDispatcher(useDefaultMailSettings = false) {
            addMockRequests(
                "/mail/v4/settings"
                    respondWith "/mail/v4/settings/mail-v4-settings_79353.json"
                    withStatusCode 200,
                "/mail/v4/conversations"
                    respondWith "/mail/v4/conversations/conversations_79353.json"
                    withStatusCode 200 ignoreQueryParams true,
                "/mail/v4/conversations/*"
                    respondWith "/mail/v4/conversations/conversation-id/conversation-id_79353.json"
                    withStatusCode 200 matchWildcards true,
                "/mail/v4/messages/*"
                    respondWith "/mail/v4/messages/message-id/message-id_79353.json"
                    withStatusCode 200 matchWildcards true serveOnce true,
                "/mail/v4/messages/read"
                    respondWith "/mail/v4/messages/read/read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest,
                "/mail/v4/conversations/read"
                    respondWith "/mail/v4/conversations/read/conversations_read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest
            )
        }

        navigator {
            navigateTo(Destination.Inbox)
        }

        inboxRobot.clickMessageByPosition(0)

        conversationDetailRobot
            .waitUntilMessageIsShown()
            .openLabelAsBottomSheet()
            .verify { labelAsBottomSheetExists() }

        uiDevice.pressBack()

        conversationDetailRobot
            .verify { labelAsBottomSheetIsDismissed() }
            .verify { conversationDetailScreenIsShown() }
    }

    @Test
    @TestId("79355")
    fun checkConversationMoveToBottomSheetDismissalWithExternalTap() {
        mockWebServer.dispatcher = mockNetworkDispatcher(useDefaultMailSettings = false) {
            addMockRequests(
                "/mail/v4/settings"
                    respondWith "/mail/v4/settings/mail-v4-settings_79355.json"
                    withStatusCode 200,
                "/mail/v4/conversations"
                    respondWith "/mail/v4/conversations/conversations_79355.json"
                    withStatusCode 200 ignoreQueryParams true,
                "/mail/v4/conversations/*"
                    respondWith "/mail/v4/conversations/conversation-id/conversation-id_79355.json"
                    withStatusCode 200 matchWildcards true,
                "/mail/v4/messages/*"
                    respondWith "/mail/v4/messages/message-id/message-id_79355.json"
                    withStatusCode 200 matchWildcards true serveOnce true,
                "/mail/v4/messages/read"
                    respondWith "/mail/v4/messages/read/read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest,
                "/mail/v4/conversations/read"
                    respondWith "/mail/v4/conversations/read/conversations_read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest
            )
        }

        navigator {
            navigateTo(Destination.Inbox)
        }

        inboxRobot.clickMessageByPosition(0)

        conversationDetailRobot
            .waitUntilMessageIsShown()
            .openMoveToBottomSheet()
            .verify { moveToBottomSheetExists() }

        // Tap outside the view.
        conversationDetailRobot.expandHeader()

        conversationDetailRobot
            .verify { moveToBottomSheetIsDismissed() }
            .verify { conversationDetailScreenIsShown() }
    }

    @Test
    @TestId("79355/2")
    fun checkConversationLabelAsBottomSheetDismissalWithExternalTap() {
        mockWebServer.dispatcher = mockNetworkDispatcher(useDefaultMailSettings = false) {
            addMockRequests(
                "/mail/v4/settings"
                    respondWith "/mail/v4/settings/mail-v4-settings_79355.json"
                    withStatusCode 200,
                "/mail/v4/conversations"
                    respondWith "/mail/v4/conversations/conversations_79355.json"
                    withStatusCode 200 ignoreQueryParams true,
                "/mail/v4/conversations/*"
                    respondWith "/mail/v4/conversations/conversation-id/conversation-id_79355.json"
                    withStatusCode 200 matchWildcards true,
                "/mail/v4/messages/*"
                    respondWith "/mail/v4/messages/message-id/message-id_79355.json"
                    withStatusCode 200 matchWildcards true serveOnce true,
                "/mail/v4/messages/read"
                    respondWith "/mail/v4/messages/read/read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest,
                "/mail/v4/conversations/read"
                    respondWith "/mail/v4/conversations/read/conversations_read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest
            )
        }

        navigator {
            navigateTo(Destination.Inbox)
        }

        inboxRobot.clickMessageByPosition(0)

        conversationDetailRobot
            .waitUntilMessageIsShown()
            .openLabelAsBottomSheet()
            .verify { labelAsBottomSheetExists() }

        conversationDetailRobot.expandHeader()

        conversationDetailRobot
            .verify { labelAsBottomSheetIsDismissed() }
            .verify { conversationDetailScreenIsShown() }
    }
}
