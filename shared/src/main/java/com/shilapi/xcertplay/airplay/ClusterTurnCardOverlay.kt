package com.shilapi.xcertplay.airplay

/**
 * Where DiPlay draws the instruction card on top of the dashboard map.
 *
 * Apple Maps paints the turn card inside the same safe area as the car marker, so the iPhone
 * cannot place the two independently. The overlay is DiPlay's own layer.
 *
 * BYD crops the same 1920×720 stream in both Full and Small screen navi. The measured centre
 * window that stays visible is about x 31–68 %, y 16–89 % of the panel (see
 * [CarPlayClusterDisplay.SAFE_AREA_PERCENT]). Placement and size are relative to that window,
 * not the full panel; otherwise Right/Small sits under the speed/power readouts and Large
 * covers half the cluster.
 */
object ClusterTurnCardOverlay {
    data class CardRect(val left: Int, val top: Int, val width: Int, val height: Int)

    fun card(
        panelWidth: Int,
        panelHeight: Int,
        position: CarPlayClusterDisplay.OverlayPosition,
        size: CarPlayClusterDisplay.OverlaySize,
    ): CardRect {
        require(panelWidth > 0 && panelHeight > 0)
        val window = visibleWindow(panelWidth, panelHeight)
        val widthFraction = when (size) {
            CarPlayClusterDisplay.OverlaySize.SMALL -> 0.42f
            CarPlayClusterDisplay.OverlaySize.MEDIUM -> 0.56f
            CarPlayClusterDisplay.OverlaySize.LARGE -> 0.70f
        }
        val heightFraction = when (size) {
            CarPlayClusterDisplay.OverlaySize.SMALL -> 0.22f
            CarPlayClusterDisplay.OverlaySize.MEDIUM -> 0.28f
            CarPlayClusterDisplay.OverlaySize.LARGE -> 0.34f
        }
        val width = (window.width * widthFraction).toInt().coerceAtLeast(140).coerceAtMost(window.width)
        val height = (window.height * heightFraction).toInt().coerceAtLeast(72).coerceAtMost(window.height)
        val inset = (window.width * 0.04f).toInt().coerceAtLeast(8)
        val top = window.top + (window.height * 0.08f).toInt()
        val left = when (position) {
            CarPlayClusterDisplay.OverlayPosition.LEFT -> window.left + inset
            CarPlayClusterDisplay.OverlayPosition.CENTER -> window.left + ((window.width - width) / 2)
            CarPlayClusterDisplay.OverlayPosition.RIGHT -> window.left + window.width - width - inset
        }
        return CardRect(
            left.coerceIn(window.left, window.left + window.width - width),
            top.coerceIn(window.top, window.top + window.height - height),
            width,
            height,
        )
    }

    internal fun visibleWindow(panelWidth: Int, panelHeight: Int): CardRect {
        val area = CarPlayClusterDisplay.SAFE_AREA_PERCENT
        val left = panelWidth * area.left / 100
        val top = panelHeight * area.top / 100
        val width = panelWidth * (100 - area.left - area.right) / 100
        val height = panelHeight * (100 - area.top - area.bottom) / 100
        return CardRect(left, top, width.coerceAtLeast(1), height.coerceAtLeast(1))
    }
}
