package org.jnario.feature.jvmmodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.jnario.feature.feature.FeaturePackage;
import org.jnario.feature.feature.Given;
import org.jnario.feature.feature.GivenReference;
import org.jnario.feature.feature.Scenario;
import org.jnario.feature.feature.Step;
import org.jnario.feature.feature.Then;
import org.jnario.feature.feature.ThenReference;
import org.jnario.feature.feature.When;
import org.jnario.feature.feature.WhenReference;

@SuppressWarnings("all")
public class StepTypeProvider {
  public static final Set<EClass> ANDS = Collections.<EClass>unmodifiableSet(CollectionLiterals.<EClass>newHashSet(FeaturePackage.eINSTANCE.getBut(), FeaturePackage.eINSTANCE.getButReference(), FeaturePackage.eINSTANCE.getAnd(), FeaturePackage.eINSTANCE.getAndReference()));
  
  public static final Set<EClass> GIVEN = Collections.<EClass>unmodifiableSet(CollectionLiterals.<EClass>newHashSet(FeaturePackage.eINSTANCE.getGiven(), FeaturePackage.eINSTANCE.getGivenReference()));
  
  public static final Set<EClass> WHEN = Collections.<EClass>unmodifiableSet(CollectionLiterals.<EClass>newHashSet(FeaturePackage.eINSTANCE.getWhen(), FeaturePackage.eINSTANCE.getWhenReference()));
  
  public static final Set<EClass> THEN = Collections.<EClass>unmodifiableSet(CollectionLiterals.<EClass>newHashSet(FeaturePackage.eINSTANCE.getThen(), FeaturePackage.eINSTANCE.getThenReference()));
  
  protected Set<EClass> _getExpectedTypes(final Given step) {
    return StepTypeProvider.GIVEN;
  }
  
  protected Set<EClass> _getExpectedTypes(final GivenReference step) {
    return StepTypeProvider.GIVEN;
  }
  
  protected Set<EClass> _getExpectedTypes(final WhenReference step) {
    return StepTypeProvider.WHEN;
  }
  
  protected Set<EClass> _getExpectedTypes(final When step) {
    return StepTypeProvider.WHEN;
  }
  
  protected Set<EClass> _getExpectedTypes(final Then step) {
    return StepTypeProvider.THEN;
  }
  
  protected Set<EClass> _getExpectedTypes(final ThenReference step) {
    return StepTypeProvider.THEN;
  }
  
  protected Set<EClass> _getExpectedTypes(final Step step) {
    return this.getExpectedTypes(this.getDefiningStep(step));
  }
  
  public EClass getActualType(final Step step) {
    return this.getDefiningStep(step).eClass();
  }
  
  private Step getDefiningStep(final Step step) {
    Step _xblockexpression = null;
    {
      EObject _eContainer = step.eContainer();
      final Scenario container = ((Scenario) _eContainer);
      final int index = container.getSteps().indexOf(step);
      int i = index;
      while ((i >= 0)) {
        {
          final Step candidate = container.getSteps().get(i);
          boolean _contains = StepTypeProvider.ANDS.contains(candidate.eClass());
          boolean _not = (!_contains);
          if (_not) {
            return candidate;
          }
          i = (i - 1);
        }
      }
      _xblockexpression = step;
    }
    return _xblockexpression;
  }
  
  public Set<EClass> getExpectedTypes(final Step step) {
    if (step instanceof Given) {
      return _getExpectedTypes((Given)step);
    } else if (step instanceof GivenReference) {
      return _getExpectedTypes((GivenReference)step);
    } else if (step instanceof Then) {
      return _getExpectedTypes((Then)step);
    } else if (step instanceof ThenReference) {
      return _getExpectedTypes((ThenReference)step);
    } else if (step instanceof When) {
      return _getExpectedTypes((When)step);
    } else if (step instanceof WhenReference) {
      return _getExpectedTypes((WhenReference)step);
    } else if (step != null) {
      return _getExpectedTypes(step);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(step).toString());
    }
  }
}
