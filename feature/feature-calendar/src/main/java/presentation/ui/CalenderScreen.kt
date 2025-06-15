package presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = "Calendar Screen")
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen()
}