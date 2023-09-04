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

package ch.protonmail.android.composer.data.usecase

import me.proton.core.crypto.common.pgp.EncryptedMessage
import me.proton.core.crypto.common.pgp.exception.CryptoException
import me.proton.core.key.domain.entity.key.PublicKey
import me.proton.core.key.domain.entity.keyholder.KeyHolderContext
import timber.log.Timber

/**
 * Encrypts the [text] with [forPublicKey] and signs it with [KeyHolderContext]'s Primary Key.
 */
fun KeyHolderContext.encryptAndSignText(text: String, forPublicKey: PublicKey): EncryptedMessage? {
    return this.privateKeyRing.unlockedPrimaryKey.unlockedKey.use { unlockedPrimaryKey ->
        try {
            val split = context.pgpCrypto.encryptAndSignText(
                text,
                forPublicKey.key,
                unlockedPrimaryKey.value
            )
            split
        } catch (e: CryptoException) {
            Timber.e("Exception in encryptAndSignText", e)
            null
        }
    }
}
