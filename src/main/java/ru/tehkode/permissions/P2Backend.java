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
package ru.tehkode.permissions;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import ru.tehkode.permissions.config.Configuration;

public class P2Backend extends PermissionBackend {

    protected Map<String, Configuration> worldPermissions = new HashMap<String, Configuration>();
    protected String defaultWorld = "";
    protected File configDir;
    protected P2Group defaultGroup;
    protected List<P2Group> groups = new LinkedList<P2Group>();
    protected List<P2User> users = new LinkedList<P2User>();

    public P2Backend(PermissionManager manager, Configuration config) {
        super(manager, config);

        this.configDir = new File(config.getString("permissions.backends.yeti.directory", "plugins/Permissions/"));

        this.loadPermissions(this.configDir);
    }

    @Override
    public PermissionGroup getDefaultGroup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionGroup getGroup(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionGroup[] getGroups() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionUser getUser(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionUser[] getUsers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reload() {
        this.loadPermissions(configDir);
    }

    protected final void loadPermissions(File dir) {
        if (!dir.exists()) {
            throw new RuntimeException("Specified directory aren't exist. Check \"permissions.backends.yeti.directory.param\"");
        }

        this.worldPermissions = new HashMap<String, Configuration>();

        File[] configFiles = dir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".yml");
            }
        });

        String defaultWorldName = this.getDefaultWorldName();

        for (File configFile : configFiles) {
            String worldName = configFile.getName().replace(".yml", "");
            this.worldPermissions.put(worldName, new Configuration(configFile));


            // Yeah that is lil bit redundant
            // you can just set this.defaultWorld = this.getDefaultWorldName(). But it can cause issues
            if (worldName.equalsIgnoreCase(defaultWorldName)) {
                this.defaultWorld = worldName;
            }

            if (this.defaultWorld.isEmpty()) {
                this.defaultWorld = worldName;
            }
        }
    }
    
    protected void parseWorld(Configuration world){
        
    }

    private String getDefaultWorldName() {
        return Bukkit.getServer().getWorlds().get(0).getName();
    }
}
