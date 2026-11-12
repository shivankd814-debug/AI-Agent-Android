package com.techunseen.agentautomation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)
        setContent { AgentHome(onSpeak = { speak(it) }) }
    }
    override fun onInit(status: Int) { tts?.language = Locale("hi", "IN") }
    private fun speak(text: String) { tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "agent_reply") }
    override fun onDestroy() { tts?.shutdown(); super.onDestroy() }
}

@Composable
fun AgentHome(onSpeak: (String) -> Unit) {
    var command by remember { mutableStateOf("") }
    var backend by remember { mutableStateOf("http://10.0.2.2:8000") }
    var reply by remember { mutableStateOf("Namaste! Main tumhara Android Operator hoon. Command type ya voice se do.") }
    var pending by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
        if (!spoken.isNullOrBlank()) command = spoken
    }
    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("AI AGENT AUTOMATION", style = MaterialTheme.typography.headlineSmall)
                Text("ANDROID OPERATOR", style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(backend, { backend = it }, label = { Text("Backend URL") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(command, { command = it }, label = { Text("Tumhara command") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        } else {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
                                putExtra(RecognizerIntent.EXTRA_PROMPT, "Apna command boliye")
                            }
                            speechLauncher.launch(intent)
                        }
                    }) { Text("🎙 Voice") }
                    OutlinedButton(onClick = { reply = "Command queued locally: $command"; pending = true }) { Text("Prepare task") }
                }
                if (pending) {
                    Card {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Approval required")
                            Text(command)
                            Text("External action execute karne se pehle backend integration aur permission zaroori hai.")
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { reply = "Approved request prepared. Connect backend to execute."; pending = false }) { Text("Approve") }
                                TextButton(onClick = { pending = false; reply = "Cancelled." }) { Text("Cancel") }
                            }
                        }
                    }
                }
                Card(Modifier.fillMaxWidth()) { Text(reply, Modifier.padding(16.dp)) }
                Text("Device features", style = MaterialTheme.typography.titleMedium)
                Text("• Voice input / spoken responses\n• Notification access (optional)\n• Accessibility actions (optional)\n• Calls, SMS and social apps: only supported, permissioned integrations")
                Text("Backend: $backend", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
