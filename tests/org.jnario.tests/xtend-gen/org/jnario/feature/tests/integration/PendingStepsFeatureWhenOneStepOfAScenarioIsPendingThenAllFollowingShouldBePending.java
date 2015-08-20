/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.feature.tests.integration;

import org.jnario.feature.tests.integration.PendingStepsFeature;
import org.jnario.jnario.test.util.FeatureExecutor;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.StepArguments;
import org.jnario.lib.StringConversions;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(FeatureRunner.class)
@Named("Scenario: When one step of a scenario is pending then all following should be pending")
@SuppressWarnings("all")
public class PendingStepsFeatureWhenOneStepOfAScenarioIsPendingThenAllFollowingShouldBePending extends PendingStepsFeature {
  CharSequence jnarioFile;
  
  @Test
  @Order(0)
  @Named("Given a scenario")
  public void _givenAScenario() {
    final StepArguments args = new StepArguments("Feature: With pending steps\n\tScenario: Scenario with pending steps\n\t\tGiven is implemented\n\t\t\tvar x = 3\n\t\tWhen is not implemented\n\t\tThen is also implemented\n\t\t\t3 => 3\n");
    String _first = JnarioIterableExtensions.<String>first(args);
    this.jnarioFile = _first;
  }
  
  @Test
  @Order(1)
  @Named("Then it should execute successfully")
  public void _thenItShouldExecuteSuccessfully() {
    FeatureExecutor.isSuccessful(this.jnarioFile);
  }
  
  @Test
  @Order(2)
  @Named("And the expected number of ignored steps is \\\"2\\\"")
  public void _andTheExpectedNumberOfIgnoredStepsIs2() {
    final StepArguments args = new StepArguments("2");
    String _first = JnarioIterableExtensions.<String>first(args);
    int _int = StringConversions.toInt(_first);
    FeatureExecutor.ignoreCountIs(this.jnarioFile, _int);
  }
}
