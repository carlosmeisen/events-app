package presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import presentation.model.LanguageUIModel
import presentation.viewmodel.LanguageViewModel
import ui.components.ConfirmationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionScreen(
    onNavigateUp: () -> Unit,
    languageViewModel: LanguageViewModel = koinViewModel()
) {
    val languageState by languageViewModel.languageState.collectAsState()

    LaunchedEffect(Unit) {
        languageViewModel.navigateToUp.collectLatest {
            onNavigateUp()
        }
    }

    if (languageState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

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
            items(languageState.availableLanguages, key = { it.code }) { languageOption ->
                LanguageItem(
                    language = languageOption,
                    isSelected = languageOption.code == languageState.currentLanguageCode,
                    onLanguageSelected = {
                        languageViewModel.changeLanguage(languageOption.code)
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    language: LanguageUIModel,
    isSelected: Boolean,
    onLanguageSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onLanguageSelected)
            .padding(vertical = 16.dp)
            .testTag("languageItem_${language.code}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = language.displayText,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageSelectionScreenPreview() {
    MaterialTheme {
        LanguageSelectionScreen(
            onNavigateUp = {}
            // languageViewModel would be typically mocked in a real test environment
        )
    }
}
