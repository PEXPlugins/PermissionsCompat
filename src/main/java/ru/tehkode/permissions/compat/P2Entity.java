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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.util.config.ConfigurationNode;
import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.PermissionManager;

public class P2Entity extends PermissionEntity {

	protected P2Backend backend;
	protected Map<String, Map<String, String>> entityOptions = new HashMap<String, Map<String, String>>();
	protected Map<String, String[]> entityPermissions = new HashMap<String, String[]>();

	public P2Entity(String name, PermissionManager manager, P2Backend backend) {
		super(name, manager);

		this.backend = backend;
	}

	public void load(String world, ConfigurationNode node) {
		if(node == null){
			return;
		}
				
		// load permissions (i fucking hate it!)
		this.entityPermissions.put(world, node.getStringList("permissions", new ArrayList<String>()).toArray(new String[0]));
		
		// load options
		Object infoNodes = node.getProperty("info");
		if(infoNodes instanceof Map){
			this.entityOptions.put(world, this.collectInfoNodes(null, (Map)infoNodes, new HashMap<String, String>()));
		}
	}

	@Override
	public String[] getWorlds() {
		Set<String> worlds = new HashSet<String>();
		
		worlds.addAll(this.entityPermissions.keySet());
		worlds.addAll(this.entityOptions.keySet());
		worlds.remove(null);
		
		return worlds.toArray(new String[0]);
	}

	@Override
	public String getPrefix(String worldName) {
		return this.getOption("prefix", worldName);
	}

	@Override
	public String getSuffix(String worldName) {
		return this.getOption("suffix", worldName);
	}

	@Override
	public void setPrefix(String string, String string1) {
		Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
	}

	@Override
	public void setSuffix(String string, String string1) {
		Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
	}

	@Override
	public String[] getPermissions(String world) {		
		return this.entityPermissions.get(world);
	}

	@Override
	public Map<String, Map<String, String>> getAllOptions() {
		return this.entityOptions;
	}

	@Override
	public Map<String, String[]> getAllPermissions() {
		return this.entityPermissions;
	}

	@Override
	public Map<String, String> getOptions(String world) {
		Map<String, String> options = new HashMap<String, String>();
		
		if(this.entityOptions.containsKey(null)){
			options.putAll(this.entityOptions.get(null));
		}
		
		if(this.entityOptions.containsKey(world)){
			options.putAll(this.entityOptions.get(world));
		}
		
		return options;
	}

	@Override
	public String getOption(String permission, String world, String defaultValue) {
		String value = null;

		if (world != null && !world.isEmpty() && this.entityOptions.containsKey(world)) {
			value = this.entityOptions.get(world).get(permission);
			if(value != null){
				return value;
			}
		}

		if(this.entityOptions.containsKey(null)){ // have common options
			value = this.entityOptions.get(null).get(permission);
			if (value != null){
				return value;
			}
		}


		return defaultValue;
	}
	
	protected Map<String, String> collectInfoNodes(String key, Map<String, Object> item, Map<String, String> collection){
		for(Map.Entry<String, Object> entry : item.entrySet()){
			Object value = entry.getValue();
			String itemKey = key != null ? key + "." + entry.getKey() : entry.getKey();
			
			if(value instanceof String){
				collection.put(itemKey, (String)value);
			} else if (value instanceof Map) {
				collectInfoNodes(itemKey, (Map)value, collection);
			} else if (value != null) {
				collection.put(itemKey, value.toString());
			}
		}
		
		return collection;
	}

	@Override
	public void addPermission(String string, String string1) {
		Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
	}

	@Override
	public void remove() {
		Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
	}

	@Override
	public void removePermission(String string, String string1) {
		Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
	}

	@Override
	public void save() {
		Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
	}

	@Override
	public void setOption(String option, String value, String world) {
		Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
	}

	@Override
	public void setPermissions(String[] strings, String string) {
		Logger.getLogger("Minecraft").severe("[PermissionsCompat] P2Compat is read-only");
	}
}
