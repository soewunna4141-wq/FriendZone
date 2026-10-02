package com.fz.friendzone.feature.choice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChoiceScreen(
    onNewsSelected: () -> Unit = {},
    onReelsSelected: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Choose Your Experience")

        Button(
            onClick = onNewsSelected
        ) {
            Text(text = "News Paper")
        }

        Button(
            onClick = onReelsSelected
        ) {
            Text(text = "Reels")
        }
    }
}
