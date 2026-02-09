package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("About This App", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "This app is a capstone project designed to demonstrate a complete refactoring from a traditional Android architecture to a modern, MVI-based approach using Jetpack Compose. It serves as a practical tutorial for various design patterns.",
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
