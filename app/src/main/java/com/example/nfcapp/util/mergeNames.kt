package com.example.nfcapp.util

fun mergeNames(firstName: String?, secondName: String?): String {
    return listOf(firstName, secondName)
        .map { it?.trim().orEmpty() }
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .replace(Regex("\\s+"), " ")
}