package com.example.testwidget2

import android.content.Context
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the key for the slider value
private val SLIDER_VALUE_KEY = floatPreferencesKey("slider_value")

// Function to read the slider value
fun getSliderValue(context: Context): Flow<Float> {
    return context.dataStore.data.map { preferences ->
        preferences[SLIDER_VALUE_KEY] ?: 50f // Default to 50% if no value is stored
    }
}

// Function to write the slider value
suspend fun setSliderValue(context: Context, value: Float) {
    context.dataStore.edit { preferences ->
        preferences[SLIDER_VALUE_KEY] = value
    }
}
