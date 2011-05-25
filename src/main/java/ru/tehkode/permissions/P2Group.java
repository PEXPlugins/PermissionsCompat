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

import java.util.Map;
import ru.tehkode.permissions.config.ConfigurationNode;

public class P2Group extends PermissionGroup {

    protected P2Backend backend;
    protected ConfigurationNode node;
    
    public P2Group(String groupName, PermissionManager manager, P2Backend backend) {
        super(groupName, manager);
        
        this.backend = backend;
    }
    
    @Override
    protected String[] getParentGroupsNamesImpl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void removeGroup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setParentGroups(PermissionGroup[] pgs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPermission(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getOption(String string, String string1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, String> getOptions(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getOwnPermissions(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePermission(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOption(String string, String string1, String string2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPermissions(String[] strings, String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
    
}
