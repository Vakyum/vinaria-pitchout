package fr.sven.vinariaPitchout.commands;

import fr.sven.vinariaPitchout.VinariaPitchout;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ResetCommand implements CommandExecutor {


    private final VinariaPitchout plugin;


    public ResetCommand(VinariaPitchout plugin) {

        this.plugin = plugin;

    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (!(sender instanceof Player player)) {

            sender.sendMessage(
                    ChatColor.RED +
                            "Cette commande est réservée aux joueurs."
            );

            return true;

        }



        plugin.getKnockbackManager()
                .reset(player.getUniqueId());



        player.setLevel(0);



        player.sendMessage(
                ChatColor.GREEN +
                        "Votre puissance de knockback a été réinitialisée."
        );



        return true;

    }


}