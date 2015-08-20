/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.feature.tests.integration;

import org.jnario.feature.tests.integration.TestRunnerFeatureCreateOnlyOneTestInstanceForOneJnarioScenario;
import org.jnario.runner.Contains;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;

/**
 * @author Birgit Engelmann - Initial contribution and API
 */
@Contains(TestRunnerFeatureCreateOnlyOneTestInstanceForOneJnarioScenario.class)
@Named("Test Runner")
@RunWith(FeatureRunner.class)
@SuppressWarnings("all")
public class TestRunnerFeature {
}
