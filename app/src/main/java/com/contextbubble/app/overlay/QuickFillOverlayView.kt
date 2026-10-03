package com.contextbubble.app.overlay

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.contextbubble.app.data.QuickFillItem

class QuickFillOverlayView(
    context: Context,
    items: List<QuickFillItem>,
    onSelected: (QuickFillItem) -> Unit,
) : ScrollView(context) {
    init {
        elevation = dp(18).toFloat()
        background = GradientDrawable().apply {
            cornerRadius = dp(20).toFloat()
            setColor(0xFF141E33.toInt())
            setStroke(dp(1), 0x1AFFFFFF)
        }
        addView(LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(10), dp(10), dp(10), dp(10))
            addView(header())
            if (items.isEmpty()) {
                addView(emptyRow())
            } else {
                items.forEach { item ->
                    addView(vaultRow(item).apply { setOnClickListener { onSelected(item) } })
                }
            }
        })
    }

    private fun header() = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(6), dp(2), dp(6), dp(10))
        addView(TextView(context).apply {
            text = "⬢ Vault — tap to fill"
            textSize = 11f
            setTextColor(0xFF7C8DB0.toInt())
            letterSpacing = 0.06f
            typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
        })
    }

    private fun emptyRow() = TextView(context).apply {
        text = "No vault items yet — add one in the app"
        textSize = 13f
        setTextColor(0xFFAEB5C3.toInt())
        setPadding(dp(12), dp(14), dp(12), dp(14))
        gravity = Gravity.CENTER
    }

    private fun vaultRow(item: QuickFillItem) = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val bg = GradientDrawable().apply {
            cornerRadius = dp(14).toFloat()
            setColor(0x1AFFFFFF)
            setStroke(dp(1), 0x0FFFFFFF)
        }
        background = bg
        setPadding(dp(2), dp(8), dp(12), dp(8))
        val accent = accentFor(item.kind)
        addView(View(context).apply {
            layoutParams = LinearLayout.LayoutParams(dp(3), dp(36)).apply { marginEnd = dp(10) }
            background = GradientDrawable().apply {
                cornerRadius = dp(3).toFloat()
                setColor(accent)
            }
        })
        addView(TextView(context).apply {
            text = iconFor(item.kind)
            textSize = 16f
            setPadding(0, 0, dp(10), 0)
        })
        addView(LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            addView(TextView(context).apply {
                text = item.label
                textSize = 14f
                setTextColor(Color.WHITE)
                maxLines = 1
                ellipsize = android.text.TextUtils.TruncateAt.END
                typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
            })
            addView(TextView(context).apply {
                val preview = item.value.take(32) + if (item.value.length > 32) "…" else ""
                text = "${item.kind.lowercase()} • $preview"
                textSize = 11f
                setTextColor(0xFFAEB5C3.toInt())
                maxLines = 1
                ellipsize = android.text.TextUtils.TruncateAt.END
            })
        })
        isClickable = true
        isFocusable = true
        setPadding(dp(8), dp(8), dp(12), dp(8))
    }

    private fun iconFor(kind: String) = when (kind.lowercase()) {
        "email" -> "✉"
        "phone" -> "◴"
        "address" -> "⌖"
        "name" -> "⬣"
        else -> "≡"
    }

    private fun accentFor(kind: String) = when (kind.lowercase()) {
        "email" -> 0xFF5C64F4.toInt()
        "phone" -> 0xFF1B8A67.toInt()
        "address" -> 0xFFE8A838.toInt()
        "name" -> 0xFF7C6CF0.toInt()
        else -> 0xFF5F6B7A.toInt()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
