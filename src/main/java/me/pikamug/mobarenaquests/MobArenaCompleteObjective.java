/*
 * Copyright (c) 2019 PikaMug and contributors. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package me.pikamug.mobarenaquests;

import java.util.Map;

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
	private final Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
	public MobArenaCompleteObjective() {
		setName("MobArena Complete Objective");
		setAuthor("PikaMug");
		setShowCount(true);
		addStringPrompt("MA Complete Obj", "Set a name for the objective", "Complete arena");
		addStringPrompt("MA Complete Arena", "Enter arena names, separating each one by a comma", "ANY");
		setCountPrompt("Set the amount of arenas to finish");
		setDisplay("%MA Complete Obj% %MA Complete Arena%: %count%");
	}
	
	@EventHandler
	public void onArenaComplete(final ArenaCompleteEvent event) {
		for (final Player survivor : event.getSurvivors()) {
			final Quester quester = MobArenaModule.getQuests().getQuester(survivor.getUniqueId());
			if (quester == null) {
				continue;
			}
			final String arenaName = event.getArena().arenaName();
			for (final Quest q : quester.getCurrentQuests().keySet()) {
				final Map<String, Object> datamap = getDataForPlayer(survivor, this, q);
				if (datamap != null) {
					final String arenaNames = (String)datamap.getOrDefault("MA Complete Arena", "ANY");
					if (arenaNames == null) {
						return;
					}
					final String[] spl = arenaNames.split(",");
					for (final String str : spl) {
						if (str.equals("ANY") || arenaName.equalsIgnoreCase(str)) {
							incrementObjective(survivor, this, 1, q);
							break;
						}
					}
				}
			}
		}
	}
}
