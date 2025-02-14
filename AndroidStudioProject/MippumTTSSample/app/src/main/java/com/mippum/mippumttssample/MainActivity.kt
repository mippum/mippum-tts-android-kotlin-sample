package com.mippum.mippumttssample

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mippum.mippumttssample.ui.theme.MippumTTSSampleTheme
import java.util.*

class MainActivity : ComponentActivity() {

    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speakButton: Button
    private lateinit var voiceSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        speakButton = findViewById(R.id.speak_button)
        voiceSpinner = findViewById(R.id.voice_spinner)

        textToSpeech = TextToSpeech(this, OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val langResult = textToSpeech.setLanguage(Locale.US)
                if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Language Missing", Toast.LENGTH_SHORT).show()
                }

                val voices = textToSpeech.voices
                val voiceNames = voices.map { it.name }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, voiceNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                voiceSpinner.adapter = adapter

                voiceSpinner.setSelection(0)

            } else {
                Toast.makeText(this, "TTS Init Fail", Toast.LENGTH_SHORT).show()
            }
        })

        speakButton.setOnClickListener {
            val textToRead = "Hello! This is TTS example."
            speakOut(textToRead)
        }

    }

    private fun speakOut(text: String) {
        val selectedVoiceName = voiceSpinner.selectedItem.toString()

        val voices = textToSpeech.voices
        val selectedVoice = voices.find { it.name == selectedVoiceName }

        if (selectedVoice != null) {
            textToSpeech.voice = selectedVoice
        }

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }

}
