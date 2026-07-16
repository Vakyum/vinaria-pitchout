package fr.sven.vinariaPitchout.managers;

import java.util.HashMap;
import java.util.UUID;


public class CooldownManager {


    private final HashMap<UUID, Long> cooldowns = new HashMap<>();


    /**
     * Définit un cooldown pour un joueur
     *
     * @param uuid joueur concerné
     * @param seconds durée en secondes
     */
    public void setCooldown(UUID uuid, int seconds) {

        long endTime = System.currentTimeMillis() + (seconds * 1000L);

        cooldowns.put(uuid, endTime);

    }



    /**
     * Vérifie si le joueur est encore en cooldown
     */
    public boolean hasCooldown(UUID uuid) {

        if (!cooldowns.containsKey(uuid)) {
            return false;
        }


        long endTime = cooldowns.get(uuid);


        if (System.currentTimeMillis() >= endTime) {

            cooldowns.remove(uuid);
            return false;

        }


        return true;

    }



    /**
     * Retourne le temps restant en secondes
     */
    public int getRemaining(UUID uuid) {

        if (!hasCooldown(uuid)) {
            return 0;
        }


        long remaining = cooldowns.get(uuid) - System.currentTimeMillis();


        return (int) Math.ceil(remaining / 1000.0);

    }



    /**
     * Supprime le cooldown d'un joueur
     */
    public void reset(UUID uuid) {

        cooldowns.remove(uuid);

    }



    /**
     * Vide tous les cooldowns
     */
    public void clear() {

        cooldowns.clear();

    }

}