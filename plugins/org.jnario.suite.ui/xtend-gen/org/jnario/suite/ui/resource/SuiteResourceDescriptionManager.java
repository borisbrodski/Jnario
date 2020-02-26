/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.suite.ui.resource;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.builder.clustering.CurrentDescriptions;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.DerivedStateAwareResourceDescriptionManager;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.jnario.JnarioPackage;
import org.jnario.Specification;
import org.jnario.feature.feature.FeaturePackage;
import org.jnario.spec.spec.SpecPackage;
import org.jnario.suite.suite.PatternReference;
import org.jnario.suite.suite.Reference;
import org.jnario.suite.suite.SpecReference;
import org.jnario.suite.suite.Suite;
import org.jnario.suite.suite.SuitePackage;

@SuppressWarnings("all")
public class SuiteResourceDescriptionManager extends DerivedStateAwareResourceDescriptionManager {
  private static final Logger logger = Logger.getLogger(SuiteResourceDescriptionManager.class);
  
  private static final HashSet<String> FILE_EXTENSIONS = CollectionLiterals.<String>newHashSet("suite", "spec", "feature");
  
  @Inject
  @Extension
  private IQualifiedNameConverter _iQualifiedNameConverter;
  
  private final WeakHashMap<Collection<IResourceDescription.Delta>, List<QualifiedName>> cachedDeltas = new WeakHashMap<Collection<IResourceDescription.Delta>, List<QualifiedName>>();
  
