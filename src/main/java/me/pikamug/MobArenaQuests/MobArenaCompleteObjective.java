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