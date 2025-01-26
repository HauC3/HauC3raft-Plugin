package net.hauc3.hauc3raft

import de.pianoman911.mapengine.api.MapEngineApi
import de.pianoman911.mapengine.api.util.Converter
import de.pianoman911.mapengine.media.movingimages.FFmpegFrameSource
import jdk.jfr.Registered
import net.citizensnpcs.api.event.CitizensEnableEvent
import net.citizensnpcs.api.CitizensAPI
import net.hauc3.hauc3raft.commands.ModCommand
import net.hauc3.hauc3raft.listeners.PlayerMoveEventListener
import net.hauc3.hauc3raft.listeners.PlayerNodeChangeListener
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import org.bukkit.Bukkit
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.BlockVector
import java.net.URI

@Suppress("unused")
class HauC3raft: JavaPlugin(), Listener {

    companion object {
        lateinit var luckPerms: LuckPerms
    }
    private lateinit var mapEngine: MapEngineApi

    override fun onEnable() {
        logger.info("HauC3raft is licensed under the Blue Oak Model License located at https://blueoakcouncil.org/license/1.0.0")
        logger.info("EAT MY ASS MICROSOFT")
        logger.info("ALSO FUCK YOU CAMDEN :)")

        var provider: RegisteredServiceProvider<LuckPerms>? = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)
        if (provider != null) {
            luckPerms = provider.provider
            mapEngine = Bukkit.getServicesManager().load(MapEngineApi::class.java)!!
            PlayerNodeChangeListener(this, luckPerms).register()
            this.server.pluginManager.registerEvents(PlayerMoveEventListener(), this)
            this.getCommand("mod")?.setExecutor(ModCommand())
            server.pluginManager.registerEvents(this, this)
        } else {
            try {
                throw Exception("LuckPerms provider is null, who did a fucky wucky?")
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    private fun spawnDisplay(viewer: Player) {
        logger.info("spawning display for " + viewer.name)

        // create a map display instance
        val display = mapEngine.displayProvider().createBasic(BlockVector(42, 34, -70), BlockVector(48, 37, -70), BlockFace.SOUTH)
        display.spawn(viewer) // spawn the map display for the player

        // create an input pipeline element
        val input = mapEngine.pipeline().createDrawingSpace(display)

        // add a player to the pipeline context,
        // making the player receive the map
        input.ctx().receivers().add(viewer)

        // enable floyd-steinberg dithering
        input.ctx().converter(Converter.FLOYD_STEINBERG)

        input.ctx().buffering(true)

        // create a new frame source with a 10 frame buffer and rescaling enabled
        val source = FFmpegFrameSource(URI("https://live.hauc3.net/hls/stream.m3u8"), 10, input, true)

        // start the decoding process
        source.start()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        spawnDisplay(event.player)
    }

    @EventHandler
    fun onCitizensEnable(event: CitizensEnableEvent) {
        // val npc: NPC = CitizensAPI.getNPCRegistry().getNPC()
        logger.info("Citizens has been enabled")
    }
}