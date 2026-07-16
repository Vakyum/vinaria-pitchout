package fr.sven.vinariaPitchout.managers;

import fr.sven.vinariaPitchout.VinariaPitchout;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GameManager {

    private final VinariaPitchout plugin;
    private final ArrayList<UUID> alivePlayers = new ArrayList<>();
    private boolean gameRunning = false;

    public GameManager(VinariaPitchout plugin) {
        this.plugin = plugin;
    }

    public void startCountdown() {
        new BukkitRunnable() {
            int time = 5;
            @Override
            public void run() {
                if (time == 0) {
                    cancel();
                    startPlaying();
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(
                            "§e" + time,
                            "",
                            10,
                            20,
                            10
                    );
                }
                time--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public boolean canStartGame() {
        return Bukkit.getOnlinePlayers().size() >= 2;
    }

    public void startGame() {
        if (gameRunning) {
            return;
        }
        gameRunning = true;
        alivePlayers.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            alivePlayers.add(player.getUniqueId());
        }
        startCountdown();
    }

    public void startPlaying() {
        gameRunning = true;
        System.out.println("Start playing lancé");
        Location spawn = new Location(
                Bukkit.getWorld("world"),
                100,
                210,
                100
        );
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.teleport(spawn);
            player.setLevel(0);
            player.getInventory().addItem(new ItemStack(Material.STICK, 1));
            player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 16));
            player.getInventory().addItem(new ItemStack(Material.FEATHER, 1));
            player.getInventory().addItem(new ItemStack(Material.FISHING_ROD, 1));
        }
        startVoidChecker();
    }
    public boolean isGameRunning() {
        return gameRunning;
    }

    public void startVoidChecker() {
        System.out.println("Void checker lancé");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!gameRunning) {
                    cancel();
                    return;
                }
                for (UUID uuid : new ArrayList<>(alivePlayers)) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        continue;
                    }
                    if (player.getLocation().getY() < 0) {
                        System.out.println(player.getName() + " est dans le vide");
                        removePlayer(uuid);
                        player.setGameMode(GameMode.SPECTATOR);
                        player.teleport(new Location(
                                Bukkit.getWorld("world"),
                                100,
                                210,
                                100
                        ));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    public void addPlayer(UUID player) {
        alivePlayers.add(player);
    }
    public void removePlayer(UUID player) {
        alivePlayers.remove(player);
        if (hasWinner()) {
            UUID winner = getWinner();
            Player winnerPlayer = plugin.getServer().getPlayer(winner);
            if (winnerPlayer != null) {
                winnerPlayer.sendTitle(
                        "Victoire !",
                        "Vous avez gagné !",
                        10,
                        70,
                        20
                );
            }
            for (Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
                if (!otherPlayer.getUniqueId().equals(winner)) {
                    otherPlayer.sendTitle(
                            "Défaite !",
                            "Le gagnant est " + winnerPlayer.getName(),
                            10,
                            70,
                            20
                    );
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    stopGame();
                    sendPlayersToLobby();
                    resetGame();
                }
            }.runTaskLater(plugin, 20L * 10);
        }
    }
    public void resetGame() {
        alivePlayers.clear();
        gameRunning = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
        }
    }
    public ArrayList<UUID> getAlivePlayers() {
        return alivePlayers;
    }

    public boolean hasWinner() {
        return alivePlayers.size() == 1;
    }
    public UUID getWinner() {
        if (hasWinner()) {
            return alivePlayers.get(0);
        }
        return null;
    }
    public void stopGame() {
        gameRunning = false;
    }

    public void sendPlayersToLobby() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteOut);
            try {
                out.writeUTF("Connect");
                out.writeUTF("lobby");
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendPluginMessage(
                    plugin,
                    "BungeeCord",
                    byteOut.toByteArray()
            );
        }
    }
}