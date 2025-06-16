package presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.feature_calendar.R

@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = stringResource(id = R.string.calendar_screen_title))
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen()
}