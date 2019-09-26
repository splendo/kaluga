package com.splendo.mpp.util
/**
 * Returns the enum by matching ordinals position, or the given default value if no ordinals match.
 *
 * @param ordinal The ordinal value in this enum
 * @param defaultValue The default value to return if the ordinal is out of range
 *
 * @return The enum value matching the ordinal, or the default.
 *
 */
inline fun <reified T: Enum<T>> Enum.Companion.byOrdinalOrDefault(ordinal:Int, defaultValue:T): T {
    return if (ordinal !in enumValues<T>().indices)
        defaultValue
    else
        enumValues<T>()[ordinal]
}