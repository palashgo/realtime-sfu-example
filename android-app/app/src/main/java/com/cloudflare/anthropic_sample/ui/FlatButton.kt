package com.cloudflare.anthropic_sample.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun FlatButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource? = null,
  elevation: ButtonElevation? = null,
  shape: Shape? = null,
  border: BorderStroke? = null,
  colors: ButtonColors? = null,
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  isLoading: Boolean = false,
  content: @Composable RowScope.() -> Unit,
) {
  val buttonElevation =
    elevation
      ?: ButtonDefaults.elevatedButtonElevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        focusedElevation = 0.dp,
        hoveredElevation = 0.dp,
      )

  val buttonShape = shape ?: RoundedCornerShape(12.dp)

  val colorScheme =
    colors
      ?: ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
      )

  Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled && !isLoading,
    interactionSource = interactionSource,
    elevation = buttonElevation,
    shape = buttonShape,
    border = border,
    colors = colorScheme,
    contentPadding = contentPadding,
  ) {
    AnimatedVisibility(visible = isLoading) {
      CircularProgressIndicator(
        modifier = Modifier.size(20.dp),
        color = MaterialTheme.colorScheme.onSecondary,
        strokeWidth = 2.dp,
      )
    }
    AnimatedVisibility(visible = !isLoading) { content() }
  }
}
