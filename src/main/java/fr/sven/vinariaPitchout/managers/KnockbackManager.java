package fr.sven.vinariaPitchout.managers;

import java.util.HashMap;
import java.util.UUID;


public class KnockbackManager {


    private final HashMap<UUID, Double> knockbacks = new HashMap<>();


    /**
     * Ajoute 0.2 de puissance de knockback au joueur
     */
    public double addKnockback(UUID uuid) {

        double value = getKnockback(uuid);

        value += 0.2;

        knockbacks.put(uuid, value);

        return value;

    }



    /**
     * Récupère le niveau actuel de knockback
     */
    public double getKnockback(UUID uuid) {

        return knockbacks.getOrDefault(uuid, 0.0);

    }



    /**
     * Supprime le knockback d'un joueur
     */
    public void reset(UUID uuid) {

        knockbacks.remove(uuid);

    }



    /**
     * Reset tous les joueurs
     */
    public void clear() {

        knockbacks.clear();

    }


}