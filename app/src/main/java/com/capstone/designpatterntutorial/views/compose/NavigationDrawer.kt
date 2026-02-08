package com.capstone.designpatterntutorial.views.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawer(
    onFavoritesClicked: () -> Unit,
    onRecentsClicked: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Favorites", modifier = Modifier.clickable { onFavoritesClicked() })
        Text("Recents", modifier = Modifier.padding(top = 16.dp).clickable { onRecentsClicked() })
    }
}
