package com.airwallex.fc

import com.airwallex.fc.pojo.Test
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import com.aliyun.fc.runtime.Context
import com.aliyun.fc.runtime.StreamRequestHandler
import com.aliyun.fc.runtime.FunctionInitializer
import java.io.InputStreamReader
import java.io.BufferedReader

class App : StreamRequestHandler, FunctionInitializer {

    companion object {
        var pdfService: PDFService? = null
        var utils: Utils? = null
    }

    @Throws(IOException::class)
    override fun initialize(context: Context) {
        App.pdfService = PDFService()
        App.utils = Utils()
    }

    @Throws(IOException::class)
    override fun handleRequest(
        inputStream: InputStream, outputStream: OutputStream, context: Context
    ) {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val result = StringBuilder()
//        var line: String
        reader.lines().forEach {line -> result.append(line)}
//        println(result.toString())
        val resultStr = result.toString()
        val json = utils!!.str2Pojo<Test>(resultStr)
        outputStream.write(json.test!!.toString().toByteArray())

//        val pdfStream = pdfService!!.generatePDF(PDFTemplate.TEST, {)
//        outputStream.write(pdfStream.toByteArray())
    }

}
