package presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feature_settings.R
import org.koin.androidx.compose.koinViewModel
import presentation.viewmodel.LanguageViewModel

// Data class to represent a language
data class Language(val code: String, val displayName: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionScreen(
    onNavigateUp: () -> Unit,
    languageViewModel: LanguageViewModel = koinViewModel() // Injected ViewModel
) {
    val languageState by languageViewModel.languageState.collectAsState()
    val currentLanguageCode = languageState.currentLanguageCode

    val languages = listOf(
        Language("en", stringResource(id = R.string.settings_language_english)),
        Language("pt-BR", stringResource(id = R.string.settings_language_brazilian_portuguese))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_language_selection_title),
                        modifier = Modifier.testTag("languageSelectionScreenTitle") // Added testTag
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.settings_content_description_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(languages) { language ->
                LanguageItem(
                    language = language,
                    isSelected = language.code == currentLanguageCode,
                    onLanguageSelected = {
                        languageViewModel.onLanguageSelected(language.code)
                        onNavigateUp() // Navigate back after selection
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onLanguageSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onLanguageSelected)
            .padding(vertical = 16.dp)
            .testTag("languageItem_${language.code}"), // Added testTag, e.g., languageItem_en
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = language.displayName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal // Highlight if selected
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageSelectionScreenPreview() {
    // Preview will need a way to mock LanguageViewModel or use a simpler setup
    // For now, this preview might not reflect the full behavior without Koin.
    MaterialTheme {
        LanguageSelectionScreen(
            onNavigateUp = {}
            // languageViewModel would be typically mocked in a real test environment
        )
    }
}
