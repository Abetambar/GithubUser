package com.example.githubuser.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var splashActivityBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashActivityBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashActivityBinding.root)
        loadAnimation()
        startIntent()
    }

    private fun loadAnimation() {
        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_animation)

        splashActivityBinding.apply {
            imageSplashScreen.startAnimation(logoAnimation)
            textSplashScreen.startAnimation(textAnimation)
        }
    }

    private fun startIntent() {
        Handler(mainLooper).postDelayed({
            startActivity(Intent(this, MainActivity::class.java)).also { finish() }
        },3000)
    }
}