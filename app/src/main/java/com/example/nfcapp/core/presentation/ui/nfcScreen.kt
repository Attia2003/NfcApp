package com.example.nfcapp.core.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.nfcapp.R
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.nfcapp.core.presentation.state.BackendStatus
import com.example.nfcapp.core.presentation.NfcViewModel
import com.example.nfcapp.core.presentation.state.NfcUiState


@Composable
fun NfcScreen(viewModel: NfcViewModel) {
    val uiState by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (uiState) {
            is NfcUiState.Idle -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.nfc_scan_image),
                        contentDescription = "Put UR CARD NEAR TO PHONE ",
                        modifier = Modifier.size(300.dp)
                    )
                }
            }

            is NfcUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Reading card...",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }

            is NfcUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Error",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = (uiState as NfcUiState.Error).message,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        TextButton(
                            onClick = { viewModel.resetState() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }

            is NfcUiState.Success -> {
                val card = (uiState as NfcUiState.Success).cardData
                val backendStatus = (uiState as NfcUiState.Success).backendStatus
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        InfoCard(
                            icon = Icons.Outlined.Person,
                            label = "Full Name",
                            value = card.fullName
                        )
                    }

                    item {
                        StatusCard(
                            status = "Active Session",
                            active = true
                        )
                    }


                    item {
                        when (backendStatus) {
                            is BackendStatus.Success -> {
                                StatusCard(
                                    status = "Sending to backend...",
                                    active = true
                                )
                            }
                            is BackendStatus.Success -> {
                                StatusCard(
                                    status = "✓ Sent successfully",
                                    active = true
                                )
                            }
                            is BackendStatus.Success -> {
                                StatusCard(
                                    status = "✗ Failed to send",
                                    active = false
                                )
                            }


                            else -> {

                            }
                        }
                    }

                    item {
                        InfoCard(
                            icon = Icons.Outlined.Business,
                            label = "Department",
                            value = card.department
                        )
                    }

                    item {
                        InfoCard(
                            icon = Icons.Outlined.Phone,
                            label = "Phone Number",
                            value = card.section
                        )
                    }

                    item {
                        InfoCard(
                            icon = Icons.Outlined.CalendarMonth,
                            label = "Join Date",
                            value = card.joinDate
                        )
                    }

                    item {
                        InfoCard(
                            icon = Icons.Outlined.LocationOn,
                            label = "Office",
                            value = card.office
                        )
                    }

                    item {
                        InfoCard(
                            icon = Icons.Outlined.Title,
                            label = "Title",
                            value = card.title
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Preview
@Composable
fun NfcScreenPreview() {
    com.example.nfcapp.ui.theme.NfcAppTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            item {
                InfoCard(
                    icon = Icons.Outlined.Person,
                    label = "Full Name",
                    value = "Imad Ziad Abukha dra"
                )
            }


            item {
                InfoCard(
                    icon = Icons.Outlined.Business,
                    label = "Department",
                    value = "Immigration Sol"
                )
            }

            item {
                InfoCard(
                    icon = Icons.Outlined.Phone,
                    label = "Phone Number",
                    value = "01024698963"
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
                    icon = Icons.Outlined.CalendarMonth,
                    label = "Join Date",
                    value = "15/10/2018"
                )
            }

            item {
                InfoCard(
                    icon = Icons.Outlined.LocationOn,
                    label = "Office",
                    value = "Abu Dhabi Office"
                )
            }

            item {
                InfoCard(
                    icon = Icons.Outlined.Title,
                    label = "Title",
                    value = "Software Engineer"
                )
            }
        }
    }
}