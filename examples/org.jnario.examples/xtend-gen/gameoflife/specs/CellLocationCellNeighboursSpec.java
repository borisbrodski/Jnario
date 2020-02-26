package gameoflife.specs;

import gameoflife.CellLocation;
import gameoflife.specs.CellLocationSpec;
import java.util.Set;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioCollectionLiterals;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

@Named("cell neighbours")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class CellLocationCellNeighboursSpec extends CellLocationSpec {
  @Test
  @Named("are adjacent cells")
  @Order(1)
  public void _areAdjacentCells() throws Exception {
    final CellLocation cell = CellLocation.cell(5, 5);
    final Set<CellLocation> expectedNeighbours = JnarioCollectionLiterals.<CellLocation>set(
      CellLocation.cell(4, 6), CellLocation.cell(5, 6), CellLocation.cell(6, 6), 
      CellLocation.cell(4, 5), CellLocation.cell(6, 5), 
      CellLocation.cell(4, 4), CellLocation.cell(5, 4), CellLocation.cell(6, 4));
    Set<CellLocation> _neighbours = cell.neighbours();
    Assert.assertTrue("\nExpected cell.neighbours => expectedNeighbours but"
     + "\n     cell.neighbours is " + new org.hamcrest.StringDescription().appendValue(_neighbours).toString()
     + "\n     cell is " + new org.hamcrest.StringDescription().appendValue(cell).toString()
     + "\n     expectedNeighbours is " + new org.hamcrest.StringDescription().appendValue(expectedNeighbours).toString() + "\n", Should.operator_doubleArrow(_neighbours, expectedNeighbours));
    
  }
}
