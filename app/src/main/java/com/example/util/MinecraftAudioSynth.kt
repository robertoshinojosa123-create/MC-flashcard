package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

class MinecraftAudioSynth {

    private val sampleRate = 44100
    private val scope = CoroutineScope(Dispatchers.Default)

    var isMuted: Boolean = false

    private fun playTone(frequencies: List<Pair<Double, Int>>, waveType: WaveType = WaveType.SQUARE) {
        if (isMuted) return

        scope.launch {
            try {
                var totalSamples = 0
                for ((_, durationMs) in frequencies) {
                    totalSamples += (sampleRate * durationMs / 1000)
                }

                val buffer = ShortArray(totalSamples)
                var offset = 0

                for ((freq, durationMs) in frequencies) {
                    val count = (sampleRate * durationMs / 1000)
                    for (i in 0 until count) {
                        val sampleIndex = offset + i
                        val t = i.toDouble() / sampleRate
                        val amplitude = 18000.0 * (1.0 - (i.toDouble() / count.toDouble())) // Decaying envelope

                        val value = when (waveType) {
                            WaveType.SQUARE -> {
                                if (sin(2.0 * Math.PI * freq * t) >= 0) amplitude else -amplitude
                            }
                            WaveType.SINE -> {
                                sin(2.0 * Math.PI * freq * t) * amplitude
                            }
                            WaveType.NOISE -> {
                                (Random.nextDouble(-1.0, 1.0) * amplitude)
                            }
                        }
                        buffer[sampleIndex] = value.toInt().coerceIn(-32767, 32767).toShort()
                    }
                    offset += count
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()

                // Release after playing
                scope.launch {
                    val totalDuration = frequencies.sumOf { it.second }
                    kotlinx.coroutines.delay(totalDuration.toLong() + 100)
                    try {
                        audioTrack.stop()
                        audioTrack.release()
                    } catch (_: Exception) {}
                }
            } catch (_: Exception) {
                // Ignore audio playback exceptions on unsupported devices
            }
        }
    }

    enum class WaveType {
        SQUARE, SINE, NOISE
    }

    fun playClick() {
        playTone(listOf(Pair(600.0, 30), Pair(800.0, 30)), WaveType.SQUARE)
    }

    fun playCorrect() {
        playTone(
            listOf(
                Pair(523.25, 60), // C5
                Pair(659.25, 60), // E5
                Pair(783.99, 100) // G5
            ),
            WaveType.SQUARE
        )
    }

    fun playWrong() {
        playTone(
            listOf(
                Pair(220.0, 100),
                Pair(180.0, 140)
            ),
            WaveType.SQUARE
        )
    }

    fun playCreeperHiss() {
        playTone(
            listOf(
                Pair(100.0, 250)
            ),
            WaveType.NOISE
        )
    }

    fun playVictory() {
        playTone(
            listOf(
                Pair(523.25, 80),
                Pair(659.25, 80),
                Pair(783.99, 80),
                Pair(1046.50, 200)
            ),
            WaveType.SQUARE
        )
    }

    fun playLevelUp() {
        playTone(
            listOf(
                Pair(440.0, 70),
                Pair(554.37, 70),
                Pair(659.25, 70),
                Pair(880.0, 180)
            ),
            WaveType.SINE
        )
    }
}
