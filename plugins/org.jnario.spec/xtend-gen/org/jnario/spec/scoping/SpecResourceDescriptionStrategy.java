/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.spec.scoping;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jnario.scoping.JnarioResourceDescriptionStrategy;
import org.jnario.spec.naming.OperationNameProvider;
import org.jnario.spec.spec.ExampleGroup;
import org.jnario.spec.spec.SpecPackage;
import org.jnario.util.Nodes;

@SuppressWarnings("all")
public class SpecResourceDescriptionStrategy extends JnarioResourceDescriptionStrategy {
  public static final String ROOT_SPEC = "root";
  
  public static final String TRUE = "1";
  
  public static final String FALSE = "0";
  
  public static final String EXAMPLE_TYPE = "type";
  
  public static final String EXAMPLE_OPERATION = "operation";
  
  @Inject
  private OperationNameProvider operationNameProvider;
  
  @Override
  public void createUserData(final EObject eObject, final ImmutableMap.Builder<String, String> userData) {
    super.createUserData(eObject, userData);
    if ((eObject instanceof ExampleGroup)) {
      final ExampleGroup exampleGroup = ((ExampleGroup) eObject);
      userData.put(SpecResourceDescriptionStrategy.ROOT_SPEC, String.valueOf(this.isRoot(exampleGroup)));
      userData.put(SpecResourceDescriptionStrategy.EXAMPLE_TYPE, this.getTargetName(exampleGroup));
      userData.put(SpecResourceDescriptionStrategy.EXAMPLE_OPERATION, this.getTargetOperation(exampleGroup));
    }
  }
  
  protected String getTargetName(final ExampleGroup exampleGroup) {
    return SpecResourceDescriptionStrategy.retrieveNameFromNodeModel(exampleGroup);
  }
  
  protected String getTargetOperation(final ExampleGroup exampleGroup) {
    String _xblockexpression = null;
    {
      Object _eGet = exampleGroup.eGet(SpecPackage.Literals.EXAMPLE_GROUP__TARGET_OPERATION, false);
      final JvmOperation targetOperation = ((JvmOperation) _eGet);
      if (((targetOperation != null) && (!targetOperation.eIsProxy()))) {
        return this.operationNameProvider.apply(targetOperation).toString();
      }
      _xblockexpression = SpecResourceDescriptionStrategy.getTargetOperationFromNodeModel(exampleGroup);
    }
    return _xblockexpression;
  }
  
  protected static String getTargetOperationFromNodeModel(final ExampleGroup exampleGroup) {
    return Nodes.textForFeature(exampleGroup, SpecPackage.Literals.EXAMPLE_GROUP__TARGET_OPERATION);
  }
  
  public String isRoot(final ExampleGroup exampleGroup) {
    String _xifexpression = null;
    EObject _eContainer = exampleGroup.eContainer();
    boolean _not = (!(_eContainer instanceof ExampleGroup));
    if (_not) {
      _xifexpression = SpecResourceDescriptionStrategy.TRUE;
    } else {
      _xifexpression = SpecResourceDescriptionStrategy.FALSE;
    }
    return _xifexpression;
  }
  
  public static String getTypeName(final IEObjectDescription specDescription) {
    return specDescription.getUserData(SpecResourceDescriptionStrategy.EXAMPLE_TYPE);
  }
  
  public static String getTargetOperation(final IEObjectDescription specDescription) {
    return specDescription.getUserData(SpecResourceDescriptionStrategy.EXAMPLE_OPERATION);
  }
  
  public static String getTypeName(final IResourceDescriptions index, final ExampleGroup exampleGroup) {
    final IResourceDescription localizedIndex = SpecResourceDescriptionStrategy.getLocalizedIndex(index, exampleGroup);
    if ((localizedIndex != null)) {
      final Iterable<IEObjectDescription> result = localizedIndex.getExportedObjectsByObject(exampleGroup);
      String _elvis = null;
      final Function1<IEObjectDescription, String> _function = (IEObjectDescription it) -> {
        return SpecResourceDescriptionStrategy.getTypeName(it);
      };
      String _head = IterableExtensions.<String>head(IterableExtensions.<IEObjectDescription, String>map(result, _function));
      if (_head != null) {
        _elvis = _head;
      } else {
        String _retrieveNameFromNodeModel = SpecResourceDescriptionStrategy.retrieveNameFromNodeModel(exampleGroup);
        _elvis = _retrieveNameFromNodeModel;
      }
      return _elvis;
    }
    return SpecResourceDescriptionStrategy.retrieveNameFromNodeModel(exampleGroup);
  }
  
  public static QualifiedName getOperationName(final IResourceDescriptions index, final ExampleGroup exampleGroup) {
    final IResourceDescription localizedIndex = SpecResourceDescriptionStrategy.getLocalizedIndex(index, exampleGroup);
    if ((localizedIndex != null)) {
      final Iterable<IEObjectDescription> result = localizedIndex.getExportedObjectsByObject(exampleGroup);
      String _elvis = null;
      final Function1<IEObjectDescription, String> _function = (IEObjectDescription it) -> {
        return SpecResourceDescriptionStrategy.getTargetOperation(it);
      };
      String _head = IterableExtensions.<String>head(IterableExtensions.<IEObjectDescription, String>map(result, _function));
      if (_head != null) {
        _elvis = _head;
      } else {
        String _targetOperationFromNodeModel = SpecResourceDescriptionStrategy.getTargetOperationFromNodeModel(exampleGroup);
        _elvis = _targetOperationFromNodeModel;
      }
      return QualifiedName.create(_elvis);
    }
    return QualifiedName.create(SpecResourceDescriptionStrategy.getTargetOperationFromNodeModel(exampleGroup));
  }
  
  private static IResourceDescription getLocalizedIndex(final IResourceDescriptions index, final EObject context) {
    if ((index instanceof IResourceDescriptions.IContextAware)) {
      ((IResourceDescriptions.IContextAware) index).setContext(context);
    }
    return index.getResourceDescription(context.eResource().getURI());
  }
  
  private static String retrieveNameFromNodeModel(final ExampleGroup exampleGroup) {
    String text = Nodes.textForFeature(exampleGroup, SpecPackage.Literals.EXAMPLE_GROUP__TARGET_TYPE);
    int _max = Math.max(text.lastIndexOf(Character.valueOf('.').charValue()), 
      text.lastIndexOf(Character.valueOf('$').charValue()));
    int begin = (_max + 1);
    int end = text.indexOf(Character.valueOf('<').charValue());
    if ((end == (-1))) {
      end = text.length();
    }
    return text.substring(begin, end);
  }
}
