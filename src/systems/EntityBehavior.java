package systems;

import java.util.List;
import java.util.Random;

import com.google.common.annotations.VisibleForTesting;

import models.entities.Entity;
import models.entities.EntityStats;
import models.genes.*;
import models.world.Food;
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
		List<Pair<Position, Food>> food = world.getFoodInRadius(entity.getPosition(),
				entity.getGene(AwarenessGene.name).getValue());

		if (!food.isEmpty()) {
			Position target = food.get(0).first;
			Food foodToEat = food.get(0).second;

			if (isCloseEnoughToTarget(target)) {
				entity.eatFood(foodToEat);
				world.removeFood(foodToEat);
				ticksSincePerformedLastAction = 0;
			} else {
				entity.setPosition(getNewEntityPositionTowardTarget(target));
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

			if (!closeEntities.isEmpty()) {
				// TODO do some sort of desirability check/availability check
				targetMate = closeEntities.get(0);
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
		double angle = 0;

		if (currentPosition.equals(previousPosition)) {
			angle = new Random().nextDouble() * Math.PI * 2;
		} else {
			double dx = currentPosition.x - previousPosition.x;
			double dy = currentPosition.y - previousPosition.y;
			angle = Math.atan2(dy, dx);
		}

		Position newPosition = getNewEntityPositionFromAngle(angle, wanderSpeed);
		previousPosition = currentPosition;
		entity.setPosition(newPosition);
	}
}
