package com.example.nfcapp.core.data.repository

import android.nfc.Tag
import com.example.nfcapp.core.data.source.ReadDataSource
import com.example.nfcapp.core.domain.model.CardData
import com.example.nfcapp.core.domain.repository.NfcReadRepository
import com.example.nfcapp.util.mergeNames


class NfcRepositoryImpl(
    private val source: ReadDataSource
) : NfcReadRepository {

    override suspend fun readClassicCard(tag: Tag): CardData {
        val firstName = source.readAscii(tag, 1, 0)
        val secondName = source.readAscii(tag, 3, 1)
        val fullName = mergeNames(firstName, secondName)

        return CardData(
            uid = tag.id.joinToString("") { "%02X".format(it) },
            fullName=fullName,
            id = source.readAscii(tag, 1, 1),
            department = source.readAscii(tag, 1, 2),
            section = source.readAscii(tag, 2, 0),
            office = source.readAscii(tag, 2, 1),
            title = source.readAscii(tag, 2, 2),
            joinDate = source.readAscii(tag, 3, 0)
        )
    }
}