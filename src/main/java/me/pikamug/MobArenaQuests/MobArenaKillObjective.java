/*******************************************************************************************************
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************************************/

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
	private final Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
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
	public void onArenaKill(final ArenaKillEvent event) {
		final Player killer = event.getPlayer();
		if (killer == null) {
			return;
		}
		final Quester quester = quests.getQuester(killer.getUniqueId());
		if (quester == null) {
			return;
		}
		final String mobName = event.getVictim().getName();
		for (final Quest q : quester.getCurrentQuests().keySet()) {
			final Map<String, Object> datamap = getDataForPlayer(killer, this, q);
			if (datamap != null) {
				final String mobNames = (String)datamap.getOrDefault("MA Kill Names", "ANY");
				if (mobNames == null) {
					continue;
				}
				final String[] spl = mobNames.split(",");
				for (final String str : spl) {
					if (str.equals("ANY") || mobName.equalsIgnoreCase(str)) {
						incrementObjective(killer, this, 1, q);
						break;
					}
				}
			}
		}
	}
}
