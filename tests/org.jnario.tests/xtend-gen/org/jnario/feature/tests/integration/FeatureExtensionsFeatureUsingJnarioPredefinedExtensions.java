/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.feature.tests.integration;

import org.jnario.feature.tests.integration.FeatureExtensionsFeature;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.lib.StepArguments;
import org.jnario.lib.StringConversions;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(FeatureRunner.class)
@Named("Scenario: Using jnario predefined extensions")
@SuppressWarnings("all")
public class FeatureExtensionsFeatureUsingJnarioPredefinedExtensions extends FeatureExtensionsFeature {
  String numberString;
  
  int number;
  
  @Test
  @Order(0)
  @Named("Given the number \\\"5\\\" as string")
  public void _givenTheNumber5AsString() {
    final StepArguments args = new StepArguments("5");
    String _first = JnarioIterableExtensions.<String>first(args);
    this.numberString = _first;
  }
  
  @Test
  @Order(1)
  @Named("When it is converted to int")
  public void _whenItIsConvertedToInt() {
    int _int = StringConversions.toInt(this.numberString);
    this.number = _int;
  }
  
  @Test
  @Order(2)
  @Named("Then it should be \\\"5\\\"")
  public void _thenItShouldBe5() {
    final StepArguments args = new StepArguments("5");
    String _first = JnarioIterableExtensions.<String>first(args);
    int _int = StringConversions.toInt(_first);
    Assert.assertTrue("\nExpected number => args.first.toInt but"
     + "\n     number is " + new org.hamcrest.StringDescription().appendValue(Integer.valueOf(this.number)).toString()
     + "\n     args.first.toInt is " + new org.hamcrest.StringDescription().appendValue(Integer.valueOf(_int)).toString()
     + "\n     args.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     args is " + new org.hamcrest.StringDescription().appendValue(args).toString() + "\n", Should.<Integer>operator_doubleArrow(Integer.valueOf(this.number), Integer.valueOf(_int)));
    
  }
}
