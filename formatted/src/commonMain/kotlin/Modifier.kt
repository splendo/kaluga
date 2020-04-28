package com.splendo.kaluga.formatted

/**
 * Generic modifier type
 *
 * In some cases It is needed to make manipulations with value stored in [Formatted] type.
 * For instance, it can round date up to half an hour
 */
interface Modifier<T> {
    /**
     * Method creates new value of type T with applied transformation
     */
    fun apply(value: T): T
}