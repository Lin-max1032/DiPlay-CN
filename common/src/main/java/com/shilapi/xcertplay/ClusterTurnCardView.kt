package com.shilapi.xcertplay

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import com.shilapi.xcertplay.airplay.CarPlayClusterDisplay
import com.shilapi.xcertplay.airplay.ClusterTurnCardOverlay
import com.shilapi.xcertplay.hud.ClusterTurnGuidance

/** Instruction card drawn by DiPlay on top of the dashboard map stream. */
internal class ClusterTurnCardView(context: Context) : View(context) {
    private var guidance: ClusterTurnGuidance? = null
    private var position = CarPlayClusterDisplay.OverlayPosition.LEFT
    private var size = CarPlayClusterDisplay.OverlaySize.MEDIUM
    private val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(230, 18, 22, 28) }
    private val accentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.rgb(90, 168, 255); style = Paint.Style.FILL }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
    }
    private val mutedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(210, 210, 220, 230)
        typeface = Typeface.create("sans-serif", Typeface.NORMAL)
    }
    private val arrowPath = Path()
    private val cardRect = RectF()

    fun setLayout(position: CarPlayClusterDisplay.OverlayPosition, size: CarPlayClusterDisplay.OverlaySize) {
        this.position = position
        this.size = size
        requestLayout()
        invalidate()
    }

    fun setGuidance(next: ClusterTurnGuidance?) {
        if (guidance == next) return
        guidance = next
        visibility = if (next == null) GONE else VISIBLE
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val panelWidth = MeasureSpec.getSize(widthMeasureSpec).coerceAtLeast(1)
        val panelHeight = MeasureSpec.getSize(heightMeasureSpec).coerceAtLeast(1)
        setMeasuredDimension(panelWidth, panelHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val next = guidance ?: return
        val card = ClusterTurnCardOverlay.card(width, height, position, size)
        val radius = dp(18f)
        cardRect.set(card.left.toFloat(), card.top.toFloat(), (card.left + card.width).toFloat(), (card.top + card.height).toFloat())
        canvas.drawRoundRect(cardRect, radius, radius, cardPaint)

        val padding = card.width * 0.08f
        val arrowBox = card.height * 0.62f
        val arrowLeft = card.left + padding
        val arrowTop = card.top + padding
        drawArrow(canvas, next, arrowLeft, arrowTop, arrowBox)

        val textLeft = arrowLeft + arrowBox + padding
        val textWidth = card.left + card.width - padding - textLeft
        if (textWidth <= 0f) return
        textPaint.textSize = card.height * 0.28f
        mutedPaint.textSize = card.height * 0.16f
        val distanceY = arrowTop + textPaint.textSize
        canvas.drawText(distanceLabel(next.distanceMeters), textLeft, distanceY, textPaint)
        val road = roadLabel(next)
        if (road.isNotEmpty()) {
            val roadY = (card.top + card.height - padding).coerceAtLeast(distanceY + mutedPaint.textSize)
            canvas.drawText(ellipsize(road, textWidth, mutedPaint), textLeft, roadY, mutedPaint)
        }
    }

    private fun drawArrow(canvas: Canvas, next: ClusterTurnGuidance, left: Float, top: Float, box: Float) {
        val cx = left + box / 2f
        val cy = top + box / 2f
        val stem = box * 0.12f
        arrowPath.reset()
        when (next.icon) {
            3, 5, 7 -> { // right family
                arrowPath.moveTo(cx + box * 0.32f, cy)
                arrowPath.lineTo(cx - box * 0.08f, cy - box * 0.32f)
                arrowPath.lineTo(cx - box * 0.08f, cy - stem)
                arrowPath.lineTo(cx - box * 0.32f, cy - stem)
                arrowPath.lineTo(cx - box * 0.32f, cy + stem)
                arrowPath.lineTo(cx - box * 0.08f, cy + stem)
                arrowPath.lineTo(cx - box * 0.08f, cy + box * 0.32f)
                arrowPath.close()
            }
            2, 4, 6, 8 -> { // left family / left u-turn drawn as left
                arrowPath.moveTo(cx - box * 0.32f, cy)
                arrowPath.lineTo(cx + box * 0.08f, cy - box * 0.32f)
                arrowPath.lineTo(cx + box * 0.08f, cy - stem)
                arrowPath.lineTo(cx + box * 0.32f, cy - stem)
                arrowPath.lineTo(cx + box * 0.32f, cy + stem)
                arrowPath.lineTo(cx + box * 0.08f, cy + stem)
                arrowPath.lineTo(cx + box * 0.08f, cy + box * 0.32f)
                arrowPath.close()
            }
            19 -> { // right u-turn as right
                arrowPath.moveTo(cx + box * 0.32f, cy)
                arrowPath.lineTo(cx - box * 0.08f, cy - box * 0.32f)
                arrowPath.lineTo(cx - box * 0.08f, cy - stem)
                arrowPath.lineTo(cx - box * 0.32f, cy - stem)
                arrowPath.lineTo(cx - box * 0.32f, cy + stem)
                arrowPath.lineTo(cx - box * 0.08f, cy + stem)
                arrowPath.lineTo(cx - box * 0.08f, cy + box * 0.32f)
                arrowPath.close()
            }
            15 -> { // destination flag
                arrowPath.moveTo(cx - box * 0.08f, cy + box * 0.32f)
                arrowPath.lineTo(cx - box * 0.08f, cy - box * 0.30f)
                arrowPath.lineTo(cx + box * 0.28f, cy - box * 0.16f)
                arrowPath.lineTo(cx - box * 0.08f, cy - box * 0.02f)
                arrowPath.close()
            }
            else -> { // straight / roundabout enter-exit
                arrowPath.moveTo(cx, cy - box * 0.34f)
                arrowPath.lineTo(cx + box * 0.28f, cy - box * 0.02f)
                arrowPath.lineTo(cx + stem, cy - box * 0.02f)
                arrowPath.lineTo(cx + stem, cy + box * 0.32f)
                arrowPath.lineTo(cx - stem, cy + box * 0.32f)
                arrowPath.lineTo(cx - stem, cy - box * 0.02f)
                arrowPath.lineTo(cx - box * 0.28f, cy - box * 0.02f)
                arrowPath.close()
            }
        }
        canvas.drawPath(arrowPath, accentPaint)
        if (next.roundaboutExit in 1..9) {
            textPaint.textSize = box * 0.28f
            val label = next.roundaboutExit.toString()
            val textWidth = textPaint.measureText(label)
            canvas.drawText(label, cx - textWidth / 2f, cy + textPaint.textSize * 0.35f, textPaint)
        }
    }

    private fun distanceLabel(meters: Int): String = when {
        meters <= 20 -> context.getString(com.shilapi.xcertplay.host.R.string.turn_card_now)
        meters < 1000 -> context.getString(com.shilapi.xcertplay.host.R.string.turn_card_distance_m, meters)
        else -> {
            val km = meters / 100 / 10f
            context.getString(com.shilapi.xcertplay.host.R.string.turn_card_distance_km, km)
        }
    }

    private fun roadLabel(next: ClusterTurnGuidance): String =
        if (next.roundaboutExit in 1..9) {
            context.getString(com.shilapi.xcertplay.host.R.string.turn_card_exit, next.roundaboutExit)
        } else {
            next.road
        }

    private fun ellipsize(text: String, maxWidth: Float, paint: Paint): String {
        if (paint.measureText(text) <= maxWidth) return text
        val ellipsis = "…"
        var end = text.length
        while (end > 0 && paint.measureText(text.take(end) + ellipsis) > maxWidth) end--
        return if (end == 0) ellipsis else text.take(end) + ellipsis
    }

    private fun dp(value: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
}
