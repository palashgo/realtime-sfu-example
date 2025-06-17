package com.cloudflare.anthropic_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cloudflare.anthropic_sample.ui.meeting.MeetingScreen
import com.cloudflare.anthropic_sample.ui.meeting.MeetingViewModel
import com.cloudflare.anthropic_sample.ui.setup.SetupScreen
import com.cloudflare.anthropic_sample.ui.setup.SetupViewModel
import com.cloudflare.anthropic_sample.ui.theme.AnthropicSampleTheme
import com.cloudflare.realtimekit.RealtimeKitClient
import com.cloudflare.realtimekit.RealtimeKitMeetingBuilder

sealed class Screens(val route: String) {
  object Setup : Screens("setup")

  object Meeting : Screens("meeting")
}

private class MainViewModel(val client: RealtimeKitClient) : ViewModel()

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val navController = rememberNavController()
      val mainViewModel = viewModel {
        val rtkClient = RealtimeKitMeetingBuilder.build(this@MainActivity)
        MainViewModel(rtkClient)
      }

      AnthropicSampleTheme {
        NavHost(navController = navController, startDestination = Screens.Setup.route) {
          composable(Screens.Setup.route) {
            val viewModel = viewModel { SetupViewModel(mainViewModel.client) }
            SetupScreen(viewModel = viewModel) { navController.navigate(Screens.Meeting.route) }
          }

          composable(Screens.Meeting.route) {
            val viewModel = viewModel { MeetingViewModel(mainViewModel.client) }
            MeetingScreen(viewModel = viewModel) {
              navController.navigate(Screens.Setup.route) { popUpTo(0) }
            }
          }
        }
      }
    }
  }
}
