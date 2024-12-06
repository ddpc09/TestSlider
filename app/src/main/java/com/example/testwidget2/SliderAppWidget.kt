package com.example.testwidget2

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import kotlinx.coroutines.flow.first



/*
class SliderAppWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {


       //val currentValue = currentState(cu, 50)

        provideContent {
            Column(
                modifier = GlanceModifier.fillMaxHeight().padding(16.dp),
                Alignment.Vertical.Bottom
            ) {
                Row(

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        text = "-",
                        onClick = actionRunCallback<AdjustSliderCallback>(
                            actionParametersOf(AdjustSliderCallback.DeltaKey to -1)
                        )
                    )

                    Text(text = "Brightness: 50", style = TextStyle(fontSize = 18.sp))

                    Button(
                        text = "+",

                        onClick = actionRunCallback<AdjustSliderCallback>(
                            actionParametersOf(AdjustSliderCallback.DeltaKey to 1)
                        )
                    )
                }
            }
            Column(
                modifier = GlanceModifier.fillMaxHeight().padding(16.dp),
                Alignment.Vertical.Top

            ) {
                Row(
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Button(
                        text = "-",
                        onClick = actionRunCallback<AdjustSliderCallback>(
                            actionParametersOf(AdjustSliderCallback.DeltaKey to -1)
                        )
                    )

                    Text(text = "Color Warmth: 50", style = TextStyle(fontSize = 18.sp))

                    Button(
                        text = "+",

                        onClick = actionRunCallback<AdjustSliderCallback>(
                            actionParametersOf(AdjustSliderCallback.DeltaKey to 1)
                        )
                    )
                }
            }
        }
    }

    private fun currentState(currentValue:AdjustSliderCallback, default: Int): AdjustSliderCallback {
        val preferences = currentValue
        return preferences
    }
}

class AdjustSliderCallback : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val delta = parameters[DeltaKey] ?: 0
        updateAppWidgetState(context, glanceId) { preferences ->
            var currentValue: Int = 10
            currentValue = (currentValue + delta).coerceIn(0, 100)
            return@updateAppWidgetState

        }
        SliderAppWidget().update(context, glanceId)
    }

    companion object {
        val DeltaKey = ActionParameters.Key<Int>("delta")
    }
}





*/
val sliderStepKey = ActionParameters.Key<Int>("slider_step")

class SliderAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            // create your AppWidget here
            SliderWidgetContent()

        }
    }
}



@Composable
fun SliderWidgetContent() {
    val context = LocalContext.current
    val sliderValueFlow = remember { getSliderValue(context) }
    val sliderValue by sliderValueFlow.collectAsState(initial = 50f)

    val modifier = GlanceModifier.fillMaxWidth().padding(16.dp)

    Column(modifier) {
        Text(text = "Adjust Brightness: ${sliderValue.toInt()}%")
        Row(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                text = "-",
                modifier = GlanceModifier.padding(8.dp),
                onClick = {
                    // Passing the step as parameter directly in ActionParameters
                    actionRunCallback<UpdateSliderAction>(
                        parameters = ActionParameters(sliderStepKey - 10)
                    )
                }
            )
            Spacer(modifier = GlanceModifier.width(16.dp))
            Button(
                text = "+",
                modifier = GlanceModifier.padding(8.dp),
                onClick = {
                    // Passing the step as parameter directly in ActionParameters
                    actionRunCallback<UpdateSliderAction>(
                        parameters = ActionParameters(sliderStepKey to 10)
                    )
                }
            )
        }
    }
}



// Action to update slider value
class UpdateSliderAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        // Accessing the slider step from parameters
        val step = parameters[sliderStepKey] ?: 0

        // Retrieve the current slider value from DataStore
        val currentValue = getSliderValue(context).first()
        val newValue = (currentValue + step).coerceIn(0f, 100f)

        // Save the new value in DataStore
        setSliderValue(context, newValue)

        // Update the widget
        SliderAppWidget().update(context, glanceId)
    }
}



