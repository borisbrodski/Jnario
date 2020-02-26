package gameoflife;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class CellLocation {
  private static final HashSet<CellLocation> NEIGHBOUR_OFFSETS = CollectionLiterals.<CellLocation>newHashSet(
    CellLocation.cell(1, 0), CellLocation.cell(1, 1), CellLocation.cell(0, 1), CellLocation.cell((-1), (-1)), CellLocation.cell((-1), 0), CellLocation.cell((-1), 1), CellLocation.cell(0, (-1)), CellLocation.cell(1, (-1)));
  
  public static CellLocation cell(final int x, final int y) {
    return new CellLocation(x, y);
  }
  
  private final int x;
  
  private final int y;
  
  public Set<CellLocation> neighbours() {
    final Function1<CellLocation, CellLocation> _function = new Function1<CellLocation, CellLocation>() {
      @Override
      public CellLocation apply(final CellLocation it) {
        return CellLocation.this.plus(it);
      }
    };
    return IterableExtensions.<CellLocation>toSet(IterableExtensions.<CellLocation, CellLocation>map(CellLocation.NEIGHBOUR_OFFSETS, _function));
  }
  
  public CellLocation plus(final CellLocation other) {
    return CellLocation.cell((this.x + other.x), (this.y + other.y));
  }
  
  public CellLocation(final int x, final int y) {
    super();
    this.x = x;
    this.y = y;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.x;
    return prime * result + this.y;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CellLocation other = (CellLocation) obj;
    if (other.x != this.x)
      return false;
    if (other.y != this.y)
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("x", this.x);
    b.add("y", this.y);
    return b.toString();
  }
  
  @Pure
  public int getX() {
    return this.x;
  }
  
  @Pure
  public int getY() {
    return this.y;
  }
}
