package com.example.pmdm_dam_proyecto

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import java.util.*

class GameFragment : Fragment() {
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var ball: ImageView
    private var ballSpeedX = 8f // Velocidad inicial en el eje X
    private var ballSpeedY = 8f // Velocidad inicial en el eje Y
    private var screenWidth = 0
    private var screenHeight = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_game, container, false)
        ball = rootView.findViewById(R.id.ball)

        // Obtener dimensiones de la pantalla
        val displayMetrics = resources.displayMetrics
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
        mediaPlayer = MediaPlayer.create(requireContext(),R.raw.illojuaneditao)

        // Configuramos un OnClickListener para iniciar el movimiento de la pelota al tocar la pantalla
        rootView.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                startBallMovement()
            }
        }
        return rootView
    }

    private fun startBallMovement() {
        // Iniciar la animación de la pelota
        val animatorX = ObjectAnimator.ofFloat(ball, View.TRANSLATION_X, 0f, screenWidth.toFloat() - ball.width)
        val animatorY = ObjectAnimator.ofFloat(ball, View.TRANSLATION_Y, 0f, screenHeight.toFloat() - ball.height)

        // Configurar el listener para detectar el final de la animación y manejar el rebote
        animatorX.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                handleBallBounce()
            }
        })

        // Iniciar la animación
        animatorX.start()
        animatorY.start()
    }

    private fun handleBallBounce() {
        // Cambiar la dirección de manera aleatoria
        ballSpeedX *= -1
        ballSpeedY *= if (Random().nextBoolean()) 1 else -1

        // Actualizar la posición inicial para el próximo rebote
        ball.translationX = 0f
        ball.translationY = 0f

        // Iniciar la animación de rebote con la nueva dirección
        startBallMovement()
    }
}
