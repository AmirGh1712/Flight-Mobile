package com.example.flightmobileapp

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class Joystick @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var radius = 0.0f
    private var joyX = 0.0f
    private var joyY = 0.0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        radius = 0.7f * min(width, height).toFloat() / 2
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.DKGRAY
        canvas.drawCircle(width / 2.0f, height / 2.0f, radius, paint)
        paint.color = Color.GRAY
        canvas.drawCircle(width / 2.0f  + joyX * radius, height / 2.0f + joyY * radius ,
            radius * 0.3f, paint)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        performClick()
        if (event != null) {
            when(event.action)  {
                MotionEvent.ACTION_UP ->startAnimation()
                else -> setCoordinates(event.x, event.y)
            }
        }
        invalidate()
        return true
    }

    private fun setCoordinates(x: Float, y: Float) {
        joyX = -(width / 2.0f - x) / radius
        joyY = -(width / 2.0f - y) / radius
        if (distance(0f, 0f, joyX, joyY) > 1) {
            var angle = atan(joyY / joyX)
            if (joyX < 0) {
                angle -= PI.toFloat()
            }
            joyY = sin(angle)
            joyX = cos(angle)

        }
    }

    private fun startAnimation() {
        val x = joyX
        val y = joyY
        val animatorX = ObjectAnimator.ofFloat(this, "joyX", x, 0f)
        val animatorY = ObjectAnimator.ofFloat(this, "joyY", y, 0f)
        animatorX.duration = 300
        animatorY.duration = 300
        animatorX.start()
        animatorY.start()

        joyX = 0f
        joyY = 0f
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float) : Float {
        return sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
    }

    public fun getJoyX() : Float {
        return joyX
    }
    public fun getJoyY() : Float {
        return joyY
    }

    public fun setJoyX(x : Float) {
        joyX = x
        invalidate()
    }
    public fun setJoyY(y : Float) {
        joyY = y
        invalidate()
    }
}