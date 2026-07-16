package fr.sven.vinariaPitchout.listeners;

import fr.sven.vinariaPitchout.VinariaPitchout;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.entity.Snowball;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class GameListener implements Listener {


    private final VinariaPitchout plugin;


    public GameListener(VinariaPitchout plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.getGameManager().isGameRunning()) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            return;
        }
        if (plugin.getGameManager().canStartGame()) {
            plugin.getGameManager().startGame();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

    Player player = event.getPlayer();
        player.getInventory().clear();
        if (plugin.getGameManager().isGameRunning()) {

            plugin.getGameManager()
                    .removePlayer(player.getUniqueId());

        }

    }

    /*
     * Gestion du knockback
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!plugin.getGameManager().isGameRunning()) {
            event.setCancelled(true);
            return;
        }
        if (!(event.getEntity() instanceof Player victim))
            return;
        if (!(event.getDamager() instanceof Player attacker))
            return;
        ItemStack item = attacker.getInventory().getItemInMainHand();
        Material material = item.getType();

        /*
         * Bâton ou boule de neige
         */
        if (material == Material.STICK ||
                material == Material.SNOWBALL) {
            double kb =
                    plugin.getKnockbackManager()
                            .addKnockback(
                                    victim.getUniqueId()
                            );
            victim.setLevel(
                    (int)(kb * 10)
            );
            Vector direction =
                    victim.getLocation()
                            .toVector()
                            .subtract(
                                    attacker.getLocation()
                                            .toVector()
                            )
                            .normalize();
            direction.multiply(kb * 3);
            direction.setY(kb);
            victim.setVelocity(direction);
            event.setCancelled(true);
        }

        /*
         * Autres attaques = gros recul
         */
        else {
            Vector direction =
                    victim.getLocation()
                            .toVector()
                            .subtract(
                                    attacker.getLocation()
                                            .toVector()
                            )
                            .normalize();
            direction.multiply(5);
            direction.setY(2);
            victim.setVelocity(direction);
        }
    }


    /*
     * Dash avec plume
     */
    @EventHandler
    public void onFeather(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory()
                .getItemInMainHand()
                .getType()
                != Material.FEATHER)
            return;
        if (plugin.getCooldownManager()
                .hasCooldown(
                        player.getUniqueId()
                )) {
            player.sendMessage(
                    ChatColor.GOLD +
                            "En cooldown !"
            );
            return;
        }
        Vector direction =
                player.getLocation()
                        .getDirection()
                        .normalize()
                        .multiply(5);
        player.setVelocity(direction);
        plugin.getCooldownManager()
                .setCooldown(
                        player.getUniqueId(),
                        10
                );
    }

    /*
     * Désactivation des dégâts de chute
     */
    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if(event.getCause()
                == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    /*
     * Empêche le drop des objets
     */
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }


    /*
     * Mort du joueur
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.getKnockbackManager()
                .reset(
                        player.getUniqueId()
                );
        player.setGameMode(
                GameMode.SPECTATOR
        );
        plugin.getGameManager().removePlayer(player.getUniqueId());
    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {

        if (!(event.getEntity() instanceof Snowball snowball))
            return;


        if (!(snowball.getShooter() instanceof Player attacker))
            return;


        if (!(event.getHitEntity() instanceof Player victim))
            return;



        double kb = plugin.getKnockbackManager()
                .addKnockback(victim.getUniqueId());



        victim.setLevel((int)(kb * 10));



        Vector direction =
                victim.getLocation()
                        .toVector()
                        .subtract(
                                attacker.getLocation()
                                        .toVector()
                        )
                        .normalize();



        direction.multiply(kb * 3);

        direction.setY(kb);



        victim.setVelocity(direction);



        snowball.remove();

    }

}

