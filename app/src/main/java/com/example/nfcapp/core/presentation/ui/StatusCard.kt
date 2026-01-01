package com.example.nfcapp.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatusCard(
    status: String,
    active: Boolean
) {
    InfoCard(
        icon = Icons.Outlined.ShowChart,
        label = "Current Status",
        value = status
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (active) {
                            // Use primary color for active status, adapting to theme
                            MaterialTheme.colorScheme.primary
                        } else {
                            // Use error color for inactive status
                            MaterialTheme.colorScheme.error
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}
