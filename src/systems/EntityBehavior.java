package systems;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.google.common.annotations.VisibleForTesting;

import models.entities.Entity;
import models.entities.EntityStats;
import models.genes.*;
import models.world.Food;
import models.world.IConsumable;
import models.world.Position;
import models.world.World;
import util.HelperFunctions;
import util.Pair;

public class EntityBehavior {
	private Entity entity;
	private Entity targetMate;
	private int ticksToWaitBetweenActions;

	@VisibleForTesting
	public int ticksSincePerformedLastAction;
	@VisibleForTesting
	public World world;
	@VisibleForTesting
	public Position previousPosition;

	public EntityBehavior(World world, Entity entity) {
		this.world = world;
		this.entity = entity;
		this.previousPosition = entity.getPosition();
		this.ticksSincePerformedLastAction = 0;
		this.ticksToWaitBetweenActions = 0;
	}

	public EntityBehavior(World world, Entity entity, int ticksToWaitBetweenActions) {
		this(world, entity);
		this.ticksSincePerformedLastAction = ticksToWaitBetweenActions;
		this.ticksToWaitBetweenActions = ticksToWaitBetweenActions;
	}

	/**
	 * Decides which action to perform and does so.
	 * 
	 * @return The amount of energy used by performing the action.
	 */
	public double doEntityAction() {
		if (ticksSincePerformedLastAction < ticksToWaitBetweenActions) {
			ticksSincePerformedLastAction++;
			return entity.energySpentBeingIdle();
		}

		Position originalPosition = entity.getPosition();

		switch (entity.getCurrentNeed()) {
		case Food:
			searchForFood();
			break;
		case Water:
			searchForWater();
			break;
		case Reproduce:
			searchForMate();
			break;
		default:
			wander();
			break;
		}

		return getTotalEnergyUsedDuringLastAction(originalPosition);
	}

	private double getTotalEnergyUsedDuringLastAction(Position originalPosition) {
		double energyUsed = 0;

		Position newPosition = entity.getPosition();
		Gene speedGene = entity.getGene(SpeedGene.name);
		double distTraveled = originalPosition.distanceTo(newPosition);
		energyUsed += distTraveled * speedGene.getCostPerTick();

		for (Gene gene : entity.getAllGenes()) {
			if (gene.getName() == SpeedGene.name)
				continue;
			energyUsed += gene.getCostPerTick();
		}

		return energyUsed;
	}

	private void searchForFood() {
		List<Pair<Position, IConsumable>> food = world.getFoodInRadius(entity.getPosition(),
				entity.getGene(AwarenessGene.name).getValue(), List.of(entity));

		if (!food.isEmpty()) {
			Position target;
			IConsumable consumable;
			int index = 0;
			do {
				target = food.get(index).first;
				consumable = food.get(index).second;
				index++;
			} while (consumable.getConsumableType() != entity.getPreferredFoodType() && index < food.size());
			
			boolean targetIsDesired = consumable.getConsumableType() == entity.getPreferredFoodType();

			if (targetIsDesired && isCloseEnoughToTarget(target)) {
				entity.consume(consumable);
				world.removeConsumable(consumable);
				ticksSincePerformedLastAction = 0;
			} else if (targetIsDesired) {
				entity.setPosition(getNewEntityPositionTowardTarget(target));
			} else {
				wander();
				return;
			}

			previousPosition = entity.getPosition();
		} else {
			wander();
		}
	}

	private void searchForWater() {
		wander(); // temporary
	}

	private void searchForMate() {
		if (targetMate == null) {
			List<Entity> closeEntities = world.getEntitiesInRadius(entity.getPosition(),
					entity.getGene(AwarenessGene.name).getValue(), List.of(entity)); // exclude self from search
			
			Comparator<Entity> comparator = Comparator.comparingDouble(e -> e.getGene(DesirabilityGene.name).getValue());
			closeEntities.sort(comparator.reversed());
			
			// TODO might be fun to have some sort of "snobbiness" factor where entities won't consider mates beneath certain Desirability value
			for (Entity e : closeEntities) {
				if (entity.canReproduceWith(e)) {
					targetMate = e;
					break;
				}
			}
		}

		if (targetMate != null) {
			Position target = targetMate.getPosition();

			if (isCloseEnoughToTarget(target)) {
				world.addEntity(entity.reproduce(targetMate));
				targetMate = null;
				ticksSincePerformedLastAction = 0;
			} else {
				entity.setPosition(getNewEntityPositionTowardTarget(target));
			}

			previousPosition = entity.getPosition();
		} else {
			wander();
		}
	}

	@VisibleForTesting
	public boolean isCloseEnoughToTarget(Position target) {
		// If entity is closer to the target than how far they would move
		// based on their speed, they can eat the food
		Position currentPosition = entity.getPosition();
		Position wouldMoveTo = getNewEntityPositionTowardTarget(target);
		double distToTarget = currentPosition.distanceTo(target);
		double distToMove = currentPosition.distanceTo(wouldMoveTo);

		return distToTarget <= distToMove;
	}

	@VisibleForTesting
	public Position getNewEntityPositionTowardTarget(Position target) {
		Position origin = entity.getPosition();
		double speed = entity.getGene(SpeedGene.name).getValue();

		double dx = target.x - origin.x;
		double dy = target.y - origin.y;

		double angle = Math.atan2(dy, dx);

		return getNewEntityPositionFromAngle(angle, speed);
	}

	@VisibleForTesting
	public Position getNewEntityPositionFromAngle(double angle, double speed) {
		Position origin = entity.getPosition();

		double normalizedY = Math.sin(angle);
		double normalizedX = Math.cos(angle);

		return origin.offsetClamp0(normalizedX * speed, normalizedY * speed, world.getWorldWidth(),
				world.getWorldHeight());
	}

	@VisibleForTesting
	public void wander() {
		double wanderSpeed = entity.getGene(SpeedGene.name).getValue() * .75;
		Position currentPosition = entity.getPosition();
		double angle = Double.NaN;
		Random rand = new Random();

		if (currentPosition.equals(previousPosition)) {
			angle = rand.nextDouble() * Math.PI * 2;
		} else if (currentPosition.y == previousPosition.y) {
			if (currentPosition.y == world.getWorldHeight()) {
				// bottom of world
				angle = HelperFunctions.randomRange(rand, Math.PI, Math.PI * 2);
			} else if (currentPosition.y == 0) {
				// top of world
				angle = HelperFunctions.randomRange(rand, 0, Math.PI);
			}
		} else if (currentPosition.x == previousPosition.x) {
			if (currentPosition.x == 0) {
				// left side of world
				angle = HelperFunctions.randomRange(rand, -Math.PI / 2, Math.PI / 2);
			} else if (currentPosition.x == world.getWorldWidth()) {
				// right side of world
				angle = HelperFunctions.randomRange(rand, Math.PI / 2, Math.PI * 3 / 4);
			}
		}

		if (Double.isNaN(angle)) {
			double dx = currentPosition.x - previousPosition.x;
			double dy = currentPosition.y - previousPosition.y;
			angle = Math.atan2(dy, dx);
		}

		Position newPosition = getNewEntityPositionFromAngle(angle, wanderSpeed);
		previousPosition = currentPosition;
		entity.setPosition(newPosition);
	}
}
