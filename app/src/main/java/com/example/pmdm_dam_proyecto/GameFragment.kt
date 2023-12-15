package com.example.pmdm_dam_proyecto

import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.pmdm_dam_proyecto.databinding.FragmentGameBinding
import com.example.pmdm_dam_proyecto.ScoresFragment
import java.util.Locale
import kotlin.random.Random
import kotlin.system.exitProcess

class GameFragment : Fragment() {
    //Variables de fragmento
    private lateinit var binding: FragmentGameBinding
    private var navController:NavController? = null
    //Variables de juego
    private var dY = 0f //Posicion de las palas
    private var speedX = 0f
    private var speedY = 0f
    private var changeDiff = 0
    var highScore: Int = 0
    private var maxSpeed = 25f
    //Variables de temporizador
    private var isTimerRunning = false
    private var timer: CountDownTimer? = null
    private var gameDuration: Long = 30000 // 2 minutos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as MainActivity).desactivarMenuLateral()
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
        val handler = Handler()
        val delay: Long = 12
        handler.postDelayed(object : Runnable {
            override fun run() {
                ballCollider()
                moverPelota()
                handler.postDelayed(this, delay)
            }
        }, delay)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        binding.root.setOnTouchListener { _, event ->
            if (speedY == 0f)
                if (event.action == MotionEvent.ACTION_DOWN) {
                    speedY = 8f
                    speedX = 8f
                }
                iniciarTimer()
            true
        }
    }
    private fun cambiarBola(resource: Int) {
        binding.ball.setImageResource(resource)
    }

    //Movimiento de las palas
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
    //Deteccion de colisiones
    private fun ballCollider() {
        if (!isAdded) return

        val rectBall = Rect()
        val rectPaddle1 = Rect()
        val rectPaddle2 = Rect()

        binding.ball.getGlobalVisibleRect(rectBall)
        binding.player1Paddle.getGlobalVisibleRect(rectPaddle1)
        binding.player2Paddle.getGlobalVisibleRect(rectPaddle2)

        // Borde superior y inferior
        if (rectBall.top <= 0 || rectBall.bottom >= binding.root.height) {
            speedY = -speedY
            introduceVariacion()
        }
        // Borde izquierdo y derecho
        if (rectBall.right >= binding.root.width) {
            val score = requireActivity().findViewById<TextView>(R.id.player1Score)
            speedX = -speedX
            score.text = (score.text.toString().toInt() + 1).toString()
            changeDiff++
            introduceVariacion()
        }
        if (rectBall.left <= 0) {
            val score = requireActivity().findViewById<TextView>(R.id.player2Score)
            speedX = -speedX
            score.text = (score.text.toString().toInt() + 1).toString()
            changeDiff++
            introduceVariacion()
        }
        // Palas
        if (rectBall.intersect(rectPaddle1) || rectBall.intersect(rectPaddle2)) {
            speedX = -speedX
            speedY = -speedY
            if (rectBall.intersect(rectPaddle1)) {
                binding.ball.x = rectPaddle1.right.toFloat() + 1
            } else {
                binding.ball.x = rectPaddle2.left.toFloat() - binding.ball.width - 1
            }
            introduceVariacion()
        }
        // Aumento de dificultad
        if (changeDiff == 3) {
            aumentarVelocidad()
        }
        binding.ball.x += speedX
        binding.ball.y += speedY
    }

    private fun introduceVariacion() {
        val random = Random
        val variation = random.nextInt(3) - 1
        speedX += variation
        speedY += variation
    }


    private fun moverPelota() {
        binding.ball.x += speedX
        binding.ball.y += speedY
    }

    private fun aumentarVelocidad() {
        speedX *= 1.20f
        speedY *= 1.20f
        speedY = speedY.coerceAtMost(maxSpeed)
        speedX = speedX.coerceAtMost(maxSpeed)
        changeDiff = 0
    }
    private fun iniciarTimer() {
        if (!isTimerRunning) {
            timer?.cancel()
            timer = object : CountDownTimer(gameDuration, 1000) {
                override fun onTick(timeUntilEnd: Long) {
                    gameDuration = timeUntilEnd
                    actualizarTimer()
                }
                override fun onFinish() {
                    val score1 = requireActivity().findViewById<TextView>(R.id.player1Score)
                    val score2 = requireActivity().findViewById<TextView>(R.id.player2Score)
                    if (score1.text.toString().toInt() > score2.text.toString().toInt()) {
                        highScore = score1.text.toString().toInt()
                    } else {
                        highScore = score2.text.toString().toInt()
                    }
                    navigateToScoresFragment()
                }
            }.start()
            isTimerRunning = true
        }
    }
    override fun onResume() {
        resetGame()
        super.onResume()
    }
    private fun actualizarTimer(){
        val minutos = (gameDuration / 1000) / 60
        val segundos = (gameDuration / 1000) % 60
        val tiempo = String.format(Locale.getDefault(),"%02d:%02d", minutos, segundos)
        binding.timer.text = tiempo
    }
    private fun resetGame() {
        val score1 = requireActivity().findViewById<TextView>(R.id.player1Score)
        val score2 = requireActivity().findViewById<TextView>(R.id.player2Score)
        score1.text = "0"
        score2.text = "0"
        speedX = 0f
        speedY = 0f
        changeDiff = 0
        binding.ball.x = binding.root.width / 2f
        binding.ball.y = binding.root.height / 2f
        isTimerRunning = false
        gameDuration = 30000
        actualizarTimer()
    }
    private fun navigateToScoresFragment() {
        val bundle = bundleOf("puntuacionMasAlta" to highScore)
        navController?.navigate(R.id.scoresFragment, bundle)
    }

}