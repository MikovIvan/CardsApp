package ru.mikov.test.ui.delegates

import ru.mikov.test.ui.base.Binding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class RenderProp<T : Any>(
    var value: T,
    private val needInit: Boolean = true,
    private val onChange: ((T) -> Unit)? = null
) : ReadWriteProperty<Binding, T> {
    private val listeners: MutableList<() -> Unit> = mutableListOf()

    fun bind() {
        if (needInit) onChange?.invoke(value)
    }

    operator fun provideDelegate(
        thisRef: Binding,
        property: KProperty<*>
    ): ReadWriteProperty<Binding, T> {
        val delegate = RenderProp(value, needInit, onChange)
        registerDelegate(thisRef, property.name, delegate)
        return delegate
    }

    override fun getValue(thisRef: Binding, property: KProperty<*>): T = value

    override fun setValue(thisRef: Binding, property: KProperty<*>, value: T) {
        if (value == this.value) return
        this.value = value
        onChange?.invoke(this.value)
        if (listeners.isNotEmpty()) listeners.forEach { it.invoke() }
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    private fun registerDelegate(thisRef: Binding, name: String, delegate: RenderProp<T>) {
        thisRef.delegates[name] = delegate
    }
}