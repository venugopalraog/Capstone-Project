package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

@Composable
fun FavoriteList(patterns: List<Pattern>, onPatternClicked: (Pattern) -> Unit) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(patterns) {
            Text(
                text = it.name, 
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable { onPatternClicked(it) }
            )
        }
    }
}
