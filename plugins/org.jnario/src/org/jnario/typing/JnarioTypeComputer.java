package org.jnario.typing;


import static org.jnario.jvmmodel.DoubleArrowSupport.isDoubleArrow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmPrimitiveType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.xbase.XAbstractFeatureCall;
import org.eclipse.xtext.xbase.XBinaryOperation;
import org.eclipse.xtext.xbase.XClosure;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XMemberFeatureCall;
import org.eclipse.xtext.xbase.XNullLiteral;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.typesystem.computation.IFeatureLinkingCandidate;
import org.eclipse.xtext.xbase.typesystem.computation.ILinkingCandidate;
import org.eclipse.xtext.xbase.typesystem.computation.ITypeComputationState;
import org.eclipse.xtext.xbase.typesystem.conformance.ConformanceHint;
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices;
import org.jnario.Assertion;
import org.jnario.Should;
import org.jnario.ShouldThrow;
import org.jnario.lib.Each;
import org.jnario.xbase.richstring.XbaseWithRichstringTypeComputer;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

public class JnarioTypeComputer extends XbaseWithRichstringTypeComputer {

	@Inject
	CommonTypeComputationServices services;
	@Inject
	TypeReferences typeReferences;

	@Override
	public void computeTypes(XExpression expression, ITypeComputationState state) {
		if (expression instanceof Assertion) {
			_computeType((Assertion) expression, state);
		} else if (expression instanceof ShouldThrow) {
			_computeType((ShouldThrow) expression, state);
		} else {
			super.computeTypes(expression, state);
		}
	}

	private void _computeType(ShouldThrow expression,
			ITypeComputationState state) {
		ITypeComputationState expressionState = state.withoutExpectation();
		expressionState.computeTypes(expression.getExpression());
		state.acceptActualType(getPrimitiveVoid(state),
				ConformanceHint.NO_IMPLICIT_RETURN);
	}

	private void _computeType(Assertion assertion, ITypeComputationState state) {
		ITypeComputationState conditionExpectation = state
				.withExpectation(getTypeForName(Boolean.TYPE, state));
		conditionExpectation.computeTypes(assertion.getExpression());
	}

	protected void _computeTypes(final XAbstractFeatureCall featureCall,
			ITypeComputationState state) {
		if (isShouldBeNull(featureCall) || isDoubleArrowNull(featureCall)) {
			List<? extends IFeatureLinkingCandidate> candidates = state.getLinkingCandidates(featureCall);
			for (IFeatureLinkingCandidate candidate : candidates) {
				if (candidate.getFeature() instanceof JvmOperation) {
					JvmOperation operation = (JvmOperation) candidate.getFeature();
					if(canHandleNullArg(operation)){
						candidate.applyToComputationState();
						return;
					}
				}
			}
		}
		super._computeTypes(featureCall, state);
	}

	private boolean canHandleNullArg(JvmOperation operation) {
		if(operation.getParameters().size() != 2){
			return false;
		}
		JvmTypeReference parameterType = operation.getParameters().get(1).getParameterType();
		if(parameterType == null){
			return false;
		}
		return !(parameterType.getType() instanceof JvmPrimitiveType);
	}

	private boolean isDoubleArrowNull(final XAbstractFeatureCall featureCall) {
		return isDoubleArrow(featureCall) && ((XBinaryOperation)featureCall).getRightOperand() instanceof XNullLiteral;
	}

	private boolean isShouldBeNull(final XAbstractFeatureCall featureCall) {
		return featureCall instanceof Should && ((Should) featureCall).getRightOperand() instanceof XNullLiteral;
	}

	/**
	 * The method {@link Each#forEach(org.jnario.lib.ExampleTable, org.eclipse.xtext.xbase.lib.Procedures.Procedure1)} get shadowed by
	 * Java 8 method <code>Iterable.forEach(...)</code>. This will be overritten here. The our <code>forEach</code> always win.
	 */
	@Override
	protected ILinkingCandidate getBestCandidate(List<? extends ILinkingCandidate> candidates) {
		if (candidates.size() == 2) {
			ILinkingCandidate candidateObjectExtensions = null;
			ILinkingCandidate candidateShould = null;
			for (ILinkingCandidate candidate : candidates) {
				if (candidate.getFeature() != null) {
					String name = candidate.getFeature().getQualifiedName();
					if (name.equals("org.eclipse.xtext.xbase.lib.ObjectExtensions.operator_doubleArrow")) {
						candidateObjectExtensions = candidate;
					} else if (name.equals("org.jnario.lib.Should.operator_doubleArrow")) {
						candidateShould = candidate;
					}
				}
			}
			if (candidateObjectExtensions != null && candidateShould != null) {
                XExpression expression = candidates.get(0).getExpression();
                if (expression instanceof XBinaryOperation && ((XBinaryOperation) expression).getRightOperand() instanceof XClosure) {
                	return candidateObjectExtensions;
                }
                return candidateShould;
			}
		}
		for (ILinkingCandidate candidate : candidates) {
			if (candidate.getFeature() instanceof JvmOperation && candidate.getFeature().eContainer() instanceof JvmGenericType) {
                JvmOperation feature = (JvmOperation) candidate.getFeature();
                JvmGenericType type = (JvmGenericType) feature.eContainer();
                if (Each.class.getName().equals(type.getQualifiedName()) && "forEach".equals(feature.getSimpleName())) {
                	if (candidate.getExpression() instanceof XMemberFeatureCall){
                		if (hasSameArguments(((XMemberFeatureCall)candidate.getExpression()), feature)){
                			return candidate;
                		}
                	}
                }
			}
		}
		return super.getBestCandidate(candidates);
	}
	
	private boolean hasSameArguments(XMemberFeatureCall featureCall, JvmOperation feature) {
		int callArguments = -1;
		XExpression argument = Iterables.getFirst(featureCall.getMemberCallArguments(), null);
		if (argument instanceof XClosure){
			EList<JvmFormalParameter> parameters = ((XClosure) argument).getDeclaredFormalParameters();
			callArguments = parameters != null ? parameters.size() : 0; 
		}
		@SuppressWarnings("unchecked")
		List<? extends JvmFormalParameter> parameters = MoreObjects.firstNonNull(feature.getParameters(), Collections.EMPTY_LIST);
		if (parameters.size() == 2){
			JvmTypeReference targetClosure = parameters.get(1).getParameterType(); //2nd parameter
			String closeureQualifiedName = targetClosure.getType().getQualifiedName();
			
			//if the closure has 1 or 0 arguments, we need the Procedure1 overloading
			if (Procedure1.class.getName().equals(closeureQualifiedName) && (callArguments == 1 || callArguments == 0)){
				return true;
				
			//if the closure has 2 arguments, we need the Procedure2 overloading
			}else if (Procedure2.class.getName().equals(closeureQualifiedName) && callArguments == 2){
				return true;
			}
		}
		
		return false;
	}
}