package net.hauc3.hauc3raft.listeners

import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId

class PlayerMoveEventListener : Listener {
    @EventHandler
    fun onPlayerMoveEvent(event: PlayerMoveEvent) {
        if (event.to.world.environment != World.Environment.NORMAL) return  // cancel if not overworld

        if (LocalDateTime.now(ZoneId.of("America/Chicago")).dayOfWeek != DayOfWeek.TUESDAY) return  // cancel if it's not tuesday

        val positions = arrayOf(
            intArrayOf(4, 115, -6),
            intArrayOf(5, 115, -6),
            intArrayOf(6, 115, -6),
            intArrayOf(4, 115, -7),
            intArrayOf(5, 115, -7),
            intArrayOf(5, 115, -8)
        ) // list of positions
        for (position in positions) {
            if (event.to.blockX == position[0] && event.to.blockY == position[1] && event.to.blockZ == position[2]) { // if position being checked for equals the player's new position
                event.player.health = 0.0 // kill
                return
            }
        }
    }
}
