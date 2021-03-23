package com.akdogan.swcharacters.ui.detail

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.akdogan.swcharacters.R

class HorizontalDividerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {
    var dividerSize : Float = context.obtainStyledAttributes(attrs, R.styleable.HorizontalDividerView).run {
        this.getInt(R.styleable.HorizontalDividerView_dividerSize, 2).toFloat()
    }
        set(value){
            field = value
            paint.strokeWidth = value
            invalidate()
        }

    private var start = 0f
    private var end = 0f
    private var centerY = 0f

    @ColorInt
    var dividerColor = context.obtainStyledAttributes(attrs, R.styleable.HorizontalDividerView).run {
        this.getColor(R.styleable.HorizontalDividerView_dividerColor, Color.BLUE)
    }
        set(value){
            field = value
            paint.color = value
            invalidate()
        }

    var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = dividerColor
        style = Paint.Style.STROKE
        strokeWidth = dividerSize
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        start = 0 + paddingLeft.toFloat()
        end = w - paddingRight.toFloat()
        centerY = h / 2f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(
            start,
            centerY,
            end,
            centerY,
            paint
        )
    }

}