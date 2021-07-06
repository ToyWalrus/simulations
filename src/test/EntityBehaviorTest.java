package test;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.*;

import models.entities.*;
import models.genes.*;
import models.world.Food;
import models.world.IConsumable;
import models.world.Position;
import models.world.World;
import systems.EntityBehavior;
import util.HelperFunctions;
import util.Pair;

class EntityBehaviorTest {
	private double wanderSpeedModifier = .75;
	private EntityBehavior behavior;
	private Entity entity;

	@BeforeEach
	void beforeEach() {
		World world = spy(new World(100, 100));

		entity = new PopulationTestEntity(0, 0);
		entity.addGene(new SpeedGene(0, 5));
		entity.addGene(new AwarenessGene(0, 10));
		resetEntityPosition();

		behavior = spy(new EntityBehavior(world, entity));
	}

	private void resetEntityPosition() {
		entity.setPosition(new Position(50, 50));
	}

	@Test
	@DisplayName("doEntityAction() - Returns the idle amount of energy used if not acting")
	void testTicksToWait() {
		double expected = entity.energySpentBeingIdle();
		behavior = new EntityBehavior(null, entity, 1);
		behavior.ticksSincePerformedLastAction = 0;

		assertEquals(behavior.doEntityAction(), expected);
	}

	@Test
	@DisplayName("doEntityAction() - Entity does not look for food if hunger is below threshold")
	void testNotEnoughHunger() {
		entity.getStats().setHunger(0);
		assertEquals(entity.isHungry(), false);

		behavior.doEntityAction();

		verify(behavior.world, never()).getFoodInRadius(any(Position.class), any(Double.class));
	}

	@Test
	@DisplayName("doEntityAction() - Entity does look for food if hunger is at or above threshold")
	void testEnoughHunger() {
		entity.getStats().setHunger(entity.getStats().getHungerThreshold());
		assertEquals(entity.isHungry(), true);
		assertEquals(entity.getPosition(), new Position(50, 50));

		ArrayList<Pair<Position, IConsumable>> defaultFood = new ArrayList<Pair<Position, IConsumable>>();
		defaultFood.add(new Pair<Position, IConsumable>(entity.getPosition(), Food.createFullyGrown(0, 0)));
		when(behavior.world.getFoodInRadius(any(Position.class), any(Double.class))).thenReturn(defaultFood);

		behavior.doEntityAction();

		verify(behavior, never()).wander();
		verify(behavior.world, times(1)).getFoodInRadius(new Position(50, 50),
				entity.getGene(AwarenessGene.name).getValue());
	}

	@Test
	@DisplayName("doEntityAction() - Entity does look for food if hunger is at or above threshold but wanders when none is found")
	void testEnoughHungerAndWander() {
		entity.getStats().setHunger(entity.getStats().getHungerThreshold());
		assertEquals(entity.isHungry(), true);
		assertEquals(entity.getPosition(), new Position(50, 50));

		behavior.doEntityAction();

		verify(behavior, times(1)).wander();
		verify(behavior.world, times(1)).getFoodInRadius(new Position(50, 50),
				entity.getGene(AwarenessGene.name).getValue());
	}

	@Test
	@DisplayName("doEntityAction() - Returns the correct amount of energy used if wandering")
	void testWanderEnergy() {
		double speed = entity.getGene(SpeedGene.name).getValue();
		double speedCostPerTick = entity.getGene(SpeedGene.name).getCostPerTick();
		double unitLength = 1;

		Position oldPosition = entity.getPosition();

		double energyUsed = behavior.doEntityAction();

		verify(behavior, times(1)).wander();
		Position newPosition = entity.getPosition();

		double distanceTraveled = oldPosition.distanceTo(newPosition);
		double expectedDistance = unitLength * speed * wanderSpeedModifier;
		assertEquals(HelperFunctions.almost(distanceTraveled, expectedDistance), true, String
				.format("Expect distance traveled (%.4f) to be equal to %.4f", distanceTraveled, expectedDistance));

		double expectedEnergyUsed = expectedDistance * speedCostPerTick;
		assertEquals(HelperFunctions.almost(energyUsed, expectedEnergyUsed), true,
				String.format("Expect energy used (%.4f) to be equal to %.4f", energyUsed, expectedEnergyUsed));
	}

	@Test
	@DisplayName("getNewEntityPositionFromAngle() - Correctly calculates the new position for cardinal directions")
	void testGetPositionFromAngle() {
		double speed = 1;
		List<Position> expectedPositions = List.of(new Position(51, 50), new Position(50, 51), new Position(49, 50),
				new Position(50, 49));

		for (int i = 0; i < 4; ++i) {
			resetEntityPosition();

			double angle = Math.toRadians(i * 90);
			Position newPosition = behavior.getNewEntityPositionFromAngle(angle, speed);

			assertEquals(newPosition, expectedPositions.get(i));
		}
	}
	
	@Test
	@DisplayName("getNewEntityPositionTowardTarget() - Correctly calculates the new position towards the given target")
	void testGetPositionTowardTarget() {
		double speed = 1;
		entity.updateGene(SpeedGene.name, new SpeedGene(0, speed));
		
		List<Position> targets = List.of(new Position(55, 50), new Position(50, 55), new Position(45, 50), new Position(50, 45));
		List<Position> expectedPositions = List.of(new Position(51, 50), new Position(50, 51), new Position(49, 50),
				new Position(50, 49));
		
		for (int i = 0; i < targets.size(); ++i) {
			resetEntityPosition();

			Position newPosition = behavior.getNewEntityPositionTowardTarget(targets.get(i));

			assertEquals(newPosition, expectedPositions.get(i));
		}
	}
	
	@Test
	@DisplayName("wander() - Entity wanders in the same general direction over multiple calls")
	void testWanderInSameDirection() {
		double speed = 1;
		entity.updateGene(SpeedGene.name, new SpeedGene(0, speed));
		resetEntityPosition();
		
		behavior.previousPosition = new Position(51, 50); // going left
		
		for (int i = 0; i < 10; ++i) {
			behavior.wander();
			assertEquals(entity.getPosition(), behavior.previousPosition.offset(-speed * wanderSpeedModifier, 0));
		}
	}

}
