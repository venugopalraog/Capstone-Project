package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternListItem(pattern: Pattern, query: String, onPatternClicked: (Pattern) -> Unit) {
    val annotatedString = buildAnnotatedString {
        val startIndex = pattern.name.indexOf(query, ignoreCase = true)
        if (startIndex != -1) {
            append(pattern.name.substring(0, startIndex))
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(pattern.name.substring(startIndex, startIndex + query.length))
            }
            append(pattern.name.substring(startIndex + query.length))
        } else {
            append(pattern.name)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = { onPatternClicked(pattern) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = annotatedString,
            modifier = Modifier.padding(16.dp)
        )
    }
}
