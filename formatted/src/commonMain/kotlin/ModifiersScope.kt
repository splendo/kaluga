package com.splendo.kaluga.formatted

/**
 * Collection of modifiers
 *
 * Class can be used if multiple modification should be applied to value in [Formatted] class
 *
 * @property list list of modifiers
 */
class ModifiersScope<T>(val list: List<Modifier<T>>) : Modifier<T> {
    /**
     * Applies modifiers in the same order as they are in the [list]
     */
    override fun apply(value: T): T {
        var mutableValue = value
        list.forEach {
            mutableValue = it.apply(mutableValue)
        }
        return mutableValue
    }
}
