package systems;

import models.entities.Entity;
import models.entities.EntityStats;
import models.genes.*;
import models.world.World;

public class EntityBehavior {
	private World world;

	public EntityBehavior(World world) {
		this.world = world;
	}

	public void doEntityAction(Entity entity) {
		EntityStats stats = entity.getStats();
		double hunger = stats.getHunger();
		double thirst = stats.getThirst();
		double repro = stats.getReproductiveUrge();

		if (thirst > hunger && thirst > repro) {
			// get water
		} else if (hunger > thirst && hunger > repro) {
			world.getFoodInRadius(world.getEntityPosition(entity), entity.getGene(AwarenessGene.name).getValue());
		}
	}
}
