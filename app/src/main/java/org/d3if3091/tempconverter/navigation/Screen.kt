package org.d3if3091.tempconverter.navigation

sealed class Screen(val route:String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
}