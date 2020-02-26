/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.spec.jvmmodel;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmAnnotationReference;
import org.eclipse.xtext.common.types.JvmAnnotationValue;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIntAnnotationValue;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XNullLiteral;
import org.eclipse.xtext.xbase.XbaseFactory;
import org.eclipse.xtext.xbase.annotations.xAnnotations.XAnnotation;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociator;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.jnario.ExampleCell;
import org.jnario.ExampleColumn;
import org.jnario.ExampleRow;
import org.jnario.ExampleTable;
import org.jnario.JnarioClass;
import org.jnario.JnarioField;
import org.jnario.JnarioFile;
import org.jnario.JnarioFunction;
import org.jnario.JnarioMember;
import org.jnario.jvmmodel.ExtendedJvmTypesBuilder;
import org.jnario.jvmmodel.JnarioJvmModelInferrer;
import org.jnario.lib.ExampleTableRow;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.jnario.spec.jvmmodel.ImplicitSubject;
import org.jnario.spec.jvmmodel.SpecIgnoringXtendJvmModelInferrer;
import org.jnario.spec.jvmmodel.SpecSyntheticNameClashResolver;
import org.jnario.spec.naming.ExampleNameProvider;
import org.jnario.spec.spec.After;
import org.jnario.spec.spec.Before;
import org.jnario.spec.spec.Example;
import org.jnario.spec.spec.ExampleGroup;
import org.jnario.spec.spec.TestFunction;

/**
 * @author Sebastian Benz - Initial contribution and API
 */
@SuppressWarnings("all")
public class SpecJvmModelInferrer extends JnarioJvmModelInferrer {
  @Inject
  @Extension
  private ExtendedJvmTypesBuilder _extendedJvmTypesBuilder;
  
  @Inject
  @Extension
  private TypeReferences _typeReferences;
  
  @Inject
  @Extension
  private ExampleNameProvider _exampleNameProvider;
  
  @Inject
  @Extension
  private ImplicitSubject _implicitSubject;
  
  @Inject
  @Extension
  private SpecSyntheticNameClashResolver _specSyntheticNameClashResolver;
  
  @Inject
  @Extension
  private TypesFactory typesFactory;
  
  @Inject
  private SpecIgnoringXtendJvmModelInferrer xtendJvmModelInferrer;
  
  @Inject
  private IJvmModelAssociator modelAssociator;
  
  private int exampleIndex = 0;
  
  private int index = 0;
  
  @Override
  public void doInfer(final EObject object, final IJvmDeclaredTypeAcceptor acceptor, final boolean preIndexingPhase) {
    if ((!(object instanceof JnarioFile))) {
      return;
    }
    final JnarioFile jnarioFile = ((JnarioFile) object);
    this.xtendJvmModelInferrer.infer(object, acceptor, preIndexingPhase);
    final ArrayList<Runnable> doLater = CollectionLiterals.<Runnable>newArrayList();
    Iterable<ExampleGroup> _filter = Iterables.<ExampleGroup>filter(jnarioFile.getXtendTypes(), ExampleGroup.class);
    for (final ExampleGroup declaration : _filter) {
      this.infer(acceptor, declaration, null, doLater, preIndexingPhase);
    }
    this.exampleIndex = 0;
    if ((!preIndexingPhase)) {
      for (final Runnable runnable : doLater) {
        runnable.run();
      }
    }
  }
  
  public JvmGenericType infer(final IJvmDeclaredTypeAcceptor acceptor, final ExampleGroup exampleGroup, final JvmGenericType superType, final List<Runnable> doLater, final boolean preIndexingPhase) {
    JvmGenericType _xblockexpression = null;
    {
      boolean _notEquals = (!Objects.equal(superType, null));
      if (_notEquals) {
        exampleGroup.setExtends(this._typeReferences.createTypeRef(superType));
      } else {
        this.addSuperClass(exampleGroup);
      }
      final JvmGenericType javaType = this.typesFactory.createJvmGenericType();
      this.setNameAndAssociate(this.jnarioFile(exampleGroup), exampleGroup, javaType);
      acceptor.<JvmGenericType>accept(javaType);
      if ((!preIndexingPhase)) {
        final Runnable _function = () -> {
          this.initialize(exampleGroup, javaType);
        };
        doLater.add(_function);
      }
      final ArrayList<JvmGenericType> children = CollectionLiterals.<JvmGenericType>newArrayList();
      final Consumer<ExampleGroup> _function_1 = (ExampleGroup child) -> {
        JvmGenericType _infer = this.infer(acceptor, child, javaType, doLater, preIndexingPhase);
        children.add(_infer);
      };
      Iterables.<ExampleGroup>filter(exampleGroup.getMembers(), ExampleGroup.class).forEach(_function_1);
      boolean _isEmpty = children.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        final Function1<JvmGenericType, JvmTypeReference> _function_2 = (JvmGenericType it) -> {
          return this._typeReferences.createTypeRef(it);
        };
        this.getTestRuntime().addChildren(exampleGroup, javaType, ListExtensions.<JvmGenericType, JvmTypeReference>map(children, _function_2));
      }
      _xblockexpression = javaType;
    }
    return _xblockexpression;
  }
  
  @Override
  public void initialize(final JnarioClass source, final JvmGenericType inferredJvmType) {
    inferredJvmType.setVisibility(JvmVisibility.PUBLIC);
    final Function1<XAnnotation, Boolean> _function = (XAnnotation it) -> {
      JvmType _annotationType = null;
      if (it!=null) {
        _annotationType=it.getAnnotationType();
      }
      return Boolean.valueOf((!Objects.equal(_annotationType, null)));
    };
    this._extendedJvmTypesBuilder.addAnnotations(inferredJvmType, IterableExtensions.<XAnnotation>filter(source.getAnnotations(), _function));
    EList<JvmAnnotationReference> _annotations = inferredJvmType.getAnnotations();
    JvmAnnotationReference _annotationRef = this._annotationTypesBuilder.annotationRef(Named.class, this._exampleNameProvider.describe(source));
    this._extendedJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotationRef);
    JvmTypeReference _extends = source.getExtends();
    boolean _equals = Objects.equal(_extends, null);
    if (_equals) {
      final JvmTypeReference typeRefToObject = this._typeReferences.getTypeForName(Object.class, source);
      boolean _notEquals = (!Objects.equal(typeRefToObject, null));
      if (_notEquals) {
        inferredJvmType.getSuperTypes().add(typeRefToObject);
      }
    } else {
      inferredJvmType.getSuperTypes().add(this._extendedJvmTypesBuilder.cloneWithProxies(source.getExtends()));
    }
    this.getTestRuntime().updateExampleGroup(source, inferredJvmType);
    this.exampleIndex = 0;
    EList<JnarioMember> _members = source.getMembers();
    for (final JnarioMember member : _members) {
      this.transform(member, inferredJvmType);
    }
    this._implicitSubject.addImplicitSubject(inferredJvmType, ((ExampleGroup) source));
    this._extendedJvmTypesBuilder.copyDocumentationTo(source, inferredJvmType);
    this._specSyntheticNameClashResolver.resolveNameClashes(inferredJvmType);
  }
  
  protected void _transform(final Example element, final JvmGenericType container) {
    this.exampleIndex = (this.exampleIndex + 1);
    XExpression _expression = element.getExpression();
    boolean _equals = Objects.equal(_expression, null);
    if (_equals) {
      element.setExpression(XbaseFactory.eINSTANCE.createXBlockExpression());
    }
    final JvmOperation method = this.toMethod(element, container);
    this.getTestRuntime().markAsTestMethod(element, method);
    boolean _isPending = element.isPending();
    if (_isPending) {
      this.getTestRuntime().markAsPending(element, method);
    }
    EList<JvmAnnotationReference> _annotations = method.getAnnotations();
    JvmAnnotationReference _annotationRef = this._annotationTypesBuilder.annotationRef(Named.class, this._exampleNameProvider.describe(element));
    this._extendedJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotationRef);
    EList<JvmAnnotationReference> _annotations_1 = method.getAnnotations();
    JvmAnnotationReference _annotationRef_1 = this._annotationTypesBuilder.annotationRef(Order.class);
    final Procedure1<JvmAnnotationReference> _function = (JvmAnnotationReference it) -> {
      EList<JvmAnnotationValue> _explicitValues = it.getExplicitValues();
      JvmIntAnnotationValue _createJvmIntAnnotationValue = this.typesFactory.createJvmIntAnnotationValue();
      final Procedure1<JvmIntAnnotationValue> _function_1 = (JvmIntAnnotationValue it_1) -> {
        EList<Integer> _values = it_1.getValues();
        this._extendedJvmTypesBuilder.<Integer>operator_add(_values, Integer.valueOf(this.exampleIndex));
      };
      JvmIntAnnotationValue _doubleArrow = ObjectExtensions.<JvmIntAnnotationValue>operator_doubleArrow(_createJvmIntAnnotationValue, _function_1);
      this._extendedJvmTypesBuilder.<JvmAnnotationValue>operator_add(_explicitValues, _doubleArrow);
    };
    JvmAnnotationReference _doubleArrow = ObjectExtensions.<JvmAnnotationReference>operator_doubleArrow(_annotationRef_1, _function);
    this._extendedJvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations_1, _doubleArrow);
    EList<JvmMember> _members = container.getMembers();
    this._extendedJvmTypesBuilder.<JvmOperation>operator_add(_members, method);
    this.modelAssociator.associatePrimary(element, method);
  }
  
  protected void _transform(final Before element, final JvmGenericType container) {
    final Procedure2<JnarioMember, JvmOperation> _function = (JnarioMember e, JvmOperation m) -> {
      this.getTestRuntime().beforeMethod(e, m);
    };
    final Procedure2<JnarioMember, JvmOperation> _function_1 = (JnarioMember e, JvmOperation m) -> {
      this.getTestRuntime().beforeAllMethod(e, m);
    };
    this.transformAround(element, container, _function, _function_1);
  }
  
  protected void _transform(final After element, final JvmGenericType container) {
    final Procedure2<JnarioMember, JvmOperation> _function = (JnarioMember e, JvmOperation m) -> {
      this.getTestRuntime().afterMethod(e, m);
    };
    final Procedure2<JnarioMember, JvmOperation> _function_1 = (JnarioMember e, JvmOperation m) -> {
      this.getTestRuntime().afterAllMethod(e, m);
    };
    this.transformAround(element, container, _function, _function_1);
  }
  
  public boolean transformAround(final TestFunction element, final JvmGenericType container, final Procedure2<JnarioMember, JvmOperation> around, final Procedure2<JnarioMember, JvmOperation> aroundAll) {
    boolean _xblockexpression = false;
    {
      final JvmOperation afterMethod = this.toMethod(element, container);
      boolean _isStatic = element.isStatic();
      if (_isStatic) {
        aroundAll.apply(((JnarioMember) element), afterMethod);
      } else {
        around.apply(element, afterMethod);
      }
      EList<JvmMember> _members = container.getMembers();
      _xblockexpression = this._extendedJvmTypesBuilder.<JvmOperation>operator_add(_members, afterMethod);
    }
    return _xblockexpression;
  }
  
  @Override
  protected void _transform(final JnarioMember source, final JvmGenericType container) {
  }
  
  public JvmOperation toMethod(final TestFunction element, final JvmGenericType container) {
    JvmOperation _xblockexpression = null;
    {
      element.setReturnType(this._typeReferences.getTypeForName(Void.TYPE, element));
      JvmOperation _createJvmOperation = this.typesFactory.createJvmOperation();
      final Procedure1<JvmOperation> _function = (JvmOperation it) -> {
        it.setSimpleName(this._exampleNameProvider.toMethodName(element));
        it.setVisibility(JvmVisibility.PUBLIC);
        it.setStatic(element.isStatic());
        it.setReturnType(this._typeReferences.getTypeForName(Void.TYPE, element));
      };
      final JvmOperation operation = ObjectExtensions.<JvmOperation>operator_doubleArrow(_createJvmOperation, _function);
      this._extendedJvmTypesBuilder.setBody(operation, element.getExpression());
      final Function1<XAnnotation, Boolean> _function_1 = (XAnnotation it) -> {
        JvmType _annotationType = null;
        if (it!=null) {
          _annotationType=it.getAnnotationType();
        }
        return Boolean.valueOf((!Objects.equal(_annotationType, null)));
      };
      this._extendedJvmTypesBuilder.addAnnotations(operation, IterableExtensions.<XAnnotation>filter(element.getAnnotations(), _function_1));
      this._extendedJvmTypesBuilder.copyDocumentationTo(element, operation);
      container.getMembers().add(operation);
      EList<JvmTypeReference> _exceptions = operation.getExceptions();
      JvmTypeReference _typeForName = this._typeReferences.getTypeForName(Exception.class, element);
      this._extendedJvmTypesBuilder.<JvmTypeReference>operator_add(_exceptions, _typeForName);
      _xblockexpression = operation;
    }
    return _xblockexpression;
  }
  
  public void configureWith(final JvmGenericType type, final EObject source, final JnarioFile spec) {
    EList<EObject> _contents = spec.eResource().getContents();
    this._extendedJvmTypesBuilder.<JvmGenericType>operator_add(_contents, type);
    type.setPackageName(spec.getPackage());
    this._extendedJvmTypesBuilder.setDocumentation(type, this._extendedJvmTypesBuilder.getDocumentation(source));
  }
  
  protected void _transform(final ExampleTable table, final JvmGenericType specType) {
    this.associateTableWithSpec(specType, table);
    final Procedure1<JvmGenericType> _function = (JvmGenericType exampleTableType) -> {
      EList<JvmTypeReference> _superTypes = exampleTableType.getSuperTypes();
      JvmTypeReference _typeForName = this._typeReferences.getTypeForName(ExampleTableRow.class, table);
      this._extendedJvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeForName);
      this.configureWith(exampleTableType, table, this.jnarioFile(table));
      final JvmTypeReference type = this._typeReferences.getTypeForName(org.jnario.lib.ExampleTable.class, table, this._typeReferences.createTypeRef(exampleTableType));
      String _javaClassName = this._exampleNameProvider.toJavaClassName(table);
      final String initMethodName = ("_init" + _javaClassName);
      EList<JvmMember> _members = specType.getMembers();
      final Procedure1<JvmOperation> _function_1 = (JvmOperation it) -> {
        final Procedure1<ITreeAppendable> _function_2 = (ITreeAppendable a) -> {
          it.setDeclaringType(specType);
          this.generateInitializationMethod(table, a);
        };
        this._extendedJvmTypesBuilder.setBody(it, _function_2);
      };
      JvmOperation _method = this._extendedJvmTypesBuilder.toMethod(table, initMethodName, type, _function_1);
      this._extendedJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
      EList<JvmMember> _members_1 = specType.getMembers();
      final Procedure1<JvmField> _function_2 = (JvmField it) -> {
        it.setVisibility(JvmVisibility.PROTECTED);
        final Procedure1<ITreeAppendable> _function_3 = (ITreeAppendable it_1) -> {
          it_1.append(initMethodName).append("()");
        };
        this._extendedJvmTypesBuilder.setInitializer(it, _function_3);
      };
      JvmField _field = this._extendedJvmTypesBuilder.toField(table, this._exampleNameProvider.toFieldName(table), type, _function_2);
      this._extendedJvmTypesBuilder.<JvmField>operator_add(_members_1, _field);
      final Procedure1<JvmConstructor> _function_3 = (JvmConstructor it) -> {
        it.setSimpleName(exampleTableType.getSimpleName());
      };
      final JvmConstructor constructor = this._extendedJvmTypesBuilder.toConstructor(table, _function_3);
      EList<JvmMember> _members_2 = exampleTableType.getMembers();
      this._extendedJvmTypesBuilder.<JvmConstructor>operator_add(_members_2, constructor);
      final ArrayList<String> assignments = CollectionLiterals.<String>newArrayList();
      final JvmTypeReference stringType = this._typeReferences.getTypeForName(String.class, table);
      final JvmTypeReference listType = this._typeReferences.getTypeForName(List.class, table, stringType);
      final JvmFormalParameter cellNames = this.typesFactory.createJvmFormalParameter();
      cellNames.setName("cellNames");
      cellNames.setParameterType(listType);
      EList<JvmFormalParameter> _parameters = constructor.getParameters();
      this._extendedJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, cellNames);
      assignments.add("super(cellNames);");
      this.index = 0;
      final Consumer<ExampleColumn> _function_4 = (ExampleColumn column) -> {
        JvmTypeReference _xifexpression = null;
        JvmTypeReference _type = column.getType();
        boolean _notEquals = (!Objects.equal(_type, null));
        if (_notEquals) {
          _xifexpression = column.getType();
        } else {
          _xifexpression = this._extendedJvmTypesBuilder.inferredType();
        }
        final JvmTypeReference columnType = _xifexpression;
        EList<JvmMember> _members_3 = exampleTableType.getMembers();
        JvmField _field_1 = this._extendedJvmTypesBuilder.toField(column, column.getName(), this._extendedJvmTypesBuilder.cloneWithProxies(columnType));
        this._extendedJvmTypesBuilder.<JvmField>operator_add(_members_3, _field_1);
        final JvmFormalParameter param = this._extendedJvmTypesBuilder.toParameter(column, column.getName(), this._extendedJvmTypesBuilder.cloneWithProxies(columnType));
        EList<JvmFormalParameter> _parameters_1 = constructor.getParameters();
        this._extendedJvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, param);
        final JvmOperation getter = this._extendedJvmTypesBuilder.toGetter(column, column.getName(), this._extendedJvmTypesBuilder.cloneWithProxies(columnType));
        EList<JvmMember> _members_4 = exampleTableType.getMembers();
        this._extendedJvmTypesBuilder.<JvmOperation>operator_add(_members_4, getter);
        String _name = column.getName();
        String _plus = ("this." + _name);
        String _plus_1 = (_plus + " = ");
        String _name_1 = column.getName();
        String _plus_2 = (_plus_1 + _name_1);
        String _plus_3 = (_plus_2 + ";");
        assignments.add(_plus_3);
      };
      table.getColumns().forEach(_function_4);
      final Consumer<ExampleRow> _function_5 = (ExampleRow it) -> {
        final Consumer<ExampleCell> _function_6 = (ExampleCell it_1) -> {
          this.generateCellInitializerMethod(specType, this.initMethodName(table, this.index), it_1);
          this.index = (this.index + 1);
        };
        it.getCells().forEach(_function_6);
      };
      table.getRows().forEach(_function_5);
      final Procedure1<ITreeAppendable> _function_6 = (ITreeAppendable a) -> {
        final Consumer<String> _function_7 = (String it) -> {
          a.append(it).newLine();
        };
        assignments.forEach(_function_7);
      };
      this._extendedJvmTypesBuilder.setBody(constructor, _function_6);
    };
    this._extendedJvmTypesBuilder.toClass(this.jnarioFile(table), this._exampleNameProvider.toJavaClassName(table), _function);
  }
  
  public void generateInitializationMethod(final ExampleTable exampleTable, final ITreeAppendable appendable) {
    final JvmType arraysType = this._typeReferences.getTypeForName(Arrays.class, exampleTable).getType();
    String _fieldName = this._exampleNameProvider.toFieldName(exampleTable);
    String _plus = ("return ExampleTable.create(\"" + _fieldName);
    String _plus_1 = (_plus + "\", \n");
    appendable.append(_plus_1);
    ITreeAppendable _append = appendable.append("  ").append(arraysType).append(".asList(\"");
    String _join = IterableExtensions.join(this.columnNames(exampleTable), "\", \"");
    String _plus_2 = (_join + "\"), ");
    _append.append(_plus_2);
    appendable.increaseIndentation();
    appendable.append("\n");
    this.index = 0;
    EList<ExampleRow> _rows = exampleTable.getRows();
    for (final ExampleRow row : _rows) {
      {
        appendable.append("new ").append(this._exampleNameProvider.toJavaClassName(exampleTable)).append("(");
        ITreeAppendable _append_1 = appendable.append("  ").append(arraysType);
        final Function1<ExampleCell, String> _function = (ExampleCell it) -> {
          return Strings.convertToJavaString(this.serialize(it).trim());
        };
        String _join_1 = IterableExtensions.join(ListExtensions.<ExampleCell, String>map(row.getCells(), _function), "\", \"");
        String _plus_3 = (".asList(\"" + _join_1);
        String _plus_4 = (_plus_3 + "\"), ");
        _append_1.append(_plus_4);
        EList<ExampleCell> _cells = row.getCells();
        for (final ExampleCell cell : _cells) {
          {
            XExpression _expression = cell.getExpression();
            if ((_expression instanceof XNullLiteral)) {
              appendable.append("null");
            } else {
              String _initMethodName = this.initMethodName(exampleTable, this.index);
              String _plus_5 = (_initMethodName + "()");
              appendable.append(_plus_5);
            }
            this.index = (this.index + 1);
            ExampleCell _last = IterableExtensions.<ExampleCell>last(row.getCells());
            boolean _notEquals = (!Objects.equal(_last, cell));
            if (_notEquals) {
              appendable.append(", ");
            }
          }
        }
        appendable.append(")");
        ExampleRow _last = IterableExtensions.<ExampleRow>last(exampleTable.getRows());
        boolean _notEquals = (!Objects.equal(_last, row));
        if (_notEquals) {
          appendable.append(",\n");
        }
      }
    }
    appendable.decreaseIndentation();
    appendable.append("\n);");
  }
  
  public void associateTableWithSpec(final JvmGenericType type, final ExampleTable table) {
    EList<ExampleRow> _rows = table.getRows();
    for (final ExampleRow row : _rows) {
      this._extendedJvmTypesBuilder.<JvmGenericType>associate(row, type);
    }
  }
  
  public String initMethodName(final ExampleTable exampleTable, final int i) {
    String _javaClassName = this._exampleNameProvider.toJavaClassName(exampleTable);
    String _plus = ("_init" + _javaClassName);
    String _plus_1 = (_plus + "Cell");
    return (_plus_1 + Integer.valueOf(i));
  }
  
  public boolean generateCellInitializerMethod(final JvmGenericType specType, final String name, final ExampleCell cell) {
    EList<JvmMember> _members = specType.getMembers();
    final Procedure1<JvmOperation> _function = (JvmOperation it) -> {
      it.setDeclaringType(specType);
      this._extendedJvmTypesBuilder.setBody(it, cell.getExpression());
    };
    JvmOperation _method = this._extendedJvmTypesBuilder.toMethod(cell, name, this._extendedJvmTypesBuilder.inferredType(), _function);
    return this._extendedJvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
  }
  
  public List<String> columnNames(final ExampleTable exampleTable) {
    final Function1<ExampleColumn, String> _function = (ExampleColumn it) -> {
      String _name = null;
      if (it!=null) {
        _name=it.getName();
      }
      return _name;
    };
    return ListExtensions.<ExampleColumn, String>map(exampleTable.getColumns(), _function);
  }
  
  @Override
  protected void transform(final JnarioMember element, final JvmGenericType container) {
    if (element instanceof After) {
      _transform((After)element, container);
      return;
    } else if (element instanceof Before) {
      _transform((Before)element, container);
      return;
    } else if (element instanceof Example) {
      _transform((Example)element, container);
      return;
    } else if (element instanceof ExampleTable) {
      _transform((ExampleTable)element, container);
      return;
    } else if (element instanceof JnarioField) {
      _transform((JnarioField)element, container);
      return;
    } else if (element instanceof JnarioFunction) {
      _transform((JnarioFunction)element, container);
      return;
    } else if (element != null) {
      _transform(element, container);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(element, container).toString());
    }
  }
}
