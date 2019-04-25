package me.pikamug.MobArenaQuests;

import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.garbagemule.MobArena.events.NewWaveEvent;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class MobArenaWaveObjective extends CustomObjective implements Listener {
	private static Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
	public MobArenaWaveObjective() {
		setName("MobArena Wave Objective");
		setAuthor("PikaMug");
		setShowCount(true);
		addStringPrompt("MA Survive Obj", "Set a name for the objective", "Survive waves");
		addStringPrompt("MA Arena Names", "Enter arena names, separating each one by a comma", "ANY");
		setCountPrompt("Set the amount of waves to survive");
		setDisplay("%MA Survive Obj% %MA Arena Names%: %count%");
	}
	
	@EventHandler
	public void onNewWave(NewWaveEvent event) {
		Set<Player> players = event.getArena().getPlayersInArena();
		for (Player player : players) {
			Quester quester = quests.getQuester(player.getUniqueId());
			if (quester == null) {
				return;
			}
			String arenaName = event.getArena().arenaName();
			for (Quest q : quester.getCurrentQuests().keySet()) {
				Map<String, Object> datamap = getDataForPlayer(player, this, q);
				if (datamap != null) {
					String arenaNames = (String)datamap.getOrDefault("MA Arena Names", "ANY");
					if (arenaNames == null) {
						incrementObjective(player, this, 1, q);
					}
					String[] spl = arenaNames.split(",");
					for (String str : spl) {
						if (str.equals("ANY") || arenaName.equalsIgnoreCase(str)) {
							incrementObjective(player, this, 1, q);
							return;
						}
					}
					return;
				}
			}
		}
	}
}
