package me.bnnq.messenger.extensions

import java.util.UUID

fun UUID.uniqueActionIdentifier(userId: String?): String
{
    return "${userId ?: "0"}-$mostSignificantBits"
}