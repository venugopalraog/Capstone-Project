package com.capstone.designpatterntutorial.views.compose

import android.graphics.Typeface
import android.text.Html
import android.text.style.StyleSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    Text(
        text = html.parseAsHtml(),
        modifier = modifier
    )
}

@Composable
private fun String.parseAsHtml(): AnnotatedString {
    val spanned = Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    return buildAnnotatedString {
        append(spanned.toString())
        spanned.getSpans(0, spanned.length, Any::class.java).forEach { span ->
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)
            when (span) {
                is StyleSpan -> {
                    when (span.style) {
                        Typeface.BOLD -> {
                            addStyle(MaterialTheme.typography.bodyLarge.toSpanStyle(), start, end)
                        }
                        Typeface.ITALIC -> {
                            addStyle(MaterialTheme.typography.bodyMedium.toSpanStyle(), start, end)
                        }
                    }
                }
            }
        }
    }
}
