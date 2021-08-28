package com.example.flightmobileapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_control.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.absoluteValue

class ControlFragment : Fragment() {
    private val args: ControlFragmentArgs by navArgs()
    private var aileron: Double = 0.0
    private var rudder: Double = 0.0
    private var elevator: Double = 0.0
    private var throttle: Double = 0.0
    private lateinit var horizontalSld : HorizontalSlider
    private lateinit var verticalSld : VerticalSlider
    private lateinit var joystick: Joystick
    private lateinit var img: ImageView
    private var flag : Boolean = false

    override fun onDestroyView() {
        super.onDestroyView()
        flag = false
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_control, container, false)
        img = view.imageView
        horizontalSld = view.horizontal_slider
        horizontalSld.setOnTouchListener { v, motionEvent ->
            view.performClick()
            listener(v, motionEvent)
        }
        verticalSld = view.vertical_slider
        verticalSld.setOnTouchListener { v, motionEvent ->
            v.performClick()
            listener(v, motionEvent)
        }
        joystick = view.joystick
        joystick.setOnTouchListener { v, motionEvent ->
            v.performClick()
            listener(v, motionEvent)
        }
        if (savedInstanceState != null) {
            horizontalSld.setValue(savedInstanceState.getFloat("rudder"))
            verticalSld.setValue(savedInstanceState.getFloat("throttle"))
        }
        flag = true
        CoroutineScope(IO).launch {
            getImageFromApi()
        }
        return view
    }
    private fun listener (view : View, motionEvent: MotionEvent) : Boolean {
        view.onTouchEvent(motionEvent)
        when (view) {
            is HorizontalSlider -> {
                if ((rudder - view.getValue()).absoluteValue > 0.01)
                    postToServer()
            }
            is VerticalSlider -> {
                if ((throttle - view.getValue()).absoluteValue > 0.01)
                    postToServer()
            }
            is Joystick -> {
                if ((aileron - view.getJoyX()).absoluteValue > 0.01 ||
                    (elevator - view.getJoyY()).absoluteValue > 0.01)
                    postToServer()
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat("rudder", horizontalSld.getValue())
        outState.putFloat("throttle", verticalSld.getValue())
        super.onSaveInstanceState(outState)
    }

    private fun postToServer() {
        /*serverRequest.sendCommand(joystick.getJoyX().toDouble(),
            horizontalSld.getValue().toDouble(),
            joystick.getJoyY().toDouble(),
            verticalSld.getValue().toDouble(),
            args.url)*/
        //img.setImageBitmap(serverRequest.getPicture(args.url));
        val retrofit_p = Retrofit.Builder()
            .baseUrl("http://${args.url}/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api_p = retrofit_p.create(Api::class.java)
        val body_p = api_p.postCommand(
            Command(joystick.getJoyX().toDouble(),
                verticalSld.getValue().toDouble(),
                joystick.getJoyY().toDouble(),
                horizontalSld.getValue().toDouble()))
            .enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                println("SUCCESS")
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(activity, "Fail post " + t.message,
                    Toast.LENGTH_LONG).show()
            }
        })
    }
    private suspend fun getImageFromApi () {
        while (flag) {
            setImage()
            delay(2000)
        }
    }
    private suspend fun setImage() {
        withContext(Main) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://${args.url}/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val api = retrofit.create(Api::class.java)
            val body = api.getImg().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val I = response.body()?.byteStream()
                    val B = BitmapFactory.decodeStream(I)
                    activity?.runOnUiThread {
                        img.setImageBitmap(B)
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(activity, "Fail get " + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}