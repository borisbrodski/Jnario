package org.jnario.jvmmodel;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmAnnotationReference;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jnario.Executable;
import org.jnario.JnarioClass;
import org.jnario.JnarioMember;
import org.jnario.Specification;
import org.jnario.jvmmodel.TestRuntimeSupport;
import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.FeatureRunner;

@SuppressWarnings("all")
public class JUnit4RuntimeSupport implements TestRuntimeSupport {
  private static final String RUN_WITH = "org.junit.runner.RunWith";
  
  @Inject
  @Extension
  private JvmTypesBuilder _jvmTypesBuilder;
  
  @Inject
  private TypeReferences typeReferences;
  
  @Override
  public void addChildren(final Specification context, final JvmGenericType parent, final Collection<JvmTypeReference> children) {
    boolean _isEmpty = children.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      EList<JvmAnnotationReference> _annotations = parent.getAnnotations();
      JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(context, Contains.class, children);
      this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
    }
  }
  
  @Override
  public void afterAllMethod(final JnarioMember after, final JvmOperation operation) {
    EList<JvmAnnotationReference> _annotations = operation.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(after, "org.junit.AfterClass");
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  @Override
  public void afterMethod(final JnarioMember after, final JvmOperation operation) {
    EList<JvmAnnotationReference> _annotations = operation.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(after, "org.junit.After");
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  @Override
  public void beforeAllMethod(final JnarioMember before, final JvmOperation operation) {
    EList<JvmAnnotationReference> _annotations = operation.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(before, "org.junit.BeforeClass");
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  @Override
  public void beforeMethod(final JnarioMember before, final JvmOperation operation) {
    EList<JvmAnnotationReference> _annotations = operation.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(before, "org.junit.Before");
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  @Override
  public void markAsPending(final Executable element, final JvmOperation operation) {
    EList<JvmAnnotationReference> _annotations = operation.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(element, "org.junit.Ignore");
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  @Override
  public void updateExampleGroup(final JnarioClass exampleGroup, final JvmGenericType inferredType) {
    EList<JvmAnnotationReference> _annotations = inferredType.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(exampleGroup, JUnit4RuntimeSupport.RUN_WITH, this.getTypeForName(ExampleGroupRunner.class, exampleGroup));
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  @Override
  public void markAsTestMethod(final Executable element, final JvmOperation operation) {
    EList<JvmAnnotationReference> _annotations = operation.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(element, "org.junit.Test");
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  @Override
  public void updateFeature(final JnarioClass feature, final JvmGenericType inferredType, final List<JvmTypeReference> scenarios) {
    EList<JvmAnnotationReference> _annotations = inferredType.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(feature, JUnit4RuntimeSupport.RUN_WITH, this.getTypeForName(FeatureRunner.class, feature));
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  @Override
  public void updateScenario(final JnarioClass scenario, final JvmGenericType inferredType) {
    EList<JvmAnnotationReference> _annotations = inferredType.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(scenario, JUnit4RuntimeSupport.RUN_WITH, this.getTypeForName(FeatureRunner.class, scenario));
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  @Override
  public void updateSuite(final JnarioClass exampleGroup, final JvmGenericType inferredType) {
    EList<JvmAnnotationReference> _annotations = inferredType.getAnnotations();
    JvmAnnotationReference _annotation = this._jvmTypesBuilder.toAnnotation(exampleGroup, JUnit4RuntimeSupport.RUN_WITH, this.getTypeForName(ExampleGroupRunner.class, exampleGroup));
    this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
  }
  
  public JvmTypeReference getTypeForName(final Class<?> type, final Notifier context) {
    JvmTypeReference _xblockexpression = null;
    {
      final JvmTypeReference result = this.typeReferences.getTypeForName(type, context);
      boolean _equals = Objects.equal(result, null);
      if (_equals) {
        String _name = type.getName();
        String _plus = ("Jnario runtime could not be resolved: " + _name);
        throw new IllegalStateException(_plus);
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
}
