package net.hauc3.hauc3raft.listeners

import net.luckperms.api.LuckPerms
import net.luckperms.api.event.node.NodeAddEvent
import net.luckperms.api.event.node.NodeRemoveEvent
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import net.luckperms.api.node.types.InheritanceNode
import net.luckperms.api.node.types.PrefixNode
import net.luckperms.api.node.types.SuffixNode
import org.bukkit.plugin.java.JavaPlugin

class PlayerNodeChangeListener(private val plugin: JavaPlugin, private val luckPerms: LuckPerms) {
    fun register() {
        val eventBus = luckPerms.eventBus
        eventBus.subscribe(
            this.plugin,
            NodeAddEvent::class.java
        ) { e: NodeAddEvent -> this.onNodeAdd(e) }
        eventBus.subscribe(
            this.plugin,
            NodeRemoveEvent::class.java
        ) { e: NodeRemoveEvent -> this.onNodeRemove(e) }
    }

    private fun onNodeAdd(e: NodeAddEvent) {
        if (e.isUser) {
            invalidateCache(e.target as User, e.node)
        }
    }

    private fun onNodeRemove(e: NodeRemoveEvent) {
        if (e.isUser) {
            invalidateCache(e.target as User, e.node)
        }
    }

    private fun invalidateCache(target: User, node: Node) {
        plugin.server.scheduler.runTask(this.plugin, Runnable {
            val player = plugin.server.getPlayer(target.uniqueId)
            if (player != null && (node is InheritanceNode || node is PrefixNode || node is SuffixNode)) {
                target.cachedData.invalidate()
            }
        })
    }
}
