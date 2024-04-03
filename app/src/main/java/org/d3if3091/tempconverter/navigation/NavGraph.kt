package org.d3if3091.tempconverter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if3091.tempconverter.ui.screen.AboutScreen
import org.d3if3091.tempconverter.ui.screen.MainScreen

@Composable
fun SetupNavGraph(navHostController:NavHostController = rememberNavController()){
    NavHost(navController = navHostController, startDestination = Screen.Home.route){
        composable(route = Screen.Home.route){
            MainScreen(navHostController)
        }
        composable(route = Screen.About.route){
            AboutScreen(navHostController)
        }
    }
}