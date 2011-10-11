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
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.util.config.ConfigurationNode;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.ProxyPermissionGroup;


public class P2Group extends ProxyPermissionGroup {

    protected P2Entity entity;
	protected P2Backend backend;
	
	protected Map<String, String[]> entityInheritance = new HashMap<String, String[]>();

    public P2Group(String groupName, PermissionManager manager, P2Backend backend) {
        super(new P2Entity(groupName, manager, backend));

        this.entity = (P2Entity) this.backendEntity;
		this.backend = backend;
    }

    public void load(String world, ConfigurationNode node) {
        this.entity.load(world, node);
				
		this.entityInheritance.put(world, node.getStringList("inheritance", new LinkedList<String>()).toArray(new String[0]));
    }

    @Override
    protected String[] getParentGroupsNamesImpl(String worldName) {
		if(this.entityInheritance.containsKey(worldName)){
			return this.entityInheritance.get(worldName);
		}
		
        return new String[0];
    }

    @Override
    protected void removeGroup() {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
    }

    @Override
    public void setParentGroups(String[] pgs, String worldName) {
        Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
    }
}
