package com.shilapi.xcertplay.airplay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClusterTurnCardOverlayTest {
    @Test
    fun leftCentreAndRightStayInsideTheVisibleNaviWindow() {
        val panelWidth = 1920
        val panelHeight = 720
        val window = ClusterTurnCardOverlay.visibleWindow(panelWidth, panelHeight)
        val left = ClusterTurnCardOverlay.card(
            panelWidth, panelHeight,
            CarPlayClusterDisplay.OverlayPosition.LEFT,
            CarPlayClusterDisplay.OverlaySize.SMALL,
        )
        val centre = ClusterTurnCardOverlay.card(
            panelWidth, panelHeight,
            CarPlayClusterDisplay.OverlayPosition.CENTER,
            CarPlayClusterDisplay.OverlaySize.SMALL,
        )
        val right = ClusterTurnCardOverlay.card(
            panelWidth, panelHeight,
            CarPlayClusterDisplay.OverlayPosition.RIGHT,
            CarPlayClusterDisplay.OverlaySize.SMALL,
        )

        assertTrue(left.left < centre.left)
        assertTrue(centre.left < right.left)
        for (card in listOf(left, centre, right)) {
            assertTrue(card.left >= window.left)
            assertTrue(card.top >= window.top)
            assertTrue(card.left + card.width <= window.left + window.width)
            assertTrue(card.top + card.height <= window.top + window.height)
        }
        assertEquals(left.width, right.width)
    }

    @Test
    fun largeCardDoesNotCoverTheWholePanel() {
        val large = ClusterTurnCardOverlay.card(
            1920, 720,
            CarPlayClusterDisplay.OverlayPosition.RIGHT,
            CarPlayClusterDisplay.OverlaySize.LARGE,
        )
        assertTrue(large.width < 1920 / 2)
        assertTrue(large.height < 720 / 2)
        assertTrue(large.left > 1920 / 3)
        assertTrue(large.left + large.width < 1920 * 72 / 100)
    }

    @Test
    fun largerSizeGrowsTheCardInsideTheWindow() {
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
        assertTrue(small.width < 1920 * 0.20)
    }
}
