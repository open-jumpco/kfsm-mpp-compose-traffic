package io.jumpco.open.kfsm.mpp.example.traffic.common

expect class AtomicCounter(value: Long) {
    fun get(): Long
    fun set(value: Long)
    fun getAndSet(value: Long): Long
    fun incrementAndGet(): Long
    fun addAndGet(delta: Long): Long
    fun decrementAndGet(): Long
}
