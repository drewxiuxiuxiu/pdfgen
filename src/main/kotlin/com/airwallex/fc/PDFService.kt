package com.airwallex.fc

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.FontStyle
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import com.openhtmltopdf.svgsupport.BatikSVGDrawer
import com.openhtmltopdf.swing.NaiveUserAgent
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class PDFService() {

    companion object {
        const val path: String = "fonts"
        val templateEngine: TemplateEngine = TemplateEngine()
    }

    fun getTemplateStr(path: String): String {
        val url = this.javaClass.getResource(path)
        return String(Files.readAllBytes(Paths.get(url.toURI())))
    }

    fun process(html: String, data: Any): String {
        val context = Context()
        context.setVariable("data", data)
        return templateEngine.process(html, context)
    }

    fun generatePDF(template: PDFTemplate, data: Any): ByteArrayOutputStream {
        return htmlToPDF(this.process(this.getTemplateStr(template.path), data), template.path)
    }

    private fun htmlToPDF(html: String, templatePath: String): ByteArrayOutputStream {
        val os = ByteArrayOutputStream()
        val builder = PdfRendererBuilder()
        builder.useFont(File("$path/FFDIN400.ttf"), "DIN", 400, FontStyle.NORMAL, true)
        builder.useFont(File("$path/FFDIN700.ttf"), "DIN", 700, FontStyle.NORMAL, true)
        builder.withHtmlContent(html, this.javaClass.getResource(templatePath).toExternalForm())
        builder.useUriResolver(NaiveUserAgent.DefaultUriResolver())
        builder.toStream(os)
        builder.useSVGDrawer(BatikSVGDrawer())
        builder.run()

        return os
    }
}

enum class PDFTemplate(val path: String) {
    PAYMENT_CONFIRMATION("/templates/PaymentConfirmation/index.xhtml"),
    TEST("/templates/test.html"),
}

