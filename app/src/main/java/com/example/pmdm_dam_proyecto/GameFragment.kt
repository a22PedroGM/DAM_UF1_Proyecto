package com.example.pmdm_dam_proyecto

import android.annotation.SuppressLint
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.pmdm_dam_proyecto.databinding.FragmentGameBinding
import java.lang.Math.abs
import java.util.*

class GameFragment : Fragment() {
    //Variables del fragmento
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var binding: FragmentGameBinding
    //Posicion de la pelota
    private var dY = 0f
    //Velocidad de la pelota
    private var velocidadX = 0f
    private var velocidadY = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        arguments?.let {
            val imageResId = it.getInt("imageResId", R.drawable.ball_image)
            cambiarBola(imageResId)
        }
        binding.root.post {
           ballCollider()
        }
        binding.player1Paddle.setOnTouchListener { _, event -> handleTouch(event, binding.player1Paddle) }
        binding.player2Paddle.setOnTouchListener { _, event -> handleTouch(event, binding.player2Paddle) }

        binding.root.setOnTouchListener{ _, event ->
                if( velocidadY == 0f)
                if (event.action == MotionEvent.ACTION_DOWN) {
                    velocidadY = 15f
                    velocidadX = 15f
            }
            true
        }

        val handler = Handler()
        //Frecuencia de actualizacion de la pelota
        val delay: Long = 16
        //Gestion del delay
        handler.postDelayed(object : Runnable {
            override fun run() {
                moverPelota()
                ballCollider()
                handler.postDelayed(this, delay)
            }
        }, delay)

        return binding.root
    }

    private fun cambiarBola(resource: Int) {
        binding.ball.setImageResource(resource)
    }

    //Gestion de movimiento de palas
    private fun handleTouch(event: MotionEvent, paddle: ImageView): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dY = paddle.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val newY = event.rawY + dY
                if (newY >= 0 && newY + paddle.height <= binding.root.height) {
                    paddle.animate()
                        .y(newY)
                        .setDuration(0)
                        .start()
                }
            }
        }
        return true
    }
    //Deteccion de colision de la pelota con los bordes
    private fun ballCollider() {
        val rectBall = Rect()
        val rectPaddle1 = Rect()
        val rectPaddle2 = Rect()

        binding.ball.getGlobalVisibleRect(rectBall)
        binding.player1Paddle.getGlobalVisibleRect(rectPaddle1)
        binding.player2Paddle.getGlobalVisibleRect(rectPaddle2)

        if (rectBall.top <= 0 || rectBall.bottom >= binding.root.height || rectBall.left <= 0 || rectBall.right >= binding.root.width || rectBall.intersect(rectPaddle1) || rectBall.intersect(rectPaddle2)) {
            velocidadX = -velocidadX
            velocidadY = -velocidadY
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sonidonico2)
            mediaPlayer.start()
        }
    }

    private fun obtenerAleatoriedad(): Float {
        val aleatorio = Random()
        return aleatorio.nextFloat() * 1 - 6
    }
    private fun moverPelota() {
        binding.ball.x += velocidadX
        binding.ball.y += velocidadY
    }
}
