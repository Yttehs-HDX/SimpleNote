package org.eviyttehsarch.note

typealias LocationValue = Pair<Float, Float>
typealias StyleValue = SettingsItem.Style.StyleValue
typealias DateFormatValue = SettingsItem.DateFormat.DateFormatValue

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

    data object DateFormat : SettingsItem<DateFormat.DateFormatValue> {
        override val key: String = "DateFormat"
        override val defaultValue: DateFormatValue = DateFormatValue.Ordinary

        enum class DateFormatValue {
            Simple, Ordinary, Complex;

            fun toUiState(): String {
                return when (this) {
                    Simple -> "Simple"
                    Ordinary -> "Ordinary"
                    Complex -> "Complex"
                }
            }

            override fun toString(): String {
                return when (this) {
                    Simple -> "yy-MM-dd HH:mm"
                    Ordinary -> "yy-MM-dd EEEE HH:mm"
                    Complex -> "EEEE MMM dd yyyy hh:mm:ss"
                }
            }
        }
    }

    data object Location : SettingsItem<LocationValue> {
        override val key: String = "Location"
        override val defaultValue: LocationValue = 0f to 0f
    }
}

fun String.toStyleOrDefault(): StyleValue {
    return when (this) {
        StyleValue.Vertical.toString() -> StyleValue.Vertical
        StyleValue.StaggeredGrid.toString() -> StyleValue.StaggeredGrid
        else -> SettingsItem.Style.defaultValue
    }
}

fun String.toDateFormatOrDefault(): DateFormatValue {
    return when (this) {
        DateFormatValue.Simple.toString() -> DateFormatValue.Simple
        DateFormatValue.Complex.toString() -> DateFormatValue.Complex
        else -> DateFormatValue.Ordinary
    }
}

fun String.toLocationOrDefault(): LocationValue {
    val regex = Regex("\\((-?\\d+\\.\\d+), (-?\\d+\\.\\d+)\\)")
    val matchResult = regex.find(this)
    return if (matchResult != null && matchResult.groupValues.size == 3) {
        val firstFloat = matchResult.groupValues[1].toFloatOrNull() ?: 0f
        val secondFloat = matchResult.groupValues[2].toFloatOrNull() ?: 0f
        Pair(firstFloat, secondFloat)
    } else SettingsItem.Location.defaultValue
}