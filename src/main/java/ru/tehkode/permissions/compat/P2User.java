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

import java.util.List;
import java.util.logging.Logger;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.ProxyPermissionUser;
import ru.tehkode.permissions.config.ConfigurationNode;

public class P2User extends ProxyPermissionUser {

    protected P2Entity entity;

    public P2User(String playerName, PermissionManager manager, P2Backend backend) {
        super(new P2Entity(playerName, manager, backend));

        this.entity = (P2Entity) this.backendEntity;
    }

    public void load(String world, ConfigurationNode node) {
        this.entity.load(world, node);

        this.prefix = this.entity.getPrefix();
        this.suffix = this.entity.getSuffix();
    }

    @Override
    protected String[] getGroupsNamesImpl() {

        // Permissions 3.x
        Object groups = this.entity.getDefaultWorldNode().getProperty("groups");
        if (groups instanceof List) {
            return ((List<String>) groups).toArray(new String[0]);
        }

        // Permissions 2.x
        groups = this.entity.getDefaultWorldNode().getString("group");
        if (groups instanceof String) {
            return new String[]{(String) groups};
        }

        return new String[0];
    }

    @Override
    public void setGroups(String[] pgs) {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat backend is read-only!");
    }
}
