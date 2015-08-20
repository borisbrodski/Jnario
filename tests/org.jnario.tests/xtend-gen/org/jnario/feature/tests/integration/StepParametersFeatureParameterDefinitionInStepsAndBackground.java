/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.feature.tests.integration;

import org.jnario.feature.tests.integration.StepParametersFeature;
import org.jnario.jnario.test.util.FeatureExecutor;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.StepArguments;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(FeatureRunner.class)
@Named("Scenario: Parameter definition in steps and Background")
@SuppressWarnings("all")
public class StepParametersFeatureParameterDefinitionInStepsAndBackground extends StepParametersFeature {
  CharSequence jnarioFile;
  
  @Test
  @Order(0)
  @Named("When I define parameters in a background")
  public void _whenIDefineParametersInABackground() {
    final StepArguments args = new StepArguments("package bootstrap \nFeature: Test feature\n\tBackground:\n\t\tString x\n\t\tGiven some values \"3\", \"4\"\n\t\t\tx = args.get(1)\n\tScenario: using fields in step definitions\n\t\tThen those values should be accessible\n\t\t\tx => \"4\"\n\t\t\t\n");
    String _first = JnarioIterableExtensions.<String>first(args);
    this.jnarioFile = _first;
  }
  
  @Test
  @Order(1)
  @Named("Then it should execute successfully")
  public void _thenItShouldExecuteSuccessfully() {
    FeatureExecutor.isSuccessful(this.jnarioFile);
  }
}
