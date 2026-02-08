package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.capstone.designpatterntutorial.model.mainscreen.Category
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

@Composable
fun CategoryList(categories: List<Category>, onPatternClicked: (Pattern) -> Unit) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        categories.forEach { category ->
            item {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(category.patternList) {
                Text(
                    text = it.name, 
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                        .clickable { onPatternClicked(it) }
                )
            }
        }
    }
}
