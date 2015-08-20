package org.jnario.jnario.tests.unit.jnario;

import java.util.List;
import org.jnario.lib.ExampleTableRow;

@SuppressWarnings("all")
public class StringsTrailingWhitespaceSpecExamples extends ExampleTableRow {
  public StringsTrailingWhitespaceSpecExamples(final List<String> cellNames, final String string, final String result) {
    super(cellNames);
    this.string = string;
    this.result = result;
    
  }
  
  private String string;
  
  public String getString() {
    return this.string;
  }
  
  private String result;
  
  public String getResult() {
    return this.result;
  }
}
