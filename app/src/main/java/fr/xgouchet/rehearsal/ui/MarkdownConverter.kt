package fr.xgouchet.rehearsal.ui

import android.text.Html
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

object MarkdownConverter {

    private val flavour = CommonMarkFlavourDescriptor()
    private val parser = MarkdownParser(flavour)

    fun parse(text : String) : CharSequence {
        val tree = parser.buildMarkdownTreeFromString(text)
        val html = HtmlGenerator(text, tree, flavour).generateHtml()
        return Html.fromHtml(html)
    }
}