  /**
   * Every spec change means new potential matches for a regex in a spec PatternReference.
   */
  @Override
  public boolean isAffected(final Collection<IResourceDescription.Delta> deltas, final IResourceDescription candidate, final IResourceDescriptions context) {
    boolean _isAffected = super.isAffected(deltas, candidate, context);
    if (_isAffected) {
      return true;
    }
    boolean _endsWith = candidate.getURI().toString().endsWith(".suite");
    if (_endsWith) {
      URI _uRI = candidate.getURI();
      String _plus = ("Considering suite: " + _uRI);
      SuiteResourceDescriptionManager.logger.debug(_plus);
      boolean _isTraceEnabled = SuiteResourceDescriptionManager.logger.isTraceEnabled();
      if (_isTraceEnabled) {
        SuiteResourceDescriptionManager.logger.debug(" Deltas to check: ");
        final Consumer<IResourceDescription.Delta> _function = new Consumer<IResourceDescription.Delta>() {
          @Override
          public void accept(final IResourceDescription.Delta it) {
            SuiteResourceDescriptionManager.logger.debug(("  " + it));
          }
        };
        deltas.forEach(_function);
      }
      ResourceSet _xifexpression = null;
      if ((context instanceof CurrentDescriptions)) {
        _xifexpression = ((CurrentDescriptions) context).getBuildData().getResourceSet();
      } else {
        return false;
      }
      final ResourceSet resourceSet = _xifexpression;
      List<QualifiedName> changedSpecs = this.cachedDeltas.get(deltas);
      boolean _equals = Objects.equal(changedSpecs, null);
      if (_equals) {
        final Function1<IResourceDescription.Delta, Boolean> _function_1 = new Function1<IResourceDescription.Delta, Boolean>() {
          @Override
          public Boolean apply(final IResourceDescription.Delta it) {
            return Boolean.valueOf(SuiteResourceDescriptionManager.this.isSpecFile(it));
          }
        };
        final Function1<IResourceDescription.Delta, Iterable<QualifiedName>> _function_2 = new Function1<IResourceDescription.Delta, Iterable<QualifiedName>>() {
          @Override
          public Iterable<QualifiedName> apply(final IResourceDescription.Delta it) {
            return SuiteResourceDescriptionManager.this.modifiedSpecs(it, resourceSet);
          }
        };
        changedSpecs = IterableExtensions.<QualifiedName>toList(Iterables.<QualifiedName>concat(IterableExtensions.<IResourceDescription.Delta, Iterable<QualifiedName>>map(IterableExtensions.<IResourceDescription.Delta>filter(deltas, _function_1), _function_2)));
        this.cachedDeltas.put(deltas, changedSpecs);
      } else {
        SuiteResourceDescriptionManager.logger.trace(" using cached deltas");
      }
      boolean _isEmpty = changedSpecs.isEmpty();
      if (_isEmpty) {
        SuiteResourceDescriptionManager.logger.debug(" no relevant changes in delta");
        return false;
      }
      boolean _isDebugEnabled = SuiteResourceDescriptionManager.logger.isDebugEnabled();
      if (_isDebugEnabled) {
        SuiteResourceDescriptionManager.logger.debug(" Changed specs: ");
        final Consumer<QualifiedName> _function_3 = new Consumer<QualifiedName>() {
          @Override
          public void accept(final QualifiedName it) {
            String _string = SuiteResourceDescriptionManager.this._iQualifiedNameConverter.toString(it);
            String _plus = ("  " + _string);
            SuiteResourceDescriptionManager.logger.debug(_plus);
          }
        };
        changedSpecs.forEach(_function_3);
      }
      final Iterable<IEObjectDescription> suites = candidate.getExportedObjectsByType(SuitePackage.Literals.SUITE);
      final Function1<IEObjectDescription, Suite> _function_4 = new Function1<IEObjectDescription, Suite>() {
        @Override
        public Suite apply(final IEObjectDescription it) {
          EObject _eObjectOrProxy = it.getEObjectOrProxy();
          EObject _resolve = EcoreUtil.resolve(((Suite) _eObjectOrProxy), resourceSet);
          return ((Suite) _resolve);
        }
      };
      final Function1<Suite, Boolean> _function_5 = new Function1<Suite, Boolean>() {
        @Override
        public Boolean apply(final Suite it) {
          boolean _eIsProxy = it.eIsProxy();
          return Boolean.valueOf((!_eIsProxy));
        }
      };
      final Function1<Suite, List<Pattern>> _function_6 = new Function1<Suite, List<Pattern>>() {
        @Override
        public List<Pattern> apply(final Suite it) {
          final Function1<PatternReference, Pattern> _function = new Function1<PatternReference, Pattern>() {
            @Override
            public Pattern apply(final PatternReference it) {
              return Pattern.compile(it.getPattern());
            }
          };
          return ListExtensions.<PatternReference, Pattern>map(SuiteResourceDescriptionManager.this.resolvePatternReferences(it), _function);
        }
      };
      final List<Pattern> matchers = IterableExtensions.<Pattern>toList(Iterables.<Pattern>concat(IterableExtensions.<Suite, List<Pattern>>map(IterableExtensions.<Suite>filter(IterableExtensions.<IEObjectDescription, Suite>map(suites, _function_4), _function_5), _function_6)));
      int _size = matchers.size();
      String _plus_1 = (" " + Integer.valueOf(_size));
      String _plus_2 = (_plus_1 + " referenced matchers");
      SuiteResourceDescriptionManager.logger.debug(_plus_2);
      boolean _isTraceEnabled_1 = SuiteResourceDescriptionManager.logger.isTraceEnabled();
      if (_isTraceEnabled_1) {
        final Consumer<Pattern> _function_7 = new Consumer<Pattern>() {
          @Override
          public void accept(final Pattern it) {
            String _pattern = it.pattern();
            String _plus = ("  " + _pattern);
            SuiteResourceDescriptionManager.logger.trace(_plus);
          }
        };
        matchers.forEach(_function_7);
      }
      final Function1<QualifiedName, Boolean> _function_8 = new Function1<QualifiedName, Boolean>() {
        @Override
        public Boolean apply(final QualifiedName specName) {
          final Function1<Pattern, Boolean> _function = new Function1<Pattern, Boolean>() {
            @Override
            public Boolean apply(final Pattern it) {
              return Boolean.valueOf(it.matcher(SuiteResourceDescriptionManager.this._iQualifiedNameConverter.toString(specName)).matches());
            }
          };
          Pattern _findFirst = IterableExtensions.<Pattern>findFirst(matchers, _function);
          return Boolean.valueOf((!Objects.equal(_findFirst, null)));
        }
      };
      final QualifiedName firstRelevant = IterableExtensions.<QualifiedName>findFirst(changedSpecs, _function_8);
      boolean _isDebugEnabled_1 = SuiteResourceDescriptionManager.logger.isDebugEnabled();
      if (_isDebugEnabled_1) {
        boolean _notEquals = (!Objects.equal(firstRelevant, null));
        if (_notEquals) {
          String _string = this._iQualifiedNameConverter.toString(firstRelevant);
          String _plus_3 = (">>  relevant spec change: " + _string);
          SuiteResourceDescriptionManager.logger.debug(_plus_3);
        } else {
          SuiteResourceDescriptionManager.logger.debug("  no relevant spec changes");
        }
      }
      return (!Objects.equal(firstRelevant, null));
    }
    return false;
  }
  
