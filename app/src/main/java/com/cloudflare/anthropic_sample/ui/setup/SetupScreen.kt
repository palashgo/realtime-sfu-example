package com.cloudflare.anthropic_sample.ui.setup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cloudflare.anthropic_sample.R
import com.cloudflare.anthropic_sample.ui.FlatButton
import com.cloudflare.anthropic_sample.ui.theme.AnthropicSampleTheme
import com.cloudflare.anthropic_sample.utils.DevicePreview

@Composable
fun SetupScreen(
  modifier: Modifier = Modifier,
  viewModel: SetupViewModel,
  onNavigateToMeeting: () -> Unit,
) {
  val state = viewModel.state.collectAsStateWithLifecycle()
  SetupScreenUi(modifier = modifier, state.value, onNavigateToMeeting = onNavigateToMeeting) { token
    ->
    viewModel.init(token)
  }
}

@Composable
fun SetupScreenUi(
  modifier: Modifier = Modifier,
  state: SetupState,
  onNavigateToMeeting: () -> Unit = {},
  onJoinMeetingClick: (String) -> Unit = {},
) {
  val context = LocalContext.current
  val authToken = rememberSaveable { mutableStateOf("") }

  LaunchedEffect(state) {
    when (state) {
      is SetupState.Success -> onNavigateToMeeting()
      is SetupState.Error ->
        Toast.makeText(context, state.error?.message, Toast.LENGTH_SHORT).show()
      else -> Unit
    }
  }

  Scaffold { innerPadding ->
    Column(
      modifier =
        modifier
          .fillMaxSize()
          .padding(innerPadding)
          .imePadding()
          .padding(horizontal = 24.dp, vertical = 36.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Spacer(modifier = Modifier.weight(1f))

      Image(
        painterResource(R.drawable.logo_cloudflare),
        modifier = Modifier.size(120.dp),
        contentDescription = stringResource(R.string.cloudflare_logo),
      )

      Spacer(modifier = Modifier.weight(1f))

      OutlinedTextField(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        enabled = state !is SetupState.Loading,
        shape = RoundedCornerShape(12.dp),
        value = authToken.value.trim(),
        onValueChange = { authToken.value = it },
        label = { Text(stringResource(R.string.auth_token)) },
        singleLine = true,
      )

      FlatButton(
        onClick = {
          if (authToken.value.isNotBlank()) {
            onJoinMeetingClick(authToken.value)
          } else {
            Toast.makeText(
                context,
                context.getString(R.string.valid_auth_token),
                Toast.LENGTH_SHORT,
              )
              .show()
          }
        },
        isLoading = state is SetupState.Loading,
        modifier = Modifier.fillMaxWidth().height(50.dp),
      ) {
        Text(stringResource(R.string.join_meeting), fontSize = 16.sp)
      }
    }
  }
}

@DevicePreview
@Composable
fun SetupScreenPreview() {
  AnthropicSampleTheme { SetupScreenUi(state = SetupState.Success) }
}
