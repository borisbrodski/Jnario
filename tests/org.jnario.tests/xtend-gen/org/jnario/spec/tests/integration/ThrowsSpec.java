/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.spec.tests.integration;

import com.google.inject.Inject;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jnario.jnario.test.util.BehaviorExecutor;
import org.jnario.jnario.test.util.SpecTestCreator;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

@CreateWith(SpecTestCreator.class)
@Named("Throws")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class ThrowsSpec {
  @Extension
  @org.jnario.runner.Extension
  @Inject
  public BehaviorExecutor _behaviorExecutor;
  
  @Test
  @Named("passes if exception is thrown")
  @Order(1)
  public void _passesIfExceptionIsThrown() throws Exception {
    this._behaviorExecutor.executesSuccessfully("package bootstrap\r\n\r\nimport java.util.Stack\r\nimport java.util.EmptyStackException\r\n\r\ndescribe \"throws\" {\r\n  fact new Stack<String>().pop throws EmptyStackException \r\n}\r\n");
  }
  
  @Test
  @Named("passes if exception of expected sub type is thrown")
  @Order(2)
  public void _passesIfExceptionOfExpectedSubTypeIsThrown() throws Exception {
    this._behaviorExecutor.executesSuccessfully("package bootstrap\r\n\r\nimport java.util.Stack\r\n\r\ndescribe \"throws\" {\r\n  fact new Stack<String>().pop throws Throwable \r\n}\r\n");
  }
  
  @Test
  @Named("fails if no exception is thrown")
  @Order(3)
  public void _failsIfNoExceptionIsThrown() throws Exception {
    this._behaviorExecutor.executionFails("package bootstrap\r\n\r\ndescribe \"throws\" {\r\n  fact 1 + 1 throws RuntimeException \r\n}\r\n");
  }
}
