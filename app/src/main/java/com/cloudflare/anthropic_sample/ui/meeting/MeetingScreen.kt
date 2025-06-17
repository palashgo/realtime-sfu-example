package com.cloudflare.anthropic_sample.ui.meeting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cloudflare.anthropic_sample.R
import com.cloudflare.anthropic_sample.ui.theme.AnthropicSampleTheme
import com.cloudflare.anthropic_sample.utils.DevicePreview

@Composable
fun MeetingScreen(
  modifier: Modifier = Modifier,
  viewModel: MeetingViewModel,
  onLeaveMeeting: () -> Unit = {},
) {
  val state = viewModel.state.collectAsStateWithLifecycle()

  MeetingScreenUi(
    modifier = modifier.fillMaxSize(),
    state = state.value,
    onMicToggle = { viewModel.toggleMic() },
    onLeaveMeeting,
  )
}

@Composable
fun MeetingScreenUi(
  modifier: Modifier = Modifier,
  state: MeetingState,
  onMicToggle: () -> Unit = {},
  onLeaveMeeting: () -> Unit = {},
) {
  val context = LocalContext.current
  LaunchedEffect(state) { Toast.makeText(context, state.toString(), Toast.LENGTH_SHORT).show() }

  Scaffold { innerPadding ->
    Column(
      modifier = modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Spacer(modifier = Modifier.weight(1f))

      val brush = Brush.horizontalGradient(listOf(Color.Red, Color.Cyan))
      Box(modifier = Modifier.clip(CircleShape).size(200.dp).background(brush)) {
        Text(
          "A\\",
          modifier = Modifier.align(Alignment.Center),
          fontSize = 70.sp,
          fontWeight = FontWeight.Black,
          color = Color.White,
        )
      }

      Spacer(modifier = Modifier.weight(1f))

      val buttonColors =
        IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF1F1F1F))
      Row {
        FilledIconButton(
          modifier = Modifier.size(80.dp),
          colors = buttonColors,
          onClick = onMicToggle,
        ) {
          val icon = if (state.micEnabled) Icons.Filled.Mic else Icons.Filled.MicOff
          Icon(
            modifier = Modifier.size(40.dp),
            imageVector = icon,
            tint = Color.White,
            contentDescription =
              if (state.micEnabled) stringResource(R.string.mic_on)
              else stringResource(R.string.mic_off),
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        FilledIconButton(
          modifier = Modifier.size(80.dp),
          colors = buttonColors,
          onClick = onLeaveMeeting,
        ) {
          Icon(
            modifier = Modifier.size(40.dp),
            imageVector = Icons.Filled.Close,
            tint = Color.White,
            contentDescription =
              if (state.micEnabled) stringResource(R.string.mic_on)
              else stringResource(R.string.mic_off),
          )
        }
      }
    }
  }
}

@DevicePreview
@Composable
fun MeetingScreenPreview() {
  AnthropicSampleTheme { MeetingScreenUi(state = MeetingState()) }
}
