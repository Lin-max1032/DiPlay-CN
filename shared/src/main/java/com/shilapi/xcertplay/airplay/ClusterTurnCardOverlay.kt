package com.shilapi.xcertplay.airplay

/**
 * Where DiPlay draws the instruction card on top of the dashboard map.
 *
 * Apple Maps paints the turn card inside the same safe area as the car marker, so the iPhone
 * cannot place the two independently. The overlay is DiPlay's own layer.
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
        val widthFraction = when (size) {
            CarPlayClusterDisplay.OverlaySize.SMALL -> 0.22f
            CarPlayClusterDisplay.OverlaySize.MEDIUM -> 0.30f
            CarPlayClusterDisplay.OverlaySize.LARGE -> 0.40f
        }
        val width = (panelWidth * widthFraction).toInt().coerceAtLeast(160).coerceAtMost(panelWidth)
        val height = (width * 0.52f).toInt().coerceAtMost((panelHeight * 0.70f).toInt()).coerceAtLeast(96)
        val marginX = (panelWidth * 0.04f).toInt().coerceAtLeast(16)
        val top = (panelHeight * 0.12f).toInt().coerceAtLeast(12)
        val left = when (position) {
            CarPlayClusterDisplay.OverlayPosition.LEFT -> marginX
            CarPlayClusterDisplay.OverlayPosition.CENTER -> ((panelWidth - width) / 2).coerceAtLeast(0)
            CarPlayClusterDisplay.OverlayPosition.RIGHT -> (panelWidth - width - marginX).coerceAtLeast(0)
        }
        return CardRect(left, top, width, height)
    }
}