  protected List<PatternReference> _resolvePatternReferences(final Suite suite) {
    final Function1<Reference, List<PatternReference>> _function = new Function1<Reference, List<PatternReference>>() {
      @Override
      public List<PatternReference> apply(final Reference it) {
        return SuiteResourceDescriptionManager.this.resolvePatternReferences(it);
      }
    };
    return IterableExtensions.<PatternReference>toList(Iterables.<PatternReference>concat(ListExtensions.<Reference, List<PatternReference>>map(suite.getElements(), _function)));
  }
  
  protected List<PatternReference> _resolvePatternReferences(final SpecReference specRef) {
    return Collections.<PatternReference>emptyList();
  }
  
  protected List<PatternReference> _resolvePatternReferences(final PatternReference specRef) {
    List<PatternReference> _xblockexpression = null;
    {
      boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(specRef.getPattern());
      if (_isNullOrEmpty) {
        return Collections.<PatternReference>emptyList();
      }
      _xblockexpression = Collections.<PatternReference>singletonList(specRef);
    }
    return _xblockexpression;
  }
  
  public boolean isSpecFile(final IResourceDescription.Delta delta) {
    return SuiteResourceDescriptionManager.FILE_EXTENSIONS.contains(delta.getUri().fileExtension());
  }
  
  public Iterable<QualifiedName> modifiedSpecs(final IResourceDescription.Delta delta, final ResourceSet resourceSet) {
    Iterable<QualifiedName> _xblockexpression = null;
    {
      final IResourceDescription old = delta.getOld();
      final IResourceDescription _new = delta.getNew();
      Iterable<IEObjectDescription> _xifexpression = null;
      boolean _equals = Objects.equal(old, null);
      if (_equals) {
        _xifexpression = Collections.<IEObjectDescription>emptyList();
      } else {
        _xifexpression = this.topLevelSpecs(old, resourceSet);
      }
      final Iterable<IEObjectDescription> oldEObjects = _xifexpression;
      Iterable<IEObjectDescription> _xifexpression_1 = null;
      boolean _equals_1 = Objects.equal(_new, null);
      if (_equals_1) {
        _xifexpression_1 = Collections.<IEObjectDescription>emptyList();
      } else {
        _xifexpression_1 = this.topLevelSpecs(_new, resourceSet);
      }
      final Iterable<IEObjectDescription> newEObjects = _xifexpression_1;
      final Function1<IEObjectDescription, QualifiedName> _function = new Function1<IEObjectDescription, QualifiedName>() {
        @Override
        public QualifiedName apply(final IEObjectDescription it) {
          return it.getQualifiedName();
        }
      };
      final Map<QualifiedName, IEObjectDescription> before = IterableExtensions.<QualifiedName, IEObjectDescription>toMap(oldEObjects, _function);
      final Function1<IEObjectDescription, QualifiedName> _function_1 = new Function1<IEObjectDescription, QualifiedName>() {
        @Override
        public QualifiedName apply(final IEObjectDescription it) {
          return it.getQualifiedName();
        }
      };
      final Map<QualifiedName, IEObjectDescription> after = IterableExtensions.<QualifiedName, IEObjectDescription>toMap(newEObjects, _function_1);
      final Function1<QualifiedName, Boolean> _function_2 = new Function1<QualifiedName, Boolean>() {
        @Override
        public Boolean apply(final QualifiedName it) {
          boolean _containsKey = after.containsKey(it);
          return Boolean.valueOf((!_containsKey));
        }
      };
      final Set<QualifiedName> deleted = IterableExtensions.<QualifiedName>toSet(IterableExtensions.<QualifiedName>filter(before.keySet(), _function_2));
      final Function1<QualifiedName, Boolean> _function_3 = new Function1<QualifiedName, Boolean>() {
        @Override
        public Boolean apply(final QualifiedName it) {
          boolean _containsKey = before.containsKey(it);
          return Boolean.valueOf((!_containsKey));
        }
      };
      final Set<QualifiedName> added = IterableExtensions.<QualifiedName>toSet(IterableExtensions.<QualifiedName>filter(after.keySet(), _function_3));
      Set<QualifiedName> _keySet = before.keySet();
      Set<QualifiedName> _keySet_1 = after.keySet();
      final Function1<QualifiedName, Boolean> _function_4 = new Function1<QualifiedName, Boolean>() {
        @Override
        public Boolean apply(final QualifiedName it) {
          return Boolean.valueOf((!(deleted.contains(it) || added.contains(it))));
        }
      };
      final Iterable<QualifiedName> kept = IterableExtensions.<QualifiedName>filter(Iterables.<QualifiedName>concat(_keySet, _keySet_1), _function_4);
      final Function1<QualifiedName, Boolean> _function_5 = new Function1<QualifiedName, Boolean>() {
        @Override
        public Boolean apply(final QualifiedName it) {
          boolean _equals = SuiteResourceDescriptionManager.this.equals(before.get(it), after.get(it));
          return Boolean.valueOf((!_equals));
        }
      };
      final Iterable<QualifiedName> changed = IterableExtensions.<QualifiedName>filter(kept, _function_5);
      Iterable<QualifiedName> _plus = Iterables.<QualifiedName>concat(deleted, added);
      _xblockexpression = Iterables.<QualifiedName>concat(_plus, changed);
    }
    return _xblockexpression;
  }
  
