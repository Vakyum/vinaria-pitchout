package fr.sven.vinariaPitchout.utils;

import fr.sven.vinariaPitchout.VinariaPitchout;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class Countdown {


    private final VinariaPitchout plugin;


    public Countdown(VinariaPitchout plugin) {

        this.plugin = plugin;

    }



    public void start(Runnable finish) {


        new BukkitRunnable() {


            int time = 5;



            @Override
            public void run() {


                if (time > 0) {


                    for (Player player : Bukkit.getOnlinePlayers()) {


                        player.sendTitle(
                                ChatColor.YELLOW + "" + time,
                                "",
                                0,
                                20,
                                0
                        );


                        player.playSound(
                                player.getLocation(),
                                Sound.BLOCK_NOTE_BLOCK_BELL,
                                1,
                                1
                        );


                    }


                    time--;


                } else {


                    for (Player player : Bukkit.getOnlinePlayers()) {


                        player.sendTitle(
                                ChatColor.GREEN + "Go!",
                                ChatColor.GOLD + "Bonne Chance !",
                                0,
                                20,
                                10
                        );


                        player.playSound(
                                player.getLocation(),
                                Sound.ENTITY_VILLAGER_AMBIENT,
                                1,
                                1
                        );


                    }


                    finish.run();


                    cancel();

                }

            }


        }.runTaskTimer(
                plugin,
                0L,
                20L
        );


    }


}