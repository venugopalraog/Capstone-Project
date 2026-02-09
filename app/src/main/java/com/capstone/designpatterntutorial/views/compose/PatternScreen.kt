package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

@Composable
fun PatternScreen(
    pattern: Pattern
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            HtmlText(html = pattern.summary)
        }
    }
}
