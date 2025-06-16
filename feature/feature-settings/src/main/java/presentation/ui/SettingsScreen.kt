package presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.R as CoreUiR // Alias to avoid conflict if feature_settings.R is also named R
import com.example.feature_settings.R // Import for local feature R class
import org.koin.androidx.compose.koinViewModel
import preference.AppTheme
import presentation.model.ButtonSettingsItem
import presentation.model.ClickableSettingsItem
import presentation.model.SettingsHeader
import presentation.model.SettingsItem
import presentation.model.ToggleSettingsItem
import presentation.viewmodel.SettingsUiState
import presentation.viewmodel.SettingsViewModel
import androidx.compose.runtime.getValue
import presentation.viewmodel.AppThemeState
import presentation.viewmodel.AppThemeViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel(),
    appThemeViewModel: AppThemeViewModel = koinViewModel()
) {
    val themeState by appThemeViewModel.themeState.collectAsState()
    val settingsState by settingsViewModel.settingsState.collectAsState()

    if (settingsState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    SettingsContent(
        uiState = settingsState,
        themeState = themeState,
        onDarkModeChange = appThemeViewModel::onDarkModeChanged,
        onLogout = settingsViewModel::onLogoutClicked,
        onLogin = settingsViewModel::onLoginClicked,
        onAccountInfoClick = settingsViewModel::onAccountInfoClicked
    )
}

@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    themeState: AppThemeState,
    onDarkModeChange: (Boolean) -> Unit,
    onLogout: () -> Unit,
    onLogin: () -> Unit,
    onAccountInfoClick: () -> Unit
) {
    val settingsItems = mutableListOf<SettingsItem>().apply {
        // Account Section
        add(SettingsHeader(stringResource(id = R.string.settings_header_account)))
        if (uiState.isUserLoggedIn) {
            add(
                ClickableSettingsItem(
                    title = uiState.userName ?: stringResource(id = R.string.settings_account_information_fallback),
                    description = uiState.userEmail,
                    icon = Icons.Filled.AccountCircle,
                    onClick = onAccountInfoClick
                )
            )
        }

        // Appearance Section
        add(SettingsHeader(stringResource(id = R.string.settings_header_appearance)))
        add(
            ToggleSettingsItem(
                title = stringResource(id = R.string.settings_dark_mode_title),
                customIconResId = CoreUiR.drawable.dark_mode_ic, // Use alias for core R
                isChecked = themeState.themeMode == UiThemeMode.DARK,
                onCheckedChanged = onDarkModeChange
            )
        )

        // Actions Section
        add(SettingsHeader(stringResource(id = R.string.settings_header_actions)))
        if (uiState.isUserLoggedIn) {
            add(
                ButtonSettingsItem(
                    title = stringResource(id = R.string.settings_logout_button_title),
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    isDestructive = true,
                    onClick = onLogout
                )
            )
        } else {
            add(
                ButtonSettingsItem(
                    title = stringResource(id = R.string.settings_login_button_title), // Corrected to title
                    icon = Icons.Filled.Person,
                    onClick = onLogin
                )
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        items(settingsItems) { item ->
            when (item) {
                is SettingsHeader -> SettingsSectionHeader(title = item.title)
                is ClickableSettingsItem -> ClickableRow(item = item)
                is ToggleSettingsItem -> ToggleRow(item = item)
                is ButtonSettingsItem -> ButtonRow(item = item)
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun ClickableRow(item: ClickableSettingsItem) {
    Surface( // Use Surface for ripple effect on click
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = item.onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            item.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = item.title,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, style = MaterialTheme.typography.bodyLarge)
                item.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
    ListDivider()
}

@Composable
fun ToggleRow(item: ToggleSettingsItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        if (item.icon != null) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
        } else if (item.customIconResId != null) {
            Icon(
                painter = painterResource(id = item.customIconResId),
                contentDescription = item.title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.title, style = MaterialTheme.typography.bodyLarge)
            item.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        Switch(
            checked = item.isChecked,
            onCheckedChange = item.onCheckedChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
    ListDivider()
}

@Composable
fun ButtonRow(item: ButtonSettingsItem) {
    Button(
        onClick = item.onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (item.isDestructive) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (item.isDestructive) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        item.icon?.let {
            Icon(
                imageVector = it, contentDescription = item.title, modifier = Modifier.size(
                    ButtonDefaults.IconSize
                )
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        }
        Text(item.title)
    }
}


@Composable
fun ListDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        thickness = 0.5.dp, // Thinner divider
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f) // Softer color
    )
}

@Preview(showBackground = true, name = "Settings Content Logged In")
@Composable
private fun SettingsContentLoggedInPreview() {
    AppTheme {
        SettingsContent(
            uiState = SettingsUiState(
                isLoading = false,
                isUserLoggedIn = true,
                userName = "Carlos Meisen",
                userEmail = "carlsmeisen@hotmail.com",
            ),
            themeState = AppThemeState(
                themeMode = UiThemeMode.DARK
            ),
            onDarkModeChange = {},
            onLogout = {},
            onLogin = {},
            onAccountInfoClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Settings Content Logged Out")
@Composable
private fun SettingsContentLoggedOutPreview() {
    AppTheme {
        SettingsContent(
            uiState = SettingsUiState(
                isLoading = false,
                isUserLoggedIn = false
            ),
            themeState = AppThemeState(
                themeMode = UiThemeMode.LIGHT
            ),
            onDarkModeChange = {},
            onLogout = {},
            onLogin = {},
            onAccountInfoClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Settings Content Loading")
@Composable
private fun SettingsContentLoadingPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            Text(
                "Preview: Loading State",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}