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

package ch.protonmail.android.mailsettings.data.repository

import androidx.datastore.preferences.core.booleanPreferencesKey
import ch.protonmail.android.mailsettings.data.MailSettingsDataStoreProvider
import ch.protonmail.android.mailsettings.domain.model.AutoLockPreference
import ch.protonmail.android.mailsettings.domain.repository.AutoLockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val DEFAULT_VALUE = false

class AutoLockRepositoryImpl @Inject constructor(
    private val dataStoreProvider: MailSettingsDataStoreProvider
) : AutoLockRepository {

    private val hasAutoLockKey = booleanPreferencesKey("hasAutoLockPrefKey")

    override fun observe(): Flow<AutoLockPreference> =
        dataStoreProvider.autoLockDataStore.data.map { prefs ->
            val hasAutoLock = prefs[hasAutoLockKey] ?: DEFAULT_VALUE
            AutoLockPreference(hasAutoLock)
        }
}
