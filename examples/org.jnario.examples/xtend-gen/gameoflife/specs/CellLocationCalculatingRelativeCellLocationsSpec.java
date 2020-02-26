package gameoflife.specs;

import gameoflife.CellLocation;
import gameoflife.specs.CellLocationSpec;
import org.jnario.lib.Assert;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

@Named("Calculating relative cell locations")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class CellLocationCalculatingRelativeCellLocationsSpec extends CellLocationSpec {
  @Test
  @Named("cell[-1, 1].plus[cell[3,4]] => cell[2,5]")
  @Order(1)
  public void _cell11PlusCell34Cell25() throws Exception {
    CellLocation _plus = CellLocation.cell((-1), 1).plus(CellLocation.cell(3, 4));
    CellLocation _cell = CellLocation.cell(2, 5);
    boolean _doubleArrow = Should.operator_doubleArrow(_plus, _cell);
    Assert.assertTrue("\nExpected cell(-1, 1).plus(cell(3,4)) => cell(2,5) but"
     + "\n     cell(-1, 1).plus(cell(3,4)) is " + new org.hamcrest.StringDescription().appendValue(_plus).toString()
     + "\n     cell(-1, 1) is " + new org.hamcrest.StringDescription().appendValue(CellLocation.cell((-1), 1)).toString()
     + "\n     -1 is " + new org.hamcrest.StringDescription().appendValue((-1)).toString()
     + "\n     cell(3,4) is " + new org.hamcrest.StringDescription().appendValue(CellLocation.cell(3, 4)).toString()
     + "\n     cell(2,5) is " + new org.hamcrest.StringDescription().appendValue(_cell).toString() + "\n", _doubleArrow);
    
  }
}
