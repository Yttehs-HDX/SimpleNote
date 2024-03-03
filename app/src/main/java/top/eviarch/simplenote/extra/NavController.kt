package top.eviarch.simplenote.extra

import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateSingleTopTo(
    route: String
) {
    val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .build()
    navigate(route, navOptions)
}

fun NavController.navigateBack() {
    popBackStack()
}