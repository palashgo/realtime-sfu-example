package com.cloudflare.anthropic_sample.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
  showSystemUi = true,
  showBackground = false,
  device = "id:pixel_8_pro",
  uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
annotation class DevicePreview
