/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.suite.jvmmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.jnario.Specification;
import org.jnario.suite.jvmmodel.SpecResolver;
import org.jnario.suite.jvmmodel.SuiteNode;
import org.jnario.suite.suite.Reference;
import org.jnario.suite.suite.Suite;
import org.jnario.suite.suite.SuiteFile;

@SuppressWarnings("all")
public class SuiteNodeBuilder {
  private static final char PREFIX = "#".charAt(0);
  
  @Inject
  @Extension
  private SpecResolver _specResolver;
  
  public Iterable<SuiteNode> buildNodeModel(final SuiteFile suiteFile) {
    ArrayList<SuiteNode> _xblockexpression = null;
    {
      final List<Suite> suites = IterableExtensions.<Suite>toList(Iterables.<Suite>filter(suiteFile.getXtendTypes(), Suite.class));
      final ArrayList<SuiteNode> result = CollectionLiterals.<SuiteNode>newArrayList();
      boolean _isEmpty = suites.isEmpty();
      if (_isEmpty) {
        return result;
      }
      final HashMap<EObject, SuiteNode> mapping = CollectionLiterals.<EObject, SuiteNode>newHashMap();
      int _size = suites.size();
      int _minus = (_size - 1);
      IntegerRange _upTo = new IntegerRange(0, _minus);
      for (final Integer i : _upTo) {
        {
          final Suite current = suites.get((i).intValue());
          final SuiteNode parentNode = mapping.get(this.parent(suites, (i).intValue()));
          final SuiteNode currentNode = this.createNode(current, parentNode);
          mapping.put(current, currentNode);
          boolean _equals = Objects.equal(parentNode, null);
          if (_equals) {
            result.add(currentNode);
          }
        }
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  public SuiteNode createNode(final Suite current, final SuiteNode parent) {
    final Function1<Reference, List<Specification>> _function = new Function1<Reference, List<Specification>>() {
      @Override
      public List<Specification> apply(final Reference it) {
        return SuiteNodeBuilder.this._specResolver.resolveSpecs(it);
      }
    };
    final Iterable<Specification> specs = Iterables.<Specification>concat(ListExtensions.<Reference, List<Specification>>map(current.getElements(), _function));
    final SuiteNode node = new SuiteNode(current, specs);
    node.setParent(parent);
    return node;
  }
  
  public Suite parent(final List<Suite> suites, final int i) {
    if ((i == 0)) {
      return null;
    }
    final int current = this.level(suites.get(i));
    IntegerRange _upTo = new IntegerRange((i - 1), 0);
    for (final Integer j : _upTo) {
      {
        final Suite candidate = suites.get((j).intValue());
        int _level = this.level(candidate);
        boolean _greaterThan = (current > _level);
        if (_greaterThan) {
          return candidate;
        }
      }
    }
    return null;
  }
  
  public int level(final Suite suite) {
    final String name = suite.getName();
    int i = 0;
    while ((i < name.length())) {
      {
        char _charAt = name.charAt(i);
        boolean _notEquals = (_charAt != SuiteNodeBuilder.PREFIX);
        if (_notEquals) {
          return i;
        }
        i = (i + 1);
      }
    }
    return i;
  }
}
