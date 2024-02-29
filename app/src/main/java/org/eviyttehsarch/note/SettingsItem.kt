package org.eviyttehsarch.note

typealias LocationValue = Pair<Float, Float>
typealias StyleValue = SettingsItem.Style.StyleValue

interface SettingsItem<T> {
    val key: String

    val defaultValue: T

    data object Style : SettingsItem<Style.StyleValue> {
        override val key: String = "Style"
        override val defaultValue: StyleValue = StyleValue.StaggeredGrid

        enum class StyleValue {
            Vertical, StaggeredGrid;
            override fun toString(): String {
                return when (this) {
                    Vertical -> "Vertical"
                    StaggeredGrid -> "Staggered grid"
                }
            }
        }
    }

    data object Location : SettingsItem<LocationValue> {
        override val key: String = "Location"
        override val defaultValue: LocationValue = 0f to 0f
    }
}

fun String.toStyleOrDefault(): SettingsItem.Style.StyleValue {
    return when (this) {
        SettingsItem.Style.StyleValue.Vertical.toString() -> SettingsItem.Style.StyleValue.Vertical
        SettingsItem.Style.StyleValue.StaggeredGrid.toString() -> SettingsItem.Style.StyleValue.StaggeredGrid
        else -> SettingsItem.Style.defaultValue
    }
}

fun String.toLocationOrDefault(): Pair<Float, Float> {
    val regex = Regex("\\((-?\\d+\\.\\d+), (-?\\d+\\.\\d+)\\)")
    val matchResult = regex.find(this)
    return if (matchResult != null && matchResult.groupValues.size == 3) {
        val firstFloat = matchResult.groupValues[1].toFloatOrNull() ?: 0f
        val secondFloat = matchResult.groupValues[2].toFloatOrNull() ?: 0f
        Pair(firstFloat, secondFloat)
    } else SettingsItem.Location.defaultValue
}