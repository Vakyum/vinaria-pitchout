package fr.sven.vinariaPitchout;

import fr.sven.vinariaPitchout.commands.GameCommand;
import fr.sven.vinariaPitchout.commands.ResetCommand;
import fr.sven.vinariaPitchout.listeners.GameListener;
import fr.sven.vinariaPitchout.managers.CooldownManager;
import fr.sven.vinariaPitchout.managers.GameManager;
import fr.sven.vinariaPitchout.managers.KnockbackManager;

import org.bukkit.plugin.java.JavaPlugin;


public class VinariaPitchout extends JavaPlugin {


    private static VinariaPitchout instance;

    private KnockbackManager knockbackManager;
    private CooldownManager cooldownManager;
    private GameManager gameManager;

    @Override
    public void onEnable() {

        instance = this;


        // Création des managers
        knockbackManager = new KnockbackManager();
        cooldownManager = new CooldownManager();
        gameManager = new GameManager(this);

        // Commandes
        getCommand("game").setExecutor(new GameCommand(this));
        getCommand("reset").setExecutor(new ResetCommand(this));


        // Events
        getServer()
                .getPluginManager()
                .registerEvents(
                        new GameListener(this),
                        this
                );


        getLogger().info("GamePlugin activé !");
        getServer().getMessenger().registerOutgoingPluginChannel(
                this,
                "BungeeCord"
        );
    }


    @Override
    public void onDisable() {

        getLogger().info("VinariaPitchout désactivé !");

    }



    public static VinariaPitchout getInstance() {

        return instance;

    }



    public KnockbackManager getKnockbackManager() {

        return knockbackManager;

    }



    public CooldownManager getCooldownManager() {

        return cooldownManager;

    }

    public GameManager getGameManager() {

        return gameManager;

    }

}