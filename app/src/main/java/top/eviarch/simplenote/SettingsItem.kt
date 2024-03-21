package top.eviarch.simplenote

import top.eviarch.simplenote.core.SimpleNoteApplication

typealias StyleValue = SettingsItem.Style.StyleValue
typealias DateFormatValue = SettingsItem.DateFormat.DateFormatValue
typealias StorageManagerValue = SettingsItem.StorageManager.StorageManagerValue
typealias PositionValue = Pair<Float, Float>

interface SettingsItem<T> {
    val key: String
    val defaultValue: T

    data object Style : SettingsItem<Style.StyleValue> {
        override val key: String = "Style"
        override val defaultValue: StyleValue = StyleValue.StaggeredGrid

        enum class StyleValue {
            Vertical, StaggeredGrid;

            fun toUiState(): String {
                return when (this) {
                    Vertical -> SimpleNoteApplication.Context.getString(R.string.vertical)
                    StaggeredGrid -> SimpleNoteApplication.Context.getString(R.string.staggered_grid)
                }
            }

            override fun toString(): String {
                return when (this) {
                    Vertical -> "Vertical"
                    StaggeredGrid -> "Staggered Grid"
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
                    Simple -> SimpleNoteApplication.Context.getString(R.string.simple)
                    Ordinary -> SimpleNoteApplication.Context.getString(R.string.ordinary)
                    Complex -> SimpleNoteApplication.Context.getString(R.string.complex)
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

    data object StorageManager : SettingsItem<StorageManagerValue> {
        override val key: String = "StorageManager"
        override val defaultValue: StorageManagerValue = StorageManagerValue.Never

        enum class StorageManagerValue {
            Hour1, Day1, Day30, Day90, Day180, Year1, Year2, Year3, Year10, Never;

            fun toUiState(): String {
                return when (this) {
                    Hour1 -> SimpleNoteApplication.Context.getString(R.string.hour_1)
                    Day1 -> SimpleNoteApplication.Context.getString(R.string.day_1)
                    Day30 -> SimpleNoteApplication.Context.getString(R.string.day_30)
                    Day90 -> SimpleNoteApplication.Context.getString(R.string.day_90)
                    Day180 -> SimpleNoteApplication.Context.getString(R.string.day_180)
                    Year1 -> SimpleNoteApplication.Context.getString(R.string.year_1)
                    Year2 -> SimpleNoteApplication.Context.getString(R.string.year_2)
                    Year3 -> SimpleNoteApplication.Context.getString(R.string.year_3)
                    Year10 -> SimpleNoteApplication.Context.getString(R.string.year_10)
                    Never -> SimpleNoteApplication.Context.getString(R.string.never)
                }
            }

            fun toTimeMillis(): Long {
                return when (this) {
                    Hour1 -> 60 * 60 * 1000L
                    Day1 -> 24 * 60 * 60 * 1000L
                    Day30 -> 30 * 24 * 60 * 60 * 1000L
                    Day90 -> 90 * 24 * 60 * 60 * 1000L
                    Day180 -> 180 * 24 * 60 * 60 * 1000L
                    Year1 -> 365 * 24 * 60 * 60 * 1000L
                    Year2 -> 730 * 24 * 60 * 60 * 1000L
                    Year3 -> 1095 * 24 * 60 * 60 * 1000L
                    Year10 -> 3650 * 24 * 60 * 60 * 1000L
                    Never -> -1
                }
            }

            override fun toString(): String {
                return when (this) {
                    Hour1 -> "1h"
                    Day1 -> "1d"
                    Day30 -> "30d"
                    Day90 -> "90d"
                    Day180 -> "1y"
                    Year1 -> "1y"
                    Year2 -> "2y"
                    Year3 -> "3y"
                    Year10 -> "10y"
                    Never -> "n"
                }
            }
        }
    }

    data object Position : SettingsItem<PositionValue> {
        override val key: String = "Position"
        override val defaultValue: PositionValue = 0f to 0f
    }

    data object VerticalPosition : SettingsItem<PositionValue> by Position {
        override val key: String = "Vertical Position"
    }

    data object HorizontalPosition : SettingsItem<PositionValue> by Position {
        override val key: String = "Horizontal Position"
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

fun String.toStorageManagerOrDefault(): StorageManagerValue {
    return when (this) {
        StorageManagerValue.Hour1.toString() -> StorageManagerValue.Hour1
        StorageManagerValue.Day1.toString() -> StorageManagerValue.Day1
        StorageManagerValue.Day30.toString() -> StorageManagerValue.Day30
        StorageManagerValue.Day90.toString() -> StorageManagerValue.Day90
        StorageManagerValue.Day180.toString() -> StorageManagerValue.Day180
        StorageManagerValue.Year1.toString() -> StorageManagerValue.Year1
        StorageManagerValue.Year2.toString() -> StorageManagerValue.Year2
        StorageManagerValue.Year3.toString() -> StorageManagerValue.Year3
        StorageManagerValue.Year10.toString() -> StorageManagerValue.Year10
        else -> StorageManagerValue.Never
    }
}

fun String.toPositionOrDefault(): PositionValue {
    val regex = Regex("\\((-?\\d+\\.\\d+), (-?\\d+\\.\\d+)\\)")
    val matchResult = regex.find(this)
    return if (matchResult != null && matchResult.groupValues.size == 3) {
        val firstFloat = matchResult.groupValues[1].toFloatOrNull() ?: 0f
        val secondFloat = matchResult.groupValues[2].toFloatOrNull() ?: 0f
        firstFloat to secondFloat
    } else SettingsItem.VerticalPosition.defaultValue
}