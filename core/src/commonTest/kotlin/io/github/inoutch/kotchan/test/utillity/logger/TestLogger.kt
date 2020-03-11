package io.github.inoutch.kotchan.test.utillity.logger

class TestLogger {
    data class Record(val callerHashCode: Int, val value: Any)

    private val records = mutableListOf<Record>()

    fun log(caller: Any, any: Any) {
        records.add(Record(caller.hashCode(), any))
    }

    fun clear() {
        records.clear()
    }

    fun printAllLogs() {
        records.forEach { println("${it.callerHashCode}: ${it.value}") }
    }

    fun assertEquals(expected: List<Any>, message: String? = null) {
        kotlin.test.assertEquals(expected, records.map { it.value }, message)
    }

    fun assertEquals(index: Int, expectedValue: Any, message: String? = null) {
        kotlin.test.assertTrue { records.size > index }
        val record = records[index]
        kotlin.test.assertEquals(record.value, expectedValue, message)
    }

    fun assertEquals(caller: Any, index: Int, expectedValue: Any, message: String? = null) {
        kotlin.test.assertTrue { records.size > index }
        val record = records[index]
        kotlin.test.assertEquals(record.callerHashCode, caller.hashCode())
        kotlin.test.assertEquals(record.value, expectedValue, message)
    }
}
