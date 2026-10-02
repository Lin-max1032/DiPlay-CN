package com.shilapi.xcertplay.airplay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClusterTurnCardOverlayTest {
    @Test
    fun leftCentreAndRightStayInsideATypicalClusterPanel() {
        val panelWidth = 1920
        val panelHeight = 720
        val left = ClusterTurnCardOverlay.card(
            panelWidth, panelHeight,
            CarPlayClusterDisplay.OverlayPosition.LEFT,
            CarPlayClusterDisplay.OverlaySize.MEDIUM,
        )
        val centre = ClusterTurnCardOverlay.card(
            panelWidth, panelHeight,
            CarPlayClusterDisplay.OverlayPosition.CENTER,
            CarPlayClusterDisplay.OverlaySize.MEDIUM,
        )
        val right = ClusterTurnCardOverlay.card(
            panelWidth, panelHeight,
            CarPlayClusterDisplay.OverlayPosition.RIGHT,
            CarPlayClusterDisplay.OverlaySize.MEDIUM,
        )

        assertTrue(left.left < centre.left)
        assertTrue(centre.left < right.left)
        for (card in listOf(left, centre, right)) {
            assertTrue(card.left >= 0)
            assertTrue(card.top >= 0)
            assertTrue(card.left + card.width <= panelWidth)
            assertTrue(card.top + card.height <= panelHeight)
        }
        assertEquals(left.width, right.width)
        assertEquals((panelWidth - centre.width) / 2, centre.left)
    }

    @Test
    fun largerSizeGrowsTheCard() {
        val small = ClusterTurnCardOverlay.card(
            1920, 720,
            CarPlayClusterDisplay.OverlayPosition.LEFT,
            CarPlayClusterDisplay.OverlaySize.SMALL,
        )
        val large = ClusterTurnCardOverlay.card(
            1920, 720,
            CarPlayClusterDisplay.OverlayPosition.LEFT,
            CarPlayClusterDisplay.OverlaySize.LARGE,
        )
        assertTrue(large.width > small.width)
        assertTrue(large.height > small.height)
    }
}
