package me.NoChance.PvPManager.Dependencies.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.NoChance.PvPManager.Dependencies.PvPlugin;

public class WorldGuard implements PvPlugin {

	private final WorldGuardPlugin inst;
	private final RegionQuery regionQuery;
	private final StateFlag pvp = new StateFlag("pvp", false);

	public WorldGuard() {
		inst = WorldGuardPlugin.inst();
		regionQuery = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
	}

	// This method has no use in free version, use canBeAttacked() instead
	// Exclusions for vulnerable anti border hopping
	@Override
	public boolean canAttack(final Player attacker, final Player defender) {
		return true;
	}

	@Override
	public boolean canBeAttacked(final Player player, final Location l) {
		// State has to be != DENY because you can pvp on ALLOW and on no state
		return getWGPvPState(l) != State.DENY;
	}

	public boolean hasAllowPvPFlag(final Player defender) {
		return getWGPvPState(defender.getLocation()) == State.ALLOW;
	}

	public State getWGPvPState(final Location l) {
		return regionQuery.queryState(BukkitAdapter.adapt(l), null, pvp);
	}

	@Override
	public JavaPlugin getMainClass() {
		return inst;
	}

}
