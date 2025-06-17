package com.cloudflare.anthropic_sample.ui.meeting

import androidx.lifecycle.ViewModel
import com.cloudflare.realtimekit.RealtimeKitClient
import com.cloudflare.realtimekit.errors.RtkError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MeetingState(val micEnabled: Boolean = true, val error: RtkError? = null)

class MeetingViewModel(private val rtkClient: RealtimeKitClient) : ViewModel() {
  private val _state = MutableStateFlow<MeetingState>(MeetingState())
  val state = _state.asStateFlow()

  fun toggleMic() {
    val localUser = rtkClient.localUser
    if (state.value.micEnabled) {
      localUser.disableAudio { e -> _state.update { it.copy(micEnabled = false, error = e) } }
    } else {
      localUser.enableAudio { e -> _state.update { it.copy(micEnabled = true, error = e) } }
    }
  }
}
