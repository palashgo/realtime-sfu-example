package com.cloudflare.anthropic_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.cloudflare.anthropic_sample.ui.home.HomeScreen
import com.cloudflare.anthropic_sample.ui.home.HomeViewModel
import com.cloudflare.anthropic_sample.ui.theme.AnthropicSampleTheme
import com.cloudflare.realtimekit.RealtimeKitMeetingBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val rtkClient = RealtimeKitMeetingBuilder.build(this)
            val viewModel = HomeViewModel(rtkClient)

            AnthropicSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel,
                    )
                }
            }
        }
    }
}
