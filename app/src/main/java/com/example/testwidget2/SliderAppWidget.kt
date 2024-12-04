package com.example.testwidget2

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import java.util.prefs.Preferences

class SliderAppWidget : GlanceAppWidget() {
    override val stateDefinition = PreferencesGlanceStateDefinition
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        TODO("Not yet implemented")
    }

    val slider_value = intPreferencesKey(currentValue)

    @Composable
    fun Content() {
        val currentValue = currentState("slider_value", 50)

        Column(
            modifier = GlanceModifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Slider Value: $currentValue", style = TextStyle(fontSize = 18.sp))
            Row(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    text = "-",
                    onClick = actionRunCallback<AdjustSliderCallback>(
                        actionParametersOf(AdjustSliderCallback.DeltaKey to -1)
                    )
                )
                Text(text = "|", style = TextStyle(fontSize = 24.sp))
                Button(
                    text = "+",
                    onClick = actionRunCallback<AdjustSliderCallback>(
                        actionParametersOf(AdjustSliderCallback.DeltaKey to 1)
                    )
                )
            }
        }
    }

    @Composable
    private fun currentState(key: String, default: Int): Int {
        val preferences = glanceState<Preferences>()
        return preferences[key] ?: default
    }
}

class AdjustSliderCallback : ActionCallback {
    suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val delta = parameters[DeltaKey] ?: 0
        updateAppWidgetState(context, glanceId) { preferences ->
            val currentValue = preferences["slider_value"] ?: 50
            preferences["slider_value"] = (currentValue + delta).coerceIn(0, 100)
        }
        SliderAppWidget().update(context, glanceId)
    }

    companion object {
        val DeltaKey = ActionParameters.Key<Int>("delta")
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        TODO("Not yet implemented")
    }
}

class SliderAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SliderAppWidget()
}

