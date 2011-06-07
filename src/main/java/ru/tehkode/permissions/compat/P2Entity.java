/*
 * PermissionsEx - Permissions plugin for Bukkit
 * Copyright (C) 2011 t3hk0d3 http://www.tehkode.ru
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package ru.tehkode.permissions.compat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.config.Configuration;
import ru.tehkode.permissions.config.ConfigurationNode;

public class P2Entity extends PermissionEntity {

    protected P2Backend backend;
    protected Map<String, ConfigurationNode> worldNodes = new ConcurrentHashMap<String, ConfigurationNode>();

    public P2Entity(String name, PermissionManager manager, P2Backend backend) {
        super(name, manager);

        this.backend = backend;
    }

    public void load(String world, ConfigurationNode node) {
        this.worldNodes.put(world, node);
    }

    @Override
    public String getPrefix() {
        return this.getOption("prefix", this.backend.getDefaultWorld(), false);
    }

    @Override
    public String getSuffix() {
        return this.getOption("suffix", this.backend.getDefaultWorld(), false);
    }

    public ConfigurationNode getNode(String world) {
        if (!this.worldNodes.containsKey(world)) {
            this.worldNodes.put(world, Configuration.getEmptyNode());
        }

        return this.worldNodes.get(world);
    }

    public ConfigurationNode getDefaultWorldNode() {
        return this.getNode(this.backend.getDefaultWorld());
    }

    public String[] getPermissions(String world, boolean inheritance) {
        List<String> permissions = new LinkedList<String>();

        if (world != null && !world.isEmpty()) {
            ConfigurationNode worldNode = this.getNode(world);
            permissions.addAll(worldNode.getStringList("permissions", new LinkedList<String>()));
        }

        if ((inheritance && !this.backend.getDefaultWorld().equals(world) || (world == null || world.isEmpty()))) {
            permissions.addAll(this.getDefaultWorldNode().getStringList("permissions", new LinkedList<String>()));
        }
        
        // Don't add permission if entity already have this permission
        if (!this.has("modifyworld.blocks.place", world) && this.getOption("build", world, false).equals("true")) {
            permissions.add("modifyworld.*");
        }

        return permissions.toArray(new String[0]);
    }

    @Override
    public String[] getPermissions(String world) {
        return this.getPermissions(world, true);
    }

    @Override
    public Map<String, Map<String, String>> getAllOptions() {
        Map<String, Map<String, String>> allOptions = new HashMap<String, Map<String, String>>();

        for (String world : this.worldNodes.keySet()) {
            if (world.equals(this.backend.getDefaultWorld())) {
                world = "";
            }
            allOptions.put(world, this.getOptions(world, false));
        }

        return allOptions;
    }

    @Override
    public Map<String, String[]> getAllPermissions() {
        Map<String, String[]> permissions = new HashMap<String, String[]>();

        for (String world : this.worldNodes.keySet()) {
            if (world.equals(this.backend.getDefaultWorld())) {
                world = "";
            }
            
            permissions.put(world, this.getPermissions(world, false));
        }

        return permissions;
    }

    @Override
    public String getOption(String permission, String world, boolean inheritance) {
        Object value = null;

        if (world != null && !world.isEmpty()) {
            value = this.getNode(world).getProperty("info." + permission);
        }

        if ((inheritance && value == null) || (world == null || world.isEmpty())) {
            value = this.getDefaultWorldNode().getProperty("info." + permission);
        }

        if (value == null) { // NPE Safety
            value = "";
        }

        return value.toString();
    }

    @Override
    public Map<String, String> getOptions(String world) {
        return this.getOptions(world, true);
    }

    public Map<String, String> getOptions(String world, boolean inheritance) {
        Map<String, String> options = new HashMap<String, String>();

        if (inheritance && !this.backend.getDefaultWorld().equals(world)) {
            options.putAll(this.getOptions(this.backend.getDefaultWorld(), false));
        }

        ConfigurationNode node = this.getNode(world);
        List<String> worldOptions = node.getKeys("info");
        if (worldOptions != null) {
            for (String option : worldOptions) {
                if ("prefix".equals(option) || "suffix".equals(option) || "build".equals(option)) {
                    continue;
                }

                Object property = node.getProperty("info." + option);

                if (property == null) {
                    continue;
                }

                if (property instanceof Map) {
                    // TODO Write submap handling code
                } else {
                    options.put(option, property.toString());
                }
            }
        }
        return options;
    }

    @Override
    public void addPermission(String string, String string1) {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat are read-only");
    }

    @Override
    public void remove() {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat are read-only");
    }

    @Override
    public void removePermission(String string, String string1) {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat are read-only");
    }

    @Override
    public void save() {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat are read-only");
    }

    @Override
    public void setOption(String string, String string1, String string2) {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat are read-only");
    }

    @Override
    public void setPermissions(String[] strings, String string) {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat are read-only");
    }
}
