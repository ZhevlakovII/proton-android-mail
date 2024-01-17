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

package ch.protonmail.android.mailcontact.data

import arrow.core.Either
import arrow.core.raise.either
import ch.protonmail.android.mailcontact.data.local.ContactDetailLocalDataSource
import ch.protonmail.android.mailcontact.data.remote.ContactDetailRemoteDataSource
import ch.protonmail.android.mailcontact.domain.repository.ContactDetailRepository
import ch.protonmail.android.mailcontact.domain.repository.ContactDetailRepository.ContactDetailErrors
import me.proton.core.contact.domain.entity.ContactId
import me.proton.core.domain.entity.UserId
import javax.inject.Inject

class ContactDetailRepositoryImpl @Inject constructor(
    private val contactDetailLocalDataSource: ContactDetailLocalDataSource,
    private val contactDetailRemoteDataSource: ContactDetailRemoteDataSource
) : ContactDetailRepository {

    override suspend fun deleteContact(userId: UserId, contactId: ContactId): Either<ContactDetailErrors, Unit> =
        either {
            Either.catch { contactDetailLocalDataSource.deleteContact(contactId) }
                .mapLeft { ContactDetailErrors.ContactDetailLocalDataSourceError }
                .bind()
            contactDetailRemoteDataSource.deleteContact(userId, contactId)
        }
}
