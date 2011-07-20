package ru.tehkode.permissions.compat;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;

/**
 *
 * @author code
 */
public class PermissionHandler extends com.nijiko.permissions.PermissionHandler {

    protected PermissionManager permissionManager;

    public PermissionHandler(PermissionManager permissionManager) {
        if(permissionManager == null){
            throw new RuntimeException("PermissionManager not found!");
        }
        
        
        this.permissionManager = permissionManager;
    }

    @Override
    public void reload() {
        this.permissionManager.reset();
    }

    /**
     * Simple alias for permission method.
     * Easier to understand / recognize what it does and is checking for.
     *
     * @param player
     * @param permission
     * @return boolean
     */
    @Override
    public boolean has(Player player, String permission) {
        return this.permission(player, permission);
    }

    @Override
    public boolean has(String worldName, String playerName, String permission) {
        return this.permission(worldName, playerName, permission);
    }

    /**
     * Checks to see if a player has permission to a specific tree node.
     * <br /><br />
     * Example usage:
     * <blockquote><pre>
     * boolean canReload = Plugin.Permissions.Security.permission(player, "permission.reload");
     * if(canReload) {
     *	System.out.println("The user can reload!");
     * } else {
     *	System.out.println("The user has no such permission!");
     * }
     * </pre></blockquote>
     *
     * @param player
     * @param permission
     * @return boolean
     */
    @Override
    public boolean permission(Player player, String permission) {
        return this.permission(player.getWorld().getName(), player.getName(), permission);
    }
    
    public boolean permission(String worldName, Player player, String permission){
        return this.permissionManager.has(player, permission, worldName);
    }

    @Override
    public boolean permission(String worldName, String playerName, String permission) {
        return this.permissionManager.has(playerName, permission, worldName);
    }

    /**
     * Grabs group name.
     * <br /><br />
     * Namespace: groups.name
     *
     * @param userName
     * @return String
     */
    @Override
    public String getGroup(String world, String userName) {
        return this.getGroups(world, userName)[0];
    }

    /**
     * Grabs users groups.
     * <br /><br />
     *
     * @param userName
     * @return Array
     */
    @Override
    public String[] getGroups(String world, String userName) {
        return this.permissionManager.getUser(userName).getGroupsNames();
    }

    /**
     * Checks to see if the player is in the requested group.
     *
     * @param world
     * @param userName - Player
     * @param groupName - Group to be checked upon.
     * @return boolean
     */
    @Override
    public boolean inGroup(String world, String userName, String groupName) {
        return this.checkInGroup(groupName, permissionManager.getUser(userName).getGroups(), true);
    }

    @Override
    public boolean inGroup(String name, String group) {
        PermissionUser user = this.permissionManager.getUser(name);
        if(user == null){
            return false;
        }
        
        return user.inGroup(group);
    }    

