package org.eviyttehsarch.note

interface SettingsItem<T> {
    val key: String
    val values: List<T>
    val defaultValue: T

    data object Style : SettingsItem<Style.Value> {
        override val key: String = "Style"
        override val values: List<Value> = Value.entries
        override val defaultValue: Value = Value.StaggeredGrid

        enum class Value {
            Vertical, StaggeredGrid;

            override fun toString(): String {
                return when (this) {
                    Vertical -> "Vertical"
                    StaggeredGrid -> "Staggered grid"
                }
            }


        }
    }
}
fun String.toStyleOrDefault(): SettingsItem.Style.Value {
    return when (this) {
        SettingsItem.Style.Value.Vertical.toString() -> SettingsItem.Style.Value.Vertical
        SettingsItem.Style.Value.StaggeredGrid.toString() -> SettingsItem.Style.Value.StaggeredGrid
        else -> SettingsItem.Style.defaultValue
    }
}