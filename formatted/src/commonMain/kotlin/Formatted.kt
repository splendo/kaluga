package com.splendo.kaluga.formatted

/**
 * Generic formatted type
 *
 * This is general interface for a formatted type which aimed to be used as an UI representation for type [T].
 * For instance FormattedDateTime is UI representation of DateTime class.
 *
 * @param T Type which represents formatted type
 * @param Self References to it's self. Needed to be able to reference on a protocol level to concrete type of class which this protocol implemented. For instance in methods like [copy] of [new]
 *
 * @property value Raw value which should be represented
 * @property formatter [Formatter] which should be used to represent [value]
 * @property modifier Optional property can be used if there are some limitation for value which should be formatted. For instance we can show 0 length if it is negative
 */
interface Formatted <T, Self: Formatted<T, Self>> {
    val value: T?
    val formatter: Formatter<T>
    val modifier: Modifier<T>?

    /**
     * Factory method for concrete formatted type
     *
     * This method was introduced as a replacement for constructor in interface formatted
     * The most common implementation will be just call constructor with the same parameters
     * In future versions automatic generation of this method will be implemented
     *
     * @return Concrete formatter
     */
    fun spawn(value: T?, formatter: Formatter<T>, modifier: Modifier<T>?): Self

    /**
     * String representation of [value]
     */
    fun formatted(): String = value?.let { formatter.string(it) } ?: ""

    /**
     * Creates new instance of formatted type with new value and the same formatter and modifier
     * Use this method instead of mutability if you need to update value if formatted type
     */
    fun new(value: T?): Self = spawn(
        value = value?.let { modifier?.apply(it) ?: value },
        formatter = this.formatter,
        modifier = this.modifier
    )
}

