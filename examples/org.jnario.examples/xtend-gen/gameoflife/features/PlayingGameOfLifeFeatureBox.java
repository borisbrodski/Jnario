package gameoflife.features;

import gameoflife.Evolution;
import gameoflife.World;
import gameoflife.features.PlayingGameOfLifeFeature;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.lib.StepArguments;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(FeatureRunner.class)
@Named("Scenario: Box")
@SuppressWarnings("all")
public class PlayingGameOfLifeFeatureBox extends PlayingGameOfLifeFeature {
  World world;
  
  @Test
  @Order(0)
  @Named("Given a world")
  public void _givenAWorld() {
    final StepArguments args = new StepArguments("------\n--XX--\n--XX--\n------ \n");
    this.world = World.parseWorld(JnarioIterableExtensions.<String>first(args));
  }
  
  @Test
  @Order(1)
  @Named("Then the world evolves into")
  public void _thenTheWorldEvolvesInto() {
    final StepArguments args = new StepArguments("------\n--XX--\n--XX--\n------\n");
    this.world = Evolution.gameOfLife().evolve(this.world);
    World _parseWorld = World.parseWorld(JnarioIterableExtensions.<String>first(args));
    Assert.assertTrue("\nExpected world => parseWorld(args.first) but"
     + "\n     world is " + new org.hamcrest.StringDescription().appendValue(this.world).toString()
     + "\n     parseWorld(args.first) is " + new org.hamcrest.StringDescription().appendValue(_parseWorld).toString()
     + "\n     args.first is " + new org.hamcrest.StringDescription().appendValue(JnarioIterableExtensions.<String>first(args)).toString()
     + "\n     args is " + new org.hamcrest.StringDescription().appendValue(args).toString() + "\n", Should.operator_doubleArrow(
      this.world, _parseWorld));
    
  }
}
