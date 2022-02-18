/*
 * Copyright (c) 2021 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonMail.
 *
 * ProtonMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonMail.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.protonmail.android

import me.proton.core.featureflag.domain.entity.FeatureId

/**
 * This class contains all the feature flags that are used by the Mail client.
 * @param defaultLocalValue will be true for debug builds and false for any other build type
 * unless differently specified
 */
enum class MailFeatureFlags(val id: FeatureId, val defaultLocalValue: Boolean = BuildConfig.DEBUG) {

    // Remote flags
    ConversationMode(FeatureId("ThreadingAndroid")),

    // Local only flag (unknown to remote API)
    ShowSettings(FeatureId("ShowSettings"));
}
