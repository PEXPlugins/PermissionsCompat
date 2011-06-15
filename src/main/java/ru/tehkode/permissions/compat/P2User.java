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

import java.util.Map;
import java.util.logging.Logger;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.config.ConfigurationNode;

public class P2User extends PermissionUser {

    protected P2Entity entity;

    public P2User(String playerName, PermissionManager manager, P2Backend backend) {
        super(playerName, manager);

        this.entity = new P2Entity(prefix, manager, backend);
    }

    public void load(String world, ConfigurationNode node) {
        this.entity.load(world, node);

        this.prefix = this.entity.getPrefix();
        this.suffix = this.entity.getSuffix();
    }

    @Override
    protected String[] getGroupsNamesImpl() {
        String groupName = this.entity.getDefaultWorldNode().getString("group", null);

        if (groupName != null) {
            return new String[]{groupName};
        }

        return new String[0];
    }

    @Override
    public String[] getOwnPermissions(String world) {
        return this.entity.getPermissions(world);
    }

    @Override
    public void setGroups(String[] pgs) {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat backend is read-only!");
    }

    @Override
    public void setPermissions(String[] strings, String string) {
        entity.setPermissions(strings, string);
    }

    @Override
    public void setOption(String string, String string1, String string2) {
        entity.setOption(string, string1, string2);
    }

    @Override
    public void save() {
        entity.save();
    }

    @Override
    public void removePermission(String string, String string1) {
        entity.removePermission(string, string1);
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public String getSuffix() {
        return entity.getSuffix();
    }

    @Override
    public String getPrefix() {
        return entity.getPrefix();
    }

    public String[] getPermissions(String world, boolean inheritance) {
        return entity.getPermissions(world, inheritance);
    }

    public Map<String, String> getOptions(String world, boolean inheritance) {
        return entity.getOptions(world, inheritance);
    }

    @Override
    public Map<String, String> getOptions(String world) {
        return entity.getOptions(world);
    }

    @Override
    public String getOption(String permission, String world) {
        return entity.getOption(permission, world);
    }

    @Override
    public Map<String, String[]> getAllPermissions() {
        return entity.getAllPermissions();
    }

    @Override
    public Map<String, Map<String, String>> getAllOptions() {
        return entity.getAllOptions();
    }

    @Override
    public void addPermission(String string, String string1) {
        entity.addPermission(string, string1);
    }
}
