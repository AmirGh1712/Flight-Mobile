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

class VerticalSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
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
        sldWidth = (width * 0.8).toFloat()
        sldHeight = height.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.GRAY
        canvas.drawRect((width * 0.1).toFloat(), 0f, (width * 0.9).toFloat(),
            height.toFloat(), paint)
        val sliderH = height * 0.07
        val sliderY = height / 2 * (1 + value) - sliderH / 2
        paint.color = Color.DKGRAY
        canvas.drawRect(0f, sliderY.toFloat(), width.toFloat() ,
           (sliderY + sliderH).toFloat(), paint)

    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        performClick()
        if (event != null) {
            value =  min(max((event.y - height / 2) / (height / 2), -1.0f), 1.0f)
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