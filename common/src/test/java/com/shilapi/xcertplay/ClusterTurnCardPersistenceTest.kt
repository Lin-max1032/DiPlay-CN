package com.shilapi.xcertplay

import com.shilapi.xcertplay.airplay.CarPlayClusterDisplay
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], manifest = Config.NONE)
class ClusterTurnCardPersistenceTest {
    private val context get() = RuntimeEnvironment.getApplication()

    @Before fun clearPreferences() {
        context.getSharedPreferences("xcertplay_airplay", 0).edit().clear().apply()
        AirPlayPersistence.overlaySettingsListener = null
    }

    @Test fun overlayDefaultsToLeftMedium() {
        assertEquals(CarPlayClusterDisplay.OverlayPosition.LEFT, AirPlayPersistence.loadClusterTurnCardOverlayPosition(context))
        assertEquals(CarPlayClusterDisplay.OverlaySize.MEDIUM, AirPlayPersistence.loadClusterTurnCardOverlaySize(context))
    }

    @Test fun overlayPositionAndSizeRoundTrip() {
        AirPlayPersistence.saveClusterTurnCardOverlayPosition(context, CarPlayClusterDisplay.OverlayPosition.RIGHT)
        AirPlayPersistence.saveClusterTurnCardOverlaySize(context, CarPlayClusterDisplay.OverlaySize.LARGE)
        assertEquals(CarPlayClusterDisplay.OverlayPosition.RIGHT, AirPlayPersistence.loadClusterTurnCardOverlayPosition(context))
        assertEquals(CarPlayClusterDisplay.OverlaySize.LARGE, AirPlayPersistence.loadClusterTurnCardOverlaySize(context))
    }

    @Test fun overlaySaveNotifiesTheHostWithoutReconnecting() {
        var noticed = 0
        AirPlayPersistence.overlaySettingsListener = { noticed++ }
        AirPlayPersistence.saveClusterTurnCardOverlayPosition(context, CarPlayClusterDisplay.OverlayPosition.CENTER)
        AirPlayPersistence.saveClusterTurnCardOverlaySize(context, CarPlayClusterDisplay.OverlaySize.SMALL)
        assertEquals(2, noticed)
    }
}
