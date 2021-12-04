package io.jumpco.open.kfsm.mpp.example.traffic.common

actual class AtomicCounter actual constructor(value: Long) {
    private var counter = value
    actual fun get(): Long = counter
    actual fun set(value: Long) {
        counter = value
    }
    actual fun getAndSet(value: Long): Long {
        val result = counter
        counter = value
        return result
    }

    actual fun incrementAndGet(): Long = ++counter
    actual fun addAndGet(delta: Long): Long = {
        counter += delta
        return counter
    }

    actual fun decrementAndGet(): Long = --counter
}
