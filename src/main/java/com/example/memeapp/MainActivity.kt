package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeapp.R.id.memeImageView
import com.example.memeapp.R.id.nextButton
import com.example.memeapp.R.id.progressBar
import com.example.memeapp.R.id.shareButton

class MainActivity : AppCompatActivity() {

     var currentMemeUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private fun loadMeme() { // Instantiate the RequestQueue.

        val nextButton: Button = findViewById(nextButton)
        nextButton.isEnabled = false
        val shareButton: Button = findViewById(shareButton)
        shareButton.isEnabled = false
        val progressBar: ProgressBar = findViewById(progressBar)
        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.com/gimme"
        val memeImageView: ImageView = findViewById(memeImageView)

// Request a string response from the provided URL.
//        val stringRequest = StringRequest(
//            Request.Method.GET, url,
//            Response.Listener<String> {
//                response -> Log.d("Success Request",response.substring(0,500))
//            },
//        Response.ErrorListener {
//                response -> Log.d(" error ",it.localizedMessage)
//        })


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            {
                               response ->  currentMemeUrl = response.getString("url")
                Glide.with(this).load(currentMemeUrl).listener(object : RequestListener<Drawable> {

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        nextButton.isEnabled = true
                        shareButton.isEnabled = true
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(memeImageView)
             },
            {
                Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()
            })

        /* Add the request to the RequestQueue. */
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        // because of this singleton feature we can create many requests but all can be acessed by
        // single volley instance
    }

    fun shareMeme(view: View) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(
            Intent.EXTRA_TEXT,
            "Hey , Checkout this meme $currentMemeUrl,  share it further if you laugh "
        )
//        val chooser = Intent.createChooser(share,"Share this using....")
        startActivity(Intent.createChooser(share,"Share this with ..."))

    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}