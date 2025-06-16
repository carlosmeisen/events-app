package presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface SettingsItem {
    val title: String
    val icon: ImageVector?
}

data class ClickableSettingsItem(
    override val title: String,
    override val icon: ImageVector? = null,
    val description: String? = null,
    val onClick: () -> Unit,
    val modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier // Added modifier
) : SettingsItem

data class ToggleSettingsItem(
    override val title: String,
    override val icon: ImageVector? = null, // For system Icons.Filled.*
    @DrawableRes val customIconResId: Int? = null, // For custom painterResource
    val description: String? = null,
    val isChecked: Boolean,
    val onCheckedChanged: (Boolean) -> Unit
) : SettingsItem

data class ButtonSettingsItem(
    override val title: String,
    override val icon: ImageVector? = null,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
) : SettingsItem

data class SettingsHeader(
    override val title: String,
) : SettingsItem {
    override val icon: ImageVector? = null
}
