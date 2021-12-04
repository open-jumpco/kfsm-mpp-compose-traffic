package io.jumpco.open.kfsm.mpp.example.traffic.common

import java.util.concurrent.atomic.AtomicLong

actual class AtomicCounter actual constructor(value: Long) {
    private val counter = AtomicLong(value)
    actual fun get(): Long = counter.get()
    actual fun set(value: Long) = counter.set(value)
    actual fun getAndSet(value: Long): Long = counter.getAndSet(value)
    actual fun incrementAndGet(): Long = counter.incrementAndGet()
    actual fun addAndGet(delta: Long): Long = counter.addAndGet(delta)
    actual fun decrementAndGet(): Long = counter.decrementAndGet()
}
