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

package org.browsit.mobarenaquests;

import java.util.Map;

import me.pikamug.quests.enums.ObjectiveType;
import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.garbagemule.MobArena.events.ArenaKillEvent;

public class MobArenaKillObjective extends BukkitCustomObjective implements Listener {
	
	public MobArenaKillObjective() {
		setName("MobArena Kill Objective");
		setAuthor("PikaMug");
		setShowCount(true);
		addStringPrompt("MA Kill Obj", "Set a name for the objective", "Kill arena mob");
		addStringPrompt("MA Kill Name", "Enter arena mob names, separating each one by a comma", "ANY");
		setCountPrompt("Set the amount of arena mobs to kill");
		setDisplay("%MA Kill Obj% %MA Kill Name%: %count%");
	}
	
	@EventHandler
	public void onArenaKill(final ArenaKillEvent event) {
		final Player killer = event.getPlayer();
		if (killer == null) {
			return;
		}
		final Quester quester = MobArenaModule.getQuests().getQuester(killer.getUniqueId());
		if (quester == null) {
			return;
		}
		final String mobName = event.getVictim().getName();
		for (final Quest quest : quester.getCurrentQuests().keySet()) {
			final Map<String, Object> datamap = getDataForPlayer(killer.getUniqueId(), this, quest);
			if (datamap != null) {
				final String mobNames = (String)datamap.getOrDefault("MA Kill Name", "ANY");
				if (mobNames == null) {
					return;
				}
				final String[] spl = mobNames.split(",");
				for (final String str : spl) {
					if (str.equals("ANY") || mobName.equalsIgnoreCase(str)) {
						incrementObjective(killer.getUniqueId(), this, quest, 1);

						quester.dispatchMultiplayerEverything(quest, ObjectiveType.CUSTOM,
								(final Quester q, final Quest cq) -> {
									incrementObjective(q.getUUID(), this, quest, 1);
									return null;
								});
						break;
					}
				}
			}
		}
	}
}
