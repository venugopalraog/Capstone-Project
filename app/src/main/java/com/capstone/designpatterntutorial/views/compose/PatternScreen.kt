package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.capstone.designpatterntutorial.model.mainscreen.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternScreen(
    pattern: Pattern, 
    onBackClicked: () -> Unit,
    onFavoriteClicked: (Pattern) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pattern.name) },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onFavoriteClicked(pattern) }) {
                        Icon(
                            if (pattern.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = pattern.summary)
        }
    }
}
