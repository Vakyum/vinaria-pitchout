package fr.sven.vinariaPitchout.commands;

import fr.sven.vinariaPitchout.VinariaPitchout;
import fr.sven.vinariaPitchout.utils.Countdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class GameCommand implements CommandExecutor {


    private final VinariaPitchout plugin;


    public GameCommand(VinariaPitchout plugin) {

        this.plugin = plugin;

    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (!(sender.hasPermission("game.admin"))) {

            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission.");
            return true;

        }



        if (args.length == 0) {

            sender.sendMessage(ChatColor.YELLOW + "/game start");
            return true;

        }



        if (args[0].equalsIgnoreCase("start")) {


            startGame();


            return true;

        }


        return true;

    }




    private void startGame() {


        World world = Bukkit.getWorld("world");


        if (world == null) {

            Bukkit.getLogger().warning("Le monde 'world' est introuvable !");
            return;

        }



        /*
         * Préparation des joueurs
         */

        for (Player player : world.getPlayers()) {


            player.getInventory().clear();

            player.setLevel(0);

            player.setExp(0);

            player.setGameMode(GameMode.ADVENTURE);


            plugin.getKnockbackManager()
                    .reset(player.getUniqueId());

        }



        /*
         * Countdown
         */

        new Countdown(plugin).start(() -> {


            Location spawn = new Location(
                    world,
                    100,
                    210,
                    100
            );



            for (Player player : Bukkit.getOnlinePlayers()) {



                // Résistance 255 pendant 30 secondes

                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.RESISTANCE,
                                20 * 30,
                                255,
                                false,
                                false
                        )
                );



                player.teleport(spawn);



                // Items de départ

                player.getInventory().addItem(
                        new ItemStack(Material.STICK)
                );


                player.getInventory().addItem(
                        new ItemStack(
                                Material.SNOWBALL,
                                16
                        )
                );


                player.getInventory().addItem(
                        new ItemStack(Material.FEATHER)
                );


                player.getInventory().addItem(
                        new ItemStack(Material.FISHING_ROD)
                );



                player.sendMessage(
                        ChatColor.GREEN +
                                "Le jeu commence !"
                );


            }


        });


    }


}