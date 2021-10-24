package fr.xpdustry.distributor.template;

import arc.*;
import arc.util.*;

import mindustry.*;
import mindustry.gen.*;
import mindustry.mod.Mods.*;
import mindustry.mod.*;
import mindustry.server.*;

import fr.xpdustry.distributor.command.mindustry.*;

import io.leangen.geantyref.*;


public abstract class DistributorPlugin extends Plugin implements Disposable{
    protected CommandRegistry<Playerc> serverRegistry;
    protected CommandRegistry<Playerc> clientRegistry;

    @Nullable
    public LoadedMod asMod(){
        return (Vars.mods != null) ? Vars.mods.getMod(this.getClass()) : null;
    }

    @Nullable
    public static CommandHandler getClientCommands(){
        return (Vars.netServer != null) ? Vars.netServer.clientCommands : null;
    }

    @Nullable
    public static CommandHandler getServerCommands(){
        if(Core.app == null) return null;
        ServerControl server = (ServerControl)Core.app.getListeners().find(listener -> listener instanceof ServerControl);
        return (server != null) ? server.handler : null;
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        serverRegistry = new CommandRegistry<>(handler, TypeToken.get(Playerc.class));
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        clientRegistry = new CommandRegistry<>(handler, TypeToken.get(Playerc.class));
    }

    @Override
    public void dispose(){
        /* Put something... */
    }
}