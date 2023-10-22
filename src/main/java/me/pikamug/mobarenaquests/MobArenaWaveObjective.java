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

import me.pikamug.quests.module.BukkitCustomObjective;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.garbagemule.MobArena.events.NewWaveEvent;

public class MobArenaWaveObjective extends BukkitCustomObjective implements Listener {
	
	public MobArenaWaveObjective() {
		setName("MobArena Wave Objective");
		setAuthor("PikaMug");
		setShowCount(true);
		addStringPrompt("MA Survive Obj", "Set a name for the objective", "Survive waves");
		addStringPrompt("MA Survive Arena", "Enter arena names, separating each one by a comma", "ANY");
		setCountPrompt("Set the amount of waves to survive");
		setDisplay("%MA Survive Obj% %MA Survive Arena%: %count%");
	}
	
	@EventHandler
	public void onNewWave(final NewWaveEvent event) {
		for (final Player player : event.getArena().getPlayersInArena()) {
			final Quester quester = MobArenaModule.getQuests().getQuester(player.getUniqueId());
			if (quester == null) {
				continue;
			}
			final String arenaName = event.getArena().arenaName();
			for (final Quest q : quester.getCurrentQuests().keySet()) {
				final Map<String, Object> datamap = getDataForPlayer(player.getUniqueId(), this, q);
				if (datamap != null) {
					final String arenaNames = (String)datamap.getOrDefault("MA Survive Arena", "ANY");
					if (arenaNames == null) {
						return;
					}
					final String[] spl = arenaNames.split(",");
					for (final String str : spl) {
						if (str.equals("ANY") || arenaName.equalsIgnoreCase(str)) {
							incrementObjective(player.getUniqueId(), this, q, 1);
							break;
						}
					}
				}
			}
		}
	}
}
