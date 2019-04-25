package me.pikamug.MobArenaQuests;

import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.garbagemule.MobArena.events.ArenaCompleteEvent;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class MobArenaCompleteObjective extends CustomObjective implements Listener {
	private static Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
	public MobArenaCompleteObjective() {
		setName("MobArena Complete Objective");
		setAuthor("PikaMug");
		setShowCount(true);
		addStringPrompt("MA Complete Obj", "Set a name for the objective", "Complete arena");
		addStringPrompt("MA Arena Names", "Enter arena names, separating each one by a comma", "ANY");
		setCountPrompt("Set the amount of arenas to finish");
		setDisplay("%MA Complete Obj% %MA Arena Names%: %count%");
	}
	
	@EventHandler
	public void onArenaComplete(ArenaCompleteEvent event) {
		Set<Player> survivors = event.getSurvivors();
		for (Player survivor : survivors) {
			Quester quester = quests.getQuester(survivor.getUniqueId());
			if (quester == null) {
				return;
			}
			String arenaName = event.getArena().arenaName();
			for (Quest q : quester.getCurrentQuests().keySet()) {
				Map<String, Object> datamap = getDataForPlayer(survivor, this, q);
				if (datamap != null) {
					String arenaNames = (String)datamap.getOrDefault("MA Arena Names", "ANY");
					if (arenaNames == null) {
						incrementObjective(survivor, this, 1, q);
					}
					String[] spl = arenaNames.split(",");
					for (String str : spl) {
						if (str.equals("ANY") || arenaName.equalsIgnoreCase(str)) {
							incrementObjective(survivor, this, 1, q);
							return;
						}
					}
					return;
				}
			}
		}
	}
}