package ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import preference.AppTheme

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    denyButtonText: String,
    onConfirm: () -> Unit,
    onDeny: () -> Unit,
    dismissible: Boolean = true,
    onDismissRequest: () -> Unit
) {
    if (dismissible) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    onDismissRequest() // Dismiss after action
                }) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDeny()
                    onDismissRequest() // Dismiss after action
                }) {
                    Text(denyButtonText)
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = { /* Cannot be dismissed by clicking outside */ },
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    // Do not dismiss here for non-dismissable dialogs
                    // The caller is responsible for hiding the dialog
                }) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDeny()
                    // Do not dismiss here for non-dismissable dialogs
                    // The caller is responsible for hiding the dialog
                }) {
                    Text(denyButtonText)
                }
            }
        )
    }
}

@Preview(name = "Dismissable Dialog")
@Composable
fun ConfirmationDialogPreviewDismissible() {
    AppTheme {
        ConfirmationDialog(
            title = "Confirm Action",
            message = "Are you sure you want to perform this action? This can be dismissed.",
            confirmButtonText = "Confirm",
            denyButtonText = "Deny",
            onConfirm = {},
            onDeny = {},
            dismissible = true,
            onDismissRequest = {}
        )
    }
}

@Preview(name = "Non-Dismissable Dialog")
@Composable
fun ConfirmationDialogPreviewNonDismissible() {
    AppTheme {
        ConfirmationDialog(
            title = "Important Choice",
            message = "This is a critical decision. You must choose an option. This dialog cannot be dismissed by clicking outside.",
            confirmButtonText = "Accept",
            denyButtonText = "Reject",
            onConfirm = {},
            onDeny = {},
            dismissible = false,
            onDismissRequest = {} // This won't be called by AlertDialog if dismissable is false
        )
    }
}
