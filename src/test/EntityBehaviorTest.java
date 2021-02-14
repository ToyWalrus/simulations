package test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.*;

import models.entities.*;
import models.genes.*;
import models.world.Position;
import models.world.World;
import systems.EntityBehavior;
import util.HelperFunctions;

class EntityBehaviorTest {
	private EntityBehavior behavior;
	private Entity entity;

	@BeforeEach
	private void beforeEach() {
		World world = spy(new World(100, 100));

		entity = new SimpleEntity(0, 0);
		entity.setPosition(new Position(50, 50));
		entity.addGene(new SpeedGene(0, 5, .01));
		entity.addGene(new AwarenessGene(0, 10, 0));

		behavior = spy(new EntityBehavior(world, entity));
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

		behavior.doEntityAction();
		verify(behavior.world, times(1)).getFoodInRadius(new Position(50, 50),
				entity.getGene(AwarenessGene.name).getValue());
	}

	@Test
	@DisplayName("doEntityAction() - Returns the correct amount of energy used if wandering")
	void testWanderEnergy() {
		double wanderSpeedModifier = .75;
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

}
