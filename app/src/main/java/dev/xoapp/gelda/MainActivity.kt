package dev.xoapp.gelda

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var rotateAnimation: ObjectAnimator? = null
    private var geldaSound: MediaPlayer? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadGeldaSound()

        val submitButton: Button = findViewById(R.id.submit_button)
        val womenSwitch: Switch = findViewById(R.id.women_switch)

        val textView: TextView = findViewById(R.id.text_view)
        val weightLabel: EditText = findViewById(R.id.weight_label)

        val imageGelda: ImageView = findViewById(R.id.imageGelda)

        submitButton.setOnClickListener {

            if (imageGelda.visibility == View.VISIBLE) {
                imageGelda.visibility = View.GONE;
            }

            if (rotateAnimation != null) {
                rotateAnimation?.cancel()
            }

            if (geldaSound != null) {
                if (geldaSound!!.isPlaying) {
                    geldaSound?.stop()
                    loadGeldaSound()
                }
            }

            val weight: String = weightLabel.text.toString()

            if (weight.isEmpty()) {
                textView.text = "Please set a valid data"
                return@setOnClickListener
            }

            val realWeight: Int = weight.toInt()

            if (realWeight <= 0) {
                textView.text = "Please set a valid data"
                return@setOnClickListener
            }

            if (!womenSwitch.isChecked || realWeight <= 60) {
                textView.text = "You have a good weight"
                return@setOnClickListener
            }

            textView.text = "You are a fat bitch"

            imageGelda.setImageResource(R.drawable.logos)
            imageGelda.visibility = View.VISIBLE

            rotateImage(imageGelda)

            geldaSound?.start()
        }
    }

    private fun rotateImage(imageView: ImageView) {

        if (rotateAnimation == null) {
            rotateAnimation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f)
        }

        rotateAnimation?.duration = 1500

        rotateAnimation?.repeatCount = ObjectAnimator.INFINITE
        rotateAnimation?.interpolator = LinearInterpolator()

        rotateAnimation?.start()
    }

    private fun loadGeldaSound() {
        geldaSound = MediaPlayer.create(this, R.raw.song)
    }

    override fun onStop() {
        super.onStop()

        geldaSound?.stop()
        geldaSound?.release()
    }
}