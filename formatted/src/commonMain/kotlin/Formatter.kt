package com.splendo.kaluga.formatted

/**
 * Generic formatter type
 *
 * This general interface for a class which aimed to co convert type T to string and back
 */
interface Formatter<T> {
    /**
     * Converts a string to value of type T
     */
    fun value(string: String): T

    /**
     * Converts value of type T to a string
     */
    fun string(value: T): String
}
