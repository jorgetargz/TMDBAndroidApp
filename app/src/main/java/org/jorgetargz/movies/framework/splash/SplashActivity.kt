package org.jorgetargz.movies.framework.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.jorgetargz.movies.databinding.ActivitySplashBinding
import org.jorgetargz.movies.framework.common.Constantes
import org.jorgetargz.movies.framework.main.MainActivity
import org.jorgetargz.movies.framework.utils.loadUrl

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        with(binding) {
            ivLogo.loadUrl(Constantes.SPLASH_IMAGE)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)

    }
}
