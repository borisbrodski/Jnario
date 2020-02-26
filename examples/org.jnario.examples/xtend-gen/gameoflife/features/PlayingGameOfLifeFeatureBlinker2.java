package gameoflife.features;

import gameoflife.Evolution;
import gameoflife.World;
import gameoflife.features.PlayingGameOfLifeFeature;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.lib.StepArguments;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(FeatureRunner.class)
@Named("Scenario: Blinker 2")
@SuppressWarnings("all")
public class PlayingGameOfLifeFeatureBlinker2 extends PlayingGameOfLifeFeature {
  World world;
  
  @Test
  @Order(0)
  @Named("Given a world")
  public void _givenAWorld() {
    final StepArguments args = new StepArguments("-----\n-----\n-XXX-\n-----\n-----\n");
    this.world = World.parseWorld(JnarioIterableExtensions.<String>first(args));
  }
  
  @Test
  @Order(1)
  @Named("Then the world evolves into")
  public void _thenTheWorldEvolvesInto() {
    final StepArguments args = new StepArguments("-----\n--X--\n--X--\n--X--\n-----\n");
    this.world = Evolution.gameOfLife().evolve(this.world);
    World _parseWorld = World.parseWorld(JnarioIterableExtensions.<String>first(args));
    Should.operator_doubleArrow(this.world, _parseWorld);
  }
}
