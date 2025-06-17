package com.cloudflare.anthropic_sample.ui.setup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cloudflare.realtimekit.RealtimeKitClient
import com.cloudflare.realtimekit.errors.MeetingError
import com.cloudflare.realtimekit.models.RtkMeetingInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class SetupState {
  data object Idle : SetupState()

  data object Loading : SetupState()

  data object Success : SetupState()

  data class Error(val error: MeetingError? = null) : SetupState()
}

class SetupViewModel(private val rtkClient: RealtimeKitClient) : ViewModel() {
  private val _state = MutableStateFlow<SetupState>(SetupState.Idle)
  val state = _state.asStateFlow()

  fun init(token: String) {
    val meetingInfo = RtkMeetingInfo(authToken = token, enableVideo = false, enableAudio = true)

    _state.value = SetupState.Loading

    rtkClient.init(
      meetingInfo = meetingInfo,
      onSuccess = {
        rtkClient.joinRoom(
          onSuccess = {
            Log.d("RTK", "Meeting joined successfully")
            _state.value = SetupState.Success
          },
          onFailure = { error ->
            Log.e("RTK", "Failed to join meeting: $error")
            _state.value = SetupState.Error(error)
          },
        )
      },
      onFailure = { error ->
        Log.e("RTK", "Failed to initialize meeting: $error")
        _state.value = SetupState.Error(error)
      },
    )
  }
}