    protected boolean checkInGroup(String groupName, PermissionGroup[] groupArray, boolean recursive) {
        for (PermissionGroup group : groupArray) {
            if (group.getName().toLowerCase().equals(groupName.toLowerCase())) {
                return true;
            }

            if (recursive && checkInGroup(groupName, group.getParentGroups(), recursive)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks to see if a player is in a single group.
     * This does not check inheritance.
     *
     * @param world
     * @param userName - player name
     * @param groupName - Group to be checked
     * @return boolean
     */
    @Override
    public boolean inSingleGroup(String world, String userName, String groupName) {
        return this.checkInGroup(groupName, permissionManager.getUser(userName).getGroups(), false);
    }

    /**
     * Grabs group prefix, line that comes before the group.
     * <br /><br />
     * Namespace: groups.name.info.prefix
     *
     * @param world
     * @param group
     * @return String
     */
    @Override
    public String getGroupPrefix(String world, String groupName) {
        return this.permissionManager.getGroup(groupName).getPrefix();
    }

    /**
     * Grabs group suffix, line that comes after the group.
     * <br /><br />
     * Namespace: groups.name.info.suffix
     *
     * @param world
     * @param group
     * @return String
     */
    @Override
    public String getGroupSuffix(String world, String groupName) {
        return this.permissionManager.getGroup(groupName).getSuffix();
    }

    /**
     * Checks to see if the group has build permission.
     * <br /><br />
     * Namespace: groups.name.info.build
     *
     * @param world
     * @param group
     * @return String
     */
    @Override
    public boolean canGroupBuild(String world, String groupName) {
        return true;
    }

    /**
     * Get permission nodes from a group that contain values.
     * <br /><br />
     * Grab Group Permission String values.
     *
     * @param world
     * @param group
     * @param permission
     * @return String. If no string found return "".
     */
    @Override
    public String getGroupPermissionString(String world, String groupName, String permission) {
        return this.permissionManager.getGroup(groupName).getOption(permission, world);
    }

    /**
     * Get permission nodes from a group that contain values.
     * <br /><br />
     * Grab Group Permission Integer values.
     *
     * @param world
     * @param group
     * @param permission
     * @return Integer. No integer found return -1.
     */
    @Override
    public int getGroupPermissionInteger(String world, String groupName, String permission) {
        return this.permissionManager.getGroup(groupName).getOptionInteger(permission, world, -1);
    }

    /**
     * Get permission nodes from a group that contain values.
     * <br /><br />
     * Grab Group Permission String values.
     *
     * @param group
     * @param permission
     * @return Boolean. No boolean found return false.
     */
    @Override
    public boolean getGroupPermissionBoolean(String world, String groupName, String permission) {
        return this.permissionManager.getGroup(groupName).getOptionBoolean(permission, world, false);
    }

    /**
     * Get permission nodes from a group that contain values.
     * <br /><br />
     * Grab Group Permission Double values.
     *
     * @param world
     * @param groupName
     * @param permission
     * @return Double. No value found return -1.0
     */
    @Override
    public double getGroupPermissionDouble(String world, String groupName, String permission) {
        String value = this.permissionManager.getGroup(groupName).getOption(permission, world);
        return value.isEmpty() ? -1.0d :  Double.parseDouble(value);
    }

    /**
     * Get permission nodes from a specific user that contain values.
     * <br /><br />
     * Grab User Permission String values.
     *
     * @param world
     * @param userName
     * @param permission
     * @return String. If no string found return "".
     */
    @Override
    public String getUserPermissionString(String world, String userName, String permission) {

        // fixture for prefix/suffix compatiblity issues with ichat
        if(permission.equals("prefix")){
            return this.permissionManager.getUser(userName).getPrefix();
        }
        if(permission.equals("suffix")){
            return this.permissionManager.getUser(userName).getSuffix();
        }

        return this.permissionManager.getUser(userName).getOption(permission, world);
    }

    /**
     * Get permission nodes from a specific user that contain values.
     * <br /><br />
     * Grab User Permission Integer values.
     *
     * @param world
     * @param userName
     * @param permission
     * @return Integer. No integer found return -1.
     */
    @Override
    public int getUserPermissionInteger(String world, String userName, String permission) {
        return this.permissionManager.getUser(userName).getOptionInteger(permission, world, -1);
    }

    /**
     * Get permission nodes from a specific user that contain values.
     * <br /><br />
     * Grab User Permission Boolean values.
     *
     * @param world
     * @param userName
     * @param permission
     * @return Boolean. No boolean found return false.
     */
    @Override
    public boolean getUserPermissionBoolean(String world, String userName, String permission) {
        return this.permissionManager.getUser(userName).getOptionBoolean(permission, world, false);
    }

    /**
     * Get permission nodes from a specific user that contain values.
     * <br /><br />
     * Grab User Permission Double values.
     *
     * @param world
     * @param userName
     * @param permission
     * @return Double. No value found return -1.0
     */
    @Override
    public double getUserPermissionDouble(String world, String userName, String permission) {
        return this.permissionManager.getUser(userName).getOptionDouble(permission, world, -1.0);
    }

    /**
     * Get permission nodes from a user / group that contain values.
     * <br /><br />
     * Grab User Permission String values.
     *
     * @param world
     * @param userName
     * @param permission
     * @return String. If no string found return "".
     */
    @Override
    public String getPermissionString(String world, String userName, String permission) {
        return this.getUserPermissionString(world, userName, permission);
    }

    /**
     * Get permission nodes from a user / group that contain values.
     * <br /><br />
     * Grab User Permission Integer values.
     *
     * @param world
     * @param userName
     * @param permission
     * @return Integer. No integer found return -1.
     */
    @Override
    public int getPermissionInteger(String world, String userName, String permission) {
        return this.getUserPermissionInteger(world, userName, permission);
    }

    /**
     * Get permission nodes from a user / group that contain values.
     * <br /><br />
     * Grab User Permission Boolean values.
     *
     * @param world
     * @param group
     * @param permission
     * @return Boolean. No boolean found return false.
     */
    @Override
    public boolean getPermissionBoolean(String world, String userName, String permission) {
        return this.getUserPermissionBoolean(world, userName, permission);
    }

    /**
     * Get permission nodes from a user / group that contain values.
     * <br /><br />
     * Grab User Permission Double values.
     *
     * @param world
     * @param groupName
     * @param permission
     * @return Double. No value found return -1.0
     */
    @Override
    public double getPermissionDouble(String world, String userName, String permission) {
        return this.getUserPermissionDouble(world, userName, permission);
    }

    @Override
    public void addUserPermission(String world, String user, String node) {
        
    }

    @Override
    public void removeUserPermission(String world, String user, String node) {
        
    }


    /*
     * Here came unneccesary for implementation stuff
     */
    @Override
    public void addGroupInfo(String world, String group, String node, Object data) {
        throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
    }

    @Override
    public void removeGroupInfo(String world, String group, String node) {
        throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
    }

    @Override
    public void setDefaultWorld(String world) {
        throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
    }

    @Override
    public boolean loadWorld(String world) {
        throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
    }

    @Override
    public void forceLoadWorld(String world) {
        throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
    }

    @Override
    public boolean checkWorld(String world) {
        throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
    }

    @Override
    public void load(String world, Configuration config) {
        throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
    }

    @Override
    public boolean reload(String world) {
        throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
    }

    // Cache
    @Override
    public void setCache(String world, Map<String, Boolean> Cache) {
        //throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
        Logger.getLogger("Minecraft").warning("[PermissionsEx] setCache item are internal Permissions plugin stuff. Nag plugin author.");
    }

    @Override
    public void setCacheItem(String world, String player, String permission, boolean data) {
        //throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
        Logger.getLogger("Minecraft").warning("[PermissionsEx] setCacheItem item are internal Permissions plugin stuff. Nag plugin author.");
    }

    @Override
    public Map<String, Boolean> getCache(String world) {
        //throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
        Logger.getLogger("Minecraft").warning("[PermissionsEx] setCacheItem item are internal Permissions plugin stuff. Nag plugin author.");
        return new HashMap<String, Boolean>();
    }

    @Override
    public boolean getCacheItem(String world, String player, String permission) {
        //throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
        Logger.getLogger("Minecraft").warning("[PermissionsEx] getCacheItem item are internal Permissions plugin stuff. Nag plugin author.");
        return false;
    }

    @Override
    public void removeCachedItem(String world, String player, String permission) {
        //throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
        Logger.getLogger("Minecraft").warning("[PermissionsEx] removeCachedItem item are internal Permissions plugin stuff. Nag plugin author.");
    }

    @Override
    public void clearCache(String world) {
        //throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
        Logger.getLogger("Minecraft").warning("[PermissionsEx] clearCache item are internal Permissions plugin stuff. Nag plugin author.");
    }

    @Override
    public void clearAllCache() {
        //throw new UnsupportedOperationException("Unsupported (or deprecated) operation, sorry dude.");
        Logger.getLogger("Minecraft").warning("[PermissionsEx] clearAllCache item are internal Permissions plugin stuff. Nag plugin author.");
    }

    @Override
    public void save(String world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