  public Iterable<IEObjectDescription> topLevelSpecs(final IResourceDescription resource, final ResourceSet resourceSet) {
    final Function1<IEObjectDescription, Boolean> _function = new Function1<IEObjectDescription, Boolean>() {
      @Override
      public Boolean apply(final IEObjectDescription it) {
        boolean _xblockexpression = false;
        {
          final EObject object = EcoreUtil.resolve(it.getEObjectOrProxy(), resourceSet);
          _xblockexpression = (((object instanceof Specification) && (!Objects.equal(object.eContainer(), null))) && 
            (Objects.equal(object.eContainer().eClass(), SpecPackage.Literals.SPEC_FILE) || 
              Objects.equal(object.eContainer().eClass(), FeaturePackage.Literals.FEATURE_FILE)));
        }
        return Boolean.valueOf(_xblockexpression);
      }
    };
    return IterableExtensions.<IEObjectDescription>filter(resource.getExportedObjectsByType(JnarioPackage.Literals.SPECIFICATION), _function);
  }
  
  public boolean equals(final IEObjectDescription oldObj, final IEObjectDescription newObj) {
    boolean _equals = Objects.equal(oldObj, newObj);
    if (_equals) {
      return true;
    }
    EClass _eClass = oldObj.getEClass();
    EClass _eClass_1 = newObj.getEClass();
    boolean _notEquals = (!Objects.equal(_eClass, _eClass_1));
    if (_notEquals) {
      return false;
    }
    if (((!Objects.equal(oldObj.getName(), null)) && (!oldObj.getName().equals(newObj.getName())))) {
      return false;
    }
    boolean _equals_1 = oldObj.getEObjectURI().equals(newObj.getEObjectURI());
    boolean _not = (!_equals_1);
    if (_not) {
      return false;
    }
    return true;
  }
  
  public List<PatternReference> resolvePatternReferences(final Notifier suite) {
    if (suite instanceof Suite) {
      return _resolvePatternReferences((Suite)suite);
    } else if (suite instanceof PatternReference) {
      return _resolvePatternReferences((PatternReference)suite);
    } else if (suite instanceof SpecReference) {
      return _resolvePatternReferences((SpecReference)suite);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(suite).toString());
    }
  }
}
