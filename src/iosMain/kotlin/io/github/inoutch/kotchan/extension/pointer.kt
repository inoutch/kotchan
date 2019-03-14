package io.github.inoutch.kotchan.extension

import kotlinx.cinterop.*
import platform.posix.memcpy

@ExperimentalUnsignedTypes
fun List<UInt>.toNative(scope: MemScope): CArrayPointer<UIntVar> {
    val natives = scope.allocArray<UIntVar>(size)

    forEachIndexed { i, x -> natives[i] = x }

    return natives
}

fun List<Int>.toNative(scope: MemScope): CArrayPointer<IntVar> {
    val natives = scope.allocArray<IntVar>(size)

    forEachIndexed { i, x -> natives[i] = x }

    return natives
}

fun List<Float>.toNative(scope: MemScope): CArrayPointer<FloatVar> {
    val natives = scope.allocArray<FloatVar>(size)
    forEachIndexed { i, x -> natives[i] = x }
    return natives
}

@ExperimentalUnsignedTypes
fun List<ULong>.toNative(scope: MemScope): CArrayPointer<ULongVar> {
    val natives = scope.allocArray<ULongVar>(size)
    forEachIndexed { i, x -> natives[i] = x }
    return natives
}

fun <T : CPointed> List<CPointer<T>>.toNative(scope: MemScope): CArrayPointer<CPointerVar<T>> {
    val natives = scope.allocArray<CPointerVar<T>>(size)
    forEachIndexed { index, x -> natives[index] = x }
    return natives
}

@ExperimentalUnsignedTypes
fun ByteArray.copyToUIntPointer(scope: MemScope): CPointer<UIntVar> {
    val p = scope.allocArray<UIntVar>(size / 2 + 1)
    memcpy(p, refTo(0), size.toULong())
    return p
}

fun CPointer<ByteVar>.toByteArray(size: Int) = ByteArray(size).also {
    for (i in 0 until size) {
        it[i] = this[i]
    }
}
