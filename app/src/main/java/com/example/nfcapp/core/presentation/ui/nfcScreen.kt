package com.example.nfcapp.core.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcapp.core.presentation.NfcViewModel



@Composable
fun NfcScreen(viewModel: NfcViewModel) {

    val card by viewModel.state.collectAsState()


    if (card == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Tap a MIFARE Classic card")
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            InfoCard(
                icon = Icons.Outlined.Person,
                label = "Full Name",
                value = card!!.fullName
            )
        }

        item {
            StatusCard(
                status = "Active Session",
                active = true
            )
        }

        item {
            InfoCard(
                icon = Icons.Outlined.Business,
                label = "Department",
                value = card!!.department
            )
        }

        item {
            InfoCard(
                icon = Icons.Outlined.Phone,
                label = "Phone Number",
                value = card!!.office
            ) {
                Icon(
                    Icons.Outlined.ChevronRight,
                    contentDescription = null
                )
            }
        }

        item {
            InfoCard(
                icon = Icons.Outlined.CalendarMonth,
                label = "Join Date",
                value = card!!.joinDate
            )
        }
    }

}


@Preview()
@Composable
fun InfoCardPreview() {
    InfoCard(
        icon = Icons.Outlined.Person,
        label = "Full Name",
        value = "Mahmoud Attia"
    )
}