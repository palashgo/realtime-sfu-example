package com.cloudflare.anthropic_sample.ui.meeting

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.CircularProgressIndicator
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
    onLeaveMeeting = {
      viewModel.leaveRoom()
      onLeaveMeeting()
    },
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

  LaunchedEffect(state) {
    if (state.error != null) {
      Toast.makeText(context, state.error.message, Toast.LENGTH_SHORT).show()
      return@LaunchedEffect
    }
  }

  Scaffold { innerPadding ->
    Column(
      modifier = modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
      ) {
        val color = if (state.activeSpeaker != null) Color.Green else Color.Gray
        val text =
          if (state.activeSpeaker != null) stringResource(R.string.connected)
          else stringResource(R.string.connecting)

        Box(modifier = Modifier.clip(CircleShape).size(12.dp).background(color))
        Text(text, modifier = Modifier.padding(start = 6.dp))
      }

      Spacer(modifier = Modifier.weight(1f))

      AnimatedVisibility(visible = state.activeSpeaker != null) {
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
      }

      AnimatedVisibility(visible = state.activeSpeaker == null) { CircularProgressIndicator() }

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
          onClick = { onLeaveMeeting() },
        ) {
          Icon(
            modifier = Modifier.size(40.dp),
            imageVector = Icons.Filled.Close,
            tint = Color.White,
            contentDescription = stringResource(R.string.leave_room),
          )
        }
      }
    }
  }
}

@DevicePreview
@Composable
fun MeetingScreenPreview() {
  AnthropicSampleTheme {
    MeetingScreenUi(state = MeetingState(micEnabled = true, activeSpeaker = "A"))
  }
}
