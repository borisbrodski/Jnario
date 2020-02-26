/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.jnario.test.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.jnario.JnarioTypeDeclaration;
import org.jnario.Specification;
import org.jnario.suite.suite.SpecReference;
import org.jnario.suite.suite.Suite;
import org.jnario.suite.suite.SuiteFactory;
import org.jnario.suite.suite.SuiteFile;

@SuppressWarnings("all")
public class Suites {
  private static final SuiteFactory factory = SuiteFactory.eINSTANCE;
  
  public static SuiteFile suiteFile(final Suite... suites) {
    final SuiteFile file = Suites.factory.createSuiteFile();
    CollectionExtensions.<Suite>addAll(file.getXtendTypes(), suites);
    return file;
  }
  
  public static Suite suiteWith(final String name, final SpecReference... references) {
    Suite _xblockexpression = null;
    {
      final Suite suite = Suites.suite(name);
      CollectionExtensions.<SpecReference>addAll(suite.getElements(), references);
      _xblockexpression = suite;
    }
    return _xblockexpression;
  }
  
  public static Suite suite(final String name) {
    Suite _xblockexpression = null;
    {
      final Suite suite = Suites.factory.createSuite();
      suite.setName(name);
      _xblockexpression = suite;
    }
    return _xblockexpression;
  }
  
  public static Suite suite(final String name, final String packageName) {
    Suite _xblockexpression = null;
    {
      final Suite suite = Suites.factory.createSuite();
      suite.setName(name);
      final SuiteFile file = Suites.factory.createSuiteFile();
      file.setPackage(packageName);
      EList<JnarioTypeDeclaration> _xtendTypes = file.getXtendTypes();
      _xtendTypes.add(suite);
      _xblockexpression = suite;
    }
    return _xblockexpression;
  }
  
  public static SpecReference specReference(final Specification spec) {
    SpecReference _xblockexpression = null;
    {
      final SpecReference result = Suites.factory.createSpecReference();
      result.setSpec(spec);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
}
