/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.suite.unit;

import org.jnario.lib.Assert;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.jnario.suite.unit.SuiteValueConverterSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

@Named("toString")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class SuiteValueConverterToStringSpec extends SuiteValueConverterSpec {
  @Test
  @Named("escapes dashes")
  @Order(1)
  public void _escapesDashes() throws Exception {
    String _string = this.subject.toString("##suite - with dash");
    Assert.assertTrue("\nExpected subject.toString(\"##suite - with dash\") => \"##suite \\\\- with dash\" but"
     + "\n     subject.toString(\"##suite - with dash\") is " + new org.hamcrest.StringDescription().appendValue(_string).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<String>operator_doubleArrow(_string, "##suite \\- with dash"));
    
  }
  
  @Test
  @Named("escapes hashs")
  @Order(2)
  public void _escapesHashs() throws Exception {
    String _string = this.subject.toString("##suite # with hash");
    Assert.assertTrue("\nExpected subject.toString(\"##suite # with hash\") => \"##suite \\\\# with hash\" but"
     + "\n     subject.toString(\"##suite # with hash\") is " + new org.hamcrest.StringDescription().appendValue(_string).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<String>operator_doubleArrow(_string, "##suite \\# with hash"));
    
  }
}
