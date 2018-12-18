package io.github.inoutch.kotchan.core.view.map.converter

data class ConversionInputData(val input: List<List<Int>>, val sizeX: Int, val sizeY: Int)

data class ConversionOutputData(val x: Int, val y: Int, val v: Int)

data class ConversionData(
        val input: ConversionInputData,
        val output: ConversionOutputData)