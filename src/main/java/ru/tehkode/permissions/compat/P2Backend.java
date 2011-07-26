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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import ru.tehkode.permissions.PermissionBackend;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.config.Configuration;
import ru.tehkode.permissions.config.ConfigurationNode;

public class P2Backend extends PermissionBackend implements FilenameFilter {

    protected Map<String, Configuration> worldPermissions = new HashMap<String, Configuration>();
    protected String defaultWorld = "";
    protected File configDir;
    protected Map<String, P2Group> defaultGroups = new HashMap<String, P2Group>();
    protected Map<String, P2Group> groups = new HashMap<String, P2Group>();
    protected Map<String, P2User> users = new HashMap<String, P2User>();

    public P2Backend(PermissionManager manager, Configuration config) {
        super(manager, config);
    }

    @Override
    public void initialize() {
        this.configDir = new File(config.getString("permissions.backends.p2compat.directory", "plugins/Permissions/"));

        this.loadPermissions(this.configDir);
    }

    @Override
    public PermissionGroup getDefaultGroup(String worldName) {
        return this.defaultGroups.get(worldName);
    }

    @Override
    public void setDefaultGroup(PermissionGroup pg, String worldName) {
        Logger.getLogger("Minecraft").severe("P2Compat is read-only");
    }
    
    @Override
    public PermissionGroup getGroup(String string) {
        if (!this.groups.containsKey(string)) {
            this.groups.put(string, new P2Group(string, manager, this));
        }

        return this.groups.get(string);
    }

    @Override
    public PermissionGroup[] getGroups() {
        return this.groups.values().toArray(new PermissionGroup[0]);
    }

    @Override
    public PermissionUser getUser(String string) {
        if (!this.users.containsKey(string)) {
            this.users.put(string, new P2User(string, manager, this));
        }

        return this.users.get(string);
    }

    @Override
    public PermissionUser[] getUsers() {
        return this.users.values().toArray(new PermissionUser[0]);
    }

    @Override
    public void reload() {
        this.loadPermissions(configDir);
    }

    public String getDefaultWorld() {
        return defaultWorld;
    }

    protected final void loadPermissions(File dir) {
        if (!dir.exists()) {
            throw new RuntimeException("Specified directory doesn't exist. Check \"permissions.backends.p2compat.directory\" param.");
        }

        this.worldPermissions = new HashMap<String, Configuration>();
        this.users.clear();
        this.groups.clear();
        this.defaultGroups.clear();

        File[] configFiles = dir.listFiles(this);

        for (File configFile : configFiles) {
            String worldName = configFile.getName().replace(".yml", "");
            Configuration world = new Configuration(configFile);
            this.worldPermissions.put(worldName, world);

            world.load();

            Logger.getLogger("Minecraft").info("[PermissionsCompat] Parsing \"" + configFile.getName() + "\" file");

            this.parseWorld(worldName, world);
        }
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".yml");
    }

    protected void parseWorld(String worldName, Configuration world) {
        // Load groups
        Map<String, ConfigurationNode> worldGroups = world.getNodesMap("groups");

        if (worldGroups != null) {
            for (Map.Entry<String, ConfigurationNode> entry : worldGroups.entrySet()) {
                P2Group group = (P2Group) this.getGroup(entry.getKey());
                group.load(worldName, entry.getValue());

                if (entry.getValue().getBoolean("default", false)) {
                    this.defaultGroups.put(worldName, group);
                }
            }
        }

        // Load users
        Map<String, ConfigurationNode> userGroups = world.getNodesMap("users");

        if (userGroups != null) {
            for (Map.Entry<String, ConfigurationNode> entry : userGroups.entrySet()) {
                P2User user = (P2User) this.getUser(entry.getKey());
                user.load(worldName, entry.getValue());
            }
        }
    }

    private String getDefaultWorldName() {
        if (this.defaultWorld.isEmpty()) {
            this.defaultWorld = Bukkit.getServer().getWorlds().get(0).getName();
        }
        return this.defaultWorld;
    }

    @Override
    public String[] getWorldInheritance(String world) {
        if (!worldPermissions.containsKey(world)) {
            return new String[0];
        }

        ConfigurationNode worldNode = this.worldPermissions.get(world);
        String worldsCopy = worldNode.getString("plugin.permissions.copies", null);
        if (worldsCopy == null || worldsCopy.equalsIgnoreCase(this.defaultWorld)) {
            return new String[0];
        }

        return new String[]{worldsCopy};

    }

    @Override
    public void setWorldInheritance(String string, String[] strings) {
        Logger.getLogger("Minecraft").severe("P2Compat is read-only");
    }

    @Override
    public void dumpData(OutputStreamWriter writer) throws IOException {
        Logger.getLogger("Minecraft").severe("P2Compat is read-only");
    }
}
