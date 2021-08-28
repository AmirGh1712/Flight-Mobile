package com.example.flightmobileapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

class HorizontalSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var sldWidth = 0.0f
    private var sldHeight = 0.0f
    private var value = 0.0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        sldWidth = width.toFloat()
        sldHeight = (height * 0.8).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.GRAY
        canvas.drawRect(0.0f, (height * 0.1).toFloat(), width.toFloat(),
            (height * 0.9).toFloat(), paint)
        val sliderW = width * 0.07
        val sliderX = width / 2 * (1 + value) - sliderW / 2
        paint.color = Color.DKGRAY
        canvas.drawRect(sliderX.toFloat(), 0.0f, (sliderX + sliderW).toFloat(),
            height.toFloat(), paint)

    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        performClick()
        if (event != null) {
            value =  min(max((event.x - width / 2) / (width / 2), -1.0f), 1.0f)
        }
        invalidate()
        return true;
    }

    public fun getValue() : Float {
        return value
    }
    public fun setValue(v : Float) {
        value = v
        invalidate()
    }
}