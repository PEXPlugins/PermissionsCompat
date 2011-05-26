package com.nijikokun.bukkit.Permissions;

import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.compat.P2Backend;
import ru.tehkode.permissions.PermissionBackend;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Legacy Permissions Compatibility Layer
 */
/**
 * Permissions 2.x
 * Copyright (C) 2011  Matt 'The Yeti' Burnett <admin@theyeticave.net>
 * Original Credit & Copyright (C) 2010 Nijikokun <nijikokun@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Permissions Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Permissions Public License for more details.
 *
 * You should have received a copy of the GNU Permissions Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Permissions extends JavaPlugin {

    public static Plugin instance = null;
    public static PermissionHandler Security;

    public Permissions() {
        super();
        
        PermissionBackend.registerBackendAlias("p2compat", P2Backend.class);
        
        Permissions.instance = getInstance();
        
        Logger.getLogger("Minecraft").info("[PermissionsCompat] Compatibility Layer Initalized!");
    }

    public static Plugin getInstance() {
        if (instance == null) {
            instance = Bukkit.getServer().getPluginManager().getPlugin("Permissions");
        }

        return instance;
    }

    @Override
    public void onLoad() {
        try {            
            Security = this.getHandler();
        } catch (RuntimeException e) {
            Logger.getLogger("Minecraft").warning("[PermissionsCompat] PermissionsEx plugin not found. Some plugins may not work.");
        }
    }

    @Override
    public void onEnable() {
        Logger.getLogger("Minecraft").info("[PermissionsCompat] Compatibility layer enabled.");
    }

    @Override
    public void onDisable() {
        Security = null;
    }

    public PermissionHandler getHandler() {
        if (Security == null) {
            Security = new ru.tehkode.permissions.compat.PermissionHandler(PermissionsEx.getPermissionManager());
        }

        return Security;
    }
}
