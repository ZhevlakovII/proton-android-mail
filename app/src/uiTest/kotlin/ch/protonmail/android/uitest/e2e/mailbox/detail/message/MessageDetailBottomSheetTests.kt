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

package ch.protonmail.android.uitest.e2e.mailbox.detail.message

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
import ch.protonmail.android.uitest.robot.detail.messageDetailRobot
import ch.protonmail.android.uitest.robot.detail.section.bottomSheetSection
import ch.protonmail.android.uitest.robot.detail.section.messageBodySection
import ch.protonmail.android.uitest.robot.detail.section.messageHeaderSection
import ch.protonmail.android.uitest.robot.detail.section.verify
import ch.protonmail.android.uitest.robot.detail.verify
import ch.protonmail.android.uitest.util.UiDeviceHolder.uiDevice
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import me.proton.core.auth.domain.usecase.ValidateServerProof
import org.junit.Test

@RegressionTest
@HiltAndroidTest
@UninstallModules(ServerProofModule::class)
internal class MessageDetailBottomSheetTests : MockedNetworkTest(loginStrategy = LoginStrategy.LoggedOut) {

    @JvmField
    @BindValue
    val serverProofValidation: ValidateServerProof = mockk(relaxUnitFun = true)

    @Test
    @TestId("79354")
    fun checkMessageMoveToBottomSheetDismissalWithBackButton() {
        mockWebServer.dispatcher = mockNetworkDispatcher(useDefaultMailSettings = false) {
            addMockRequests(
                "/mail/v4/settings"
                    respondWith "/mail/v4/settings/mail-v4-settings_79354.json"
                    withStatusCode 200,
                "/mail/v4/messages"
                    respondWith "/mail/v4/messages/messages_79354.json"
                    withStatusCode 200 ignoreQueryParams true,
                "/mail/v4/messages/*"
                    respondWith "/mail/v4/messages/message-id/message-id_79354.json"
                    withStatusCode 200 matchWildcards true serveOnce true,
                "/mail/v4/messages/read"
                    respondWith "/mail/v4/messages/read/read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest
            )
        }

        navigator {
            navigateTo(Destination.MailDetail(messagePosition = 0))
        }

        messageDetailRobot {
            messageBodySection { waitUntilMessageIsShown() }

            bottomSheetSection {
                openMoveToBottomSheet()

                verify { moveToBottomSheetExists() }
            }
        }

        // Physical/soft key press is required by this test case.
        uiDevice.pressBack()

        messageDetailRobot {
            bottomSheetSection {
                verify { moveToBottomSheetIsDismissed() }
            }

            verify { messageDetailScreenIsShown() }
        }
    }

    @Test
    @TestId("79354/2")
    fun checkMessageLabelAsBottomSheetDismissalWithBackButton() {
        mockWebServer.dispatcher = mockNetworkDispatcher(useDefaultMailSettings = false) {
            addMockRequests(
                "/mail/v4/settings"
                    respondWith "/mail/v4/settings/mail-v4-settings_79354.json"
                    withStatusCode 200,
                "/mail/v4/messages"
                    respondWith "/mail/v4/messages/messages_79354.json"
                    withStatusCode 200 ignoreQueryParams true,
                "/mail/v4/messages/*"
                    respondWith "/mail/v4/messages/message-id/message-id_79354.json"
                    withStatusCode 200 matchWildcards true serveOnce true,
                "/mail/v4/messages/read"
                    respondWith "/mail/v4/messages/read/read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest
            )
        }

        navigator {
            navigateTo(Destination.MailDetail(messagePosition = 0))
        }

        messageDetailRobot {
            messageBodySection { waitUntilMessageIsShown() }

            bottomSheetSection {
                openLabelAsBottomSheet()

                verify { labelAsBottomSheetExists() }
            }
        }

        uiDevice.pressBack()

        messageDetailRobot {
            bottomSheetSection {
                verify { labelAsBottomSheetIsDismissed() }
            }

            verify { messageDetailScreenIsShown() }
        }
    }

    @Test
    @TestId("79356")
    fun checkMessageMoveToBottomSheetDismissalWithExternalTap() {
        mockWebServer.dispatcher = mockNetworkDispatcher(useDefaultMailSettings = false) {
            addMockRequests(
                "/mail/v4/settings"
                    respondWith "/mail/v4/settings/mail-v4-settings_79356.json"
                    withStatusCode 200,
                "/mail/v4/messages"
                    respondWith "/mail/v4/messages/messages_79356.json"
                    withStatusCode 200 ignoreQueryParams true,
                "/mail/v4/messages/*"
                    respondWith "/mail/v4/messages/message-id/message-id_79356.json"
                    withStatusCode 200 matchWildcards true serveOnce true,
                "/mail/v4/messages/read"
                    respondWith "/mail/v4/messages/read/read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest
            )
        }

        navigator {
            navigateTo(Destination.MailDetail(messagePosition = 0))
        }

        messageDetailRobot {
            messageBodySection { waitUntilMessageIsShown() }

            bottomSheetSection {
                openMoveToBottomSheet()

                verify { moveToBottomSheetExists() }
            }

            // Tap outside the view.
            messageHeaderSection { expandHeader() }

            bottomSheetSection {
                verify { moveToBottomSheetIsDismissed() }
            }

            verify { messageDetailScreenIsShown() }
        }
    }

    @Test
    @TestId("79356/2")
    fun checkMessageLabelAsBottomSheetDismissalWithExternalTap() {
        mockWebServer.dispatcher = mockNetworkDispatcher(useDefaultMailSettings = false) {
            addMockRequests(
                "/mail/v4/settings"
                    respondWith "/mail/v4/settings/mail-v4-settings_79356.json"
                    withStatusCode 200,
                "/mail/v4/messages"
                    respondWith "/mail/v4/messages/messages_79356.json"
                    withStatusCode 200 ignoreQueryParams true,
                "/mail/v4/messages/*"
                    respondWith "/mail/v4/messages/message-id/message-id_79356.json"
                    withStatusCode 200 matchWildcards true serveOnce true,
                "/mail/v4/messages/read"
                    respondWith "/mail/v4/messages/read/read_base_placeholder.json"
                    withStatusCode 200 withPriority MockPriority.Highest
            )
        }

        navigator {
            navigateTo(Destination.MailDetail(messagePosition = 0))
        }

        messageDetailRobot {
            bottomSheetSection {
                openLabelAsBottomSheet()

                verify { labelAsBottomSheetExists() }
            }

            // Tap outside the view.
            messageHeaderSection { expandHeader() }

            bottomSheetSection {
                verify { labelAsBottomSheetIsDismissed() }
            }

            verify { messageDetailScreenIsShown() }
        }
    }
}
