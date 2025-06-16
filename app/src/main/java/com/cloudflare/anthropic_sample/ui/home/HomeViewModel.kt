package com.cloudflare.anthropic_sample.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cloudflare.anthropic_sample.Constants
import com.cloudflare.realtimekit.RealtimeKitClient
import com.cloudflare.realtimekit.errors.MeetingError
import com.cloudflare.realtimekit.models.RtkMeetingInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class HomeState {
    data object Loading : HomeState()
    data object Success : HomeState()
    data class Error(val error: MeetingError) : HomeState()
}

class HomeViewModel(private val rtkClient: RealtimeKitClient) : ViewModel() {
    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state = _state.asStateFlow()

    fun init() {
        val meetingInfo = RtkMeetingInfo(
            authToken = Constants.AUTH_TOKEN,
            enableVideo = false,
            enableAudio = true,
        )

        rtkClient.init(meetingInfo, onSuccess = {
            _state.value = HomeState.Success
            Log.d("RTK", "Meeting initialized successfully")
        }) { e ->
            _state.value = HomeState.Error(e)
            Log.e("RTK", "Failed to initialize meeting: $e")
        }
    }
}
