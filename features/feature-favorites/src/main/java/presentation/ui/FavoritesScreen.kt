package presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.feature.favorites.R

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = stringResource(id = R.string.favorites_screen_title))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FavoritesScreenPreview() {
    FavoritesScreen()
}