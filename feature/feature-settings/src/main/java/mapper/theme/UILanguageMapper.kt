package mapper.theme

import language.LanguageOption
import presentation.model.LanguageUIModel


fun LanguageOption.toUIModel(currentSelectedCode: String): LanguageUIModel {
    val displayText = flagEmoji?.let { "$it $displayName" } ?: displayName
    return LanguageUIModel(
        code = this.code,
        displayText = displayText,
        isSelected = this.code == currentSelectedCode
    )
}

fun List<LanguageOption>.toUIModel(currentSelectedCode: String): List<LanguageUIModel> {
    return this.map { it.toUIModel(currentSelectedCode) }
}