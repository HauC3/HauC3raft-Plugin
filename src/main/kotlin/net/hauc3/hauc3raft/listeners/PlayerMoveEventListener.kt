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
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.to.world.environment == World.Environment.NORMAL) {
            if (LocalDateTime.now(ZoneId.of("America/Chicago")).dayOfWeek == DayOfWeek.TUESDAY) {
                val positions = arrayOf(
                    intArrayOf(-7, 65, 20),
                    intArrayOf(-8, 65, 20),
                    intArrayOf(-9, 65, 20),
                    intArrayOf(-7, 65, 19),
                    intArrayOf(-8, 65, 19),
                    intArrayOf(-9, 65, 19),
                    intArrayOf(-10, 65, 19),
                    intArrayOf(-8, 65, 18),
                    intArrayOf(-9, 65, 18),
                    intArrayOf(-10, 65, 18)
                )

                for (position in positions) {
                    if (event.to.blockX == position[0] && event.to.blockY == position[1] && event.to.blockZ == position[2]) {
                        event.player.health = 0.0
                        return
                    }
                }
            }
        }
    }
}
