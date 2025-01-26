package net.hauc3.hauc3raft.commands

import de.myzelyam.api.vanish.VanishAPI
import net.hauc3.hauc3raft.HauC3raft
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import net.luckperms.api.node.NodeType
import net.luckperms.api.node.types.InheritanceNode
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.stream.Collectors

class ModCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player: Player?
        if (args.size == 1) {
            player = Bukkit.getPlayer(args[0])
            if (player == null) {
                if (sender is Player) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer not found"))
                } else {
                    sender.sendMessage("Player not found")
                }

                return true
            }
        } else {
            if (sender !is Player || args.size != 0) {
                return false
            }

            player = sender
        }

        val parent: String
        if (player.hasPermission("hauc3raft.mod.owner")) {
            parent = "owner"
        } else if (player.hasPermission("hauc3raft.mod.manager")) {
            parent = "manager"
        } else if (player.hasPermission("hauc3raft.mod.developer")) {
            parent = "developer"
        } else if (player.hasPermission("hauc3raft.mod.administrator")) {
            parent = "administrator"
        } else {
            if (!player.hasPermission("hauc3raft.mod.helper")) {
                if (sender is Player) {
                    sender.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&cPlayer does not have permission for a staff group"
                        )
                    )
                } else {
                    sender.sendMessage("Player does not have permission for a staff group")
                }

                return true
            }

            parent = "helper"
        }

        val userFuture = HauC3raft.luckPerms.userManager.loadUser(player.uniqueId)
        userFuture.thenAcceptAsync { user: User ->
            val groups = user.getNodes(NodeType.INHERITANCE).stream()
                .map { obj: InheritanceNode -> obj.groupName }
                .collect(Collectors.toSet())
            if (groups.contains(parent)) {
                this.setParent(
                    user,
                    if (player.hasPermission("hauc3raft.veteran")) "veteran" else "default"
                )
                if (sender == player) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Mod mode &4disabled"))
                } else if (sender is Player) {
                    sender.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&6Mod mode &4disabled &6for &c" + player.name
                        )
                    )
                    player.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&6Mod mode &4disabled &6by &c" + sender.getName()
                        )
                    )
                } else {
                    sender.sendMessage("Mod mode disabled for " + player.name)
                    player.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&6Mod mode &4disabled &6by &cCONSOLE"
                        )
                    )
                }

                if (VanishAPI.isInvisible(player)) {
                    VanishAPI.showPlayer(player)
                }
            } else {
                this.setParent(user, parent)
                if (sender == player) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Mod mode &aenabled"))
                } else if (sender is Player) {
                    sender.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&6Mod mode &aenabled &6for &c" + player.name
                        )
                    )
                    player.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&6Mod mode &aenabled &6by &c" + sender.getName()
                        )
                    )
                } else {
                    sender.sendMessage("Mod mode enabled for " + player.name)
                    player.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&6Mod mode &aenabled &6by &cCONSOLE"
                        )
                    )
                }
            }
        }
        return true
    }

    private fun setParent(user: User, parent: String) {
        HauC3raft.luckPerms.userManager.modifyUser(user.uniqueId) { _user: User ->
            _user.data().clear { node: Node? ->
                NodeType.INHERITANCE.matches(
                    node!!
                )
            }
            val node: Node = InheritanceNode.builder(parent).build()
            _user.data().add(node)
        }
    }
}
