package com.example.audio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class AudioSynth {
    var isMuted: Boolean = false
    private val scope = CoroutineScope(Dispatchers.Default)

    fun playClick() {
        if (isMuted) return
        scope.launch {
            playTone(freq = 800.0, durationMs = 30, volume = 0.4f)
        }
    }

    fun playCorrect() {
        if (isMuted) return
        scope.launch {
            playTone(freq = 1046.5, durationMs = 60, volume = 0.5f) // C6
            playTone(freq = 1318.5, durationMs = 60, volume = 0.5f) // E6
            playTone(freq = 1567.9, durationMs = 120, volume = 0.6f) // G6
        }
    }

    fun playWrong() {
        if (isMuted) return
        scope.launch {
            playTone(freq = 180.0, durationMs = 120, volume = 0.6f)
            playTone(freq = 120.0, durationMs = 150, volume = 0.6f)
        }
    }

    fun playCreeperHiss() {
        if (isMuted) return
        scope.launch {
            playNoise(durationMs = 400, volume = 0.5f)
        }
    }

    fun playVictory() {
        if (isMuted) return
        scope.launch {
            val notes = doubleArrayOf(523.25, 659.25, 783.99, 1046.50) // C5, E5, G5, C6
            for (freq in notes) {
                playTone(freq = freq, durationMs = 90, volume = 0.6f)
            }
        }
    }

    private fun playTone(freq: Double, durationMs: Int, volume: Float) {
        try {
            val sampleRate = 22050
            val numSamples = (sampleRate * durationMs / 1000.0).toInt()
            val sample = DoubleArray(numSamples)
            val generatedSnd = ByteArray(2 * numSamples)

            for (i in 0 until numSamples) {
                // Fade out envelope to avoid popping
                val env = 1.0 - (i.toDouble() / numSamples)
                sample[i] = sin(2.0 * Math.PI * i.toDouble() / (sampleRate / freq)) * env
            }

            var idx = 0
            for (dVal in sample) {
                val valInt = (dVal * 32767 * volume).toInt().coerceIn(-32768, 32767)
                val valShort = valInt.toShort()
                generatedSnd[idx++] = (valShort.toInt() and 0x00ff).toByte()
                generatedSnd[idx++] = ((valShort.toInt() and 0xff00) ushr 8).toByte()
            }

            val audioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                generatedSnd.size,
                AudioTrack.MODE_STATIC
            )
            audioTrack.write(generatedSnd, 0, generatedSnd.size)
            audioTrack.play()
            Thread.sleep(durationMs.toLong() + 20)
            audioTrack.release()
        } catch (_: Exception) {
            // Audio error safety fallback
        }
    }

    private fun playNoise(durationMs: Int, volume: Float) {
        try {
            val sampleRate = 22050
            val numSamples = (sampleRate * durationMs / 1000.0).toInt()
            val generatedSnd = ByteArray(2 * numSamples)
            val random = java.util.Random()

            var idx = 0
            for (i in 0 until numSamples) {
                val env = 1.0 - (i.toDouble() / numSamples)
                val noise = (random.nextDouble() * 2.0 - 1.0) * env
                val valInt = (noise * 32767 * volume).toInt().coerceIn(-32768, 32767)
                val valShort = valInt.toShort()
                generatedSnd[idx++] = (valShort.toInt() and 0x00ff).toByte()
                generatedSnd[idx++] = ((valShort.toInt() and 0xff00) ushr 8).toByte()
            }

            val audioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                generatedSnd.size,
                AudioTrack.MODE_STATIC
            )
            audioTrack.write(generatedSnd, 0, generatedSnd.size)
            audioTrack.play()
            Thread.sleep(durationMs.toLong() + 20)
            audioTrack.release()
        } catch (_: Exception) {
            // Audio fallback
        }
    }
}
