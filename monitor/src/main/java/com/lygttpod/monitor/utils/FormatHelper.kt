package com.lygttpod.monitor.utils

import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.xml.transform.OutputKeys
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.sax.SAXTransformerFactory
import javax.xml.transform.stream.StreamResult


fun formatBody(body: String, contentType: String?): String {
    return when {
        contentType?.contains("json", true) == true -> formatJson(body)
        contentType?.contains("xml", true) == true -> formatXml(body)
        else -> body
    }
}

private fun formatJson(json: String): String {
    return GsonHelper.setPrettyPrinting(json)
}

private fun formatXml(xml: String): String {
    return try {
        val serializer = SAXTransformerFactory.newInstance().newTransformer()
        serializer.setOutputProperty(OutputKeys.INDENT, "yes")
        serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        val xmlSource = SAXSource(InputSource(ByteArrayInputStream(xml.toByteArray())))
        val res = StreamResult(ByteArrayOutputStream())
        serializer.transform(xmlSource, res)
        String((res.outputStream as ByteArrayOutputStream).toByteArray())
    } catch (e: Exception) {
        xml
    }
}