package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

@Composable
fun PatternListItem(pattern: Pattern, onPatternClicked: (Pattern) -> Unit) {
    Text(
        text = pattern.name,
        modifier = Modifier
            .padding(16.dp)
            .clickable { onPatternClicked(pattern) }
    )
}
