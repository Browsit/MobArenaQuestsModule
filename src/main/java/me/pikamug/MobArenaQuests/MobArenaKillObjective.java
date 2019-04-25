package me.pikamug.MobArenaQuests;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.garbagemule.MobArena.events.ArenaKillEvent;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class MobArenaKillObjective extends CustomObjective implements Listener {
	private static Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
	public MobArenaKillObjective() {
		setName("MobArena Kill Objective");
		setAuthor("PikaMug");
		setShowCount(true);
		addStringPrompt("MA Kill Obj", "Set a name for the objective", "Kill arena mob");
		addStringPrompt("MA Kill Names", "Enter arena mob names, separating each one by a comma", "ANY");
		setCountPrompt("Set the amount of arena mobs to kill");
		setDisplay("%MA Kill Obj% %MA Kill Names%: %count%");
	}
	
	@EventHandler
	public void onArenaKill(ArenaKillEvent event) {
		Player killer = event.getPlayer();
		if (killer == null) {
			return;
		}
		Quester quester = quests.getQuester(killer.getUniqueId());
		if (quester == null) {
			return;
		}
		String mobName = event.getVictim().getName();
		for (Quest q : quester.getCurrentQuests().keySet()) {
			Map<String, Object> datamap = getDataForPlayer(killer, this, q);
			if (datamap != null) {
				String mobNames = (String)datamap.getOrDefault("MA Kill Names", "ANY");
				if (mobNames == null) {
					return;
				}
				String[] spl = mobNames.split(",");
				for (String str : spl) {
					if (str.equals("ANY") || mobName.equalsIgnoreCase(str)) {
						incrementObjective(killer, this, 1, q);
						return;
					}
				}
			}
		}
	}
}
