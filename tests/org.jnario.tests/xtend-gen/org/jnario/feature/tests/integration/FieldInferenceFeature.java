/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.feature.tests.integration;

import org.jnario.feature.tests.integration.FieldInferenceFeatureInferringFieldsFromBackground;
import org.jnario.feature.tests.integration.FieldInferenceFeatureInferringFieldsFromBackgroundInDifferentFeature;
import org.jnario.feature.tests.integration.FieldInferenceFeatureInferringFieldsFromOtherScenario;
import org.jnario.feature.tests.integration.FieldInferenceFeatureInferringFieldsFromScenarioInDifferentFeatures;
import org.jnario.jnario.test.util.FeatureTestCreator;
import org.jnario.runner.Contains;
import org.jnario.runner.CreateWith;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;

@Contains({ FieldInferenceFeatureInferringFieldsFromOtherScenario.class, FieldInferenceFeatureInferringFieldsFromScenarioInDifferentFeatures.class, FieldInferenceFeatureInferringFieldsFromBackground.class, FieldInferenceFeatureInferringFieldsFromBackgroundInDifferentFeature.class })
@Named("Field Inference")
@CreateWith(FeatureTestCreator.class)
@RunWith(FeatureRunner.class)
@SuppressWarnings("all")
public class FieldInferenceFeature {
}
