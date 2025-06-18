package com.cloudflare.anthropic_sample.ui.meeting

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cloudflare.realtimekit.RealtimeKitClient
import com.cloudflare.realtimekit.errors.RtkError
import com.cloudflare.realtimekit.participants.RtkParticipantsEventListener
import com.cloudflare.realtimekit.participants.RtkRemoteParticipant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MeetingState(
  val isConnecting: Boolean = true,
  val activeSpeaker: String? = null,
  val didLeaveRoom: Boolean = false,
  val micEnabled: Boolean = false,
  val error: RtkError? = null,
)

class MeetingViewModel(private val rtkClient: RealtimeKitClient) : ViewModel() {
  private val _state = MutableStateFlow<MeetingState>(MeetingState())
  val state = _state.asStateFlow()

  init {
    val active = rtkClient.participants.active
    if (active.isNotEmpty()) {
      val isLocalUser = active.first().id == rtkClient.localUser.id
      if (isLocalUser.not()) {
        _state.update { it.copy(isConnecting = false, activeSpeaker = active.first().id) }
      }
    }

    rtkClient.addParticipantsEventListener(
      object : RtkParticipantsEventListener {
        override fun onActiveParticipantsChanged(active: List<RtkRemoteParticipant>) {
          active.firstOrNull()?.let { speaker ->
            _state.update { it.copy(isConnecting = false, activeSpeaker = speaker.id) }
          }
        }
      }
    )
  }

  fun toggleMic() {
    val localUser = rtkClient.localUser
    if (state.value.micEnabled) {
      localUser.disableAudio { e -> _state.update { it.copy(micEnabled = false, error = e) } }
    } else {
      localUser.enableAudio { e -> _state.update { it.copy(micEnabled = true, error = e) } }
    }
  }

  fun leaveRoom() {
    rtkClient.leaveRoom(
      onSuccess = { _state.update { it.copy(didLeaveRoom = true) } },
      onFailure = { e ->
        Log.e("RTK", "Failed to leave room: $e")
        rtkClient.release(
          onSuccess = { _state.update { it.copy(didLeaveRoom = true) } },
          onFailure = {
            Log.e("RTK", "Failed to release: $e")
            _state.update { it.copy(didLeaveRoom = true, error = e) }
          },
        )
      },
    )
  }
}
