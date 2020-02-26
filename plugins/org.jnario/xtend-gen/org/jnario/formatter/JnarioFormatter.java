package org.jnario.formatter;

import com.google.common.base.Objects;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericArrayTypeReference;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeConstraint;
import org.eclipse.xtext.common.types.JvmTypeParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmWildcardTypeReference;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IHiddenRegionFormatter;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.eclipse.xtext.formatting2.regionaccess.ITextSegment;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.XAssignment;
import org.eclipse.xtext.xbase.XBasicForLoopExpression;
import org.eclipse.xtext.xbase.XBinaryOperation;
import org.eclipse.xtext.xbase.XBlockExpression;
import org.eclipse.xtext.xbase.XCastedExpression;
import org.eclipse.xtext.xbase.XClosure;
import org.eclipse.xtext.xbase.XCollectionLiteral;
import org.eclipse.xtext.xbase.XConstructorCall;
import org.eclipse.xtext.xbase.XDoWhileExpression;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.eclipse.xtext.xbase.XForLoopExpression;
import org.eclipse.xtext.xbase.XIfExpression;
import org.eclipse.xtext.xbase.XInstanceOfExpression;
import org.eclipse.xtext.xbase.XMemberFeatureCall;
import org.eclipse.xtext.xbase.XPostfixOperation;
import org.eclipse.xtext.xbase.XReturnExpression;
import org.eclipse.xtext.xbase.XSwitchExpression;
import org.eclipse.xtext.xbase.XSynchronizedExpression;
import org.eclipse.xtext.xbase.XThrowExpression;
import org.eclipse.xtext.xbase.XTryCatchFinallyExpression;
import org.eclipse.xtext.xbase.XTypeLiteral;
import org.eclipse.xtext.xbase.XVariableDeclaration;
import org.eclipse.xtext.xbase.XWhileExpression;
import org.eclipse.xtext.xbase.annotations.formatting2.XbaseWithAnnotationsFormatter;
import org.eclipse.xtext.xbase.annotations.xAnnotations.XAnnotation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xtype.XFunctionTypeRef;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;
import org.jnario.ExampleCell;
import org.jnario.ExampleColumn;
import org.jnario.ExampleRow;
import org.jnario.JnarioPackage;

/**
 * TODO NO_XTEND - Verify implementation
 * @author Sebastian Benz - Initial contribution and API
 */
@SuppressWarnings("all")
public class JnarioFormatter extends XbaseWithAnnotationsFormatter {
  private void formatRows(final EList<ExampleRow> rows, @Extension final IFormattableDocument format) {
    final Consumer<ExampleRow> _function = new Consumer<ExampleRow>() {
      @Override
      public void accept(final ExampleRow it) {
        final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.newLine();
          }
        };
        format.<ExampleRow>append(it, _function);
      }
    };
    rows.forEach(_function);
  }
  
  private void formatColumns(final EList<ExampleColumn> columns, @Extension final IFormattableDocument format) {
    final Consumer<ExampleColumn> _function = new Consumer<ExampleColumn>() {
      @Override
      public void accept(final ExampleColumn it) {
        final ISemanticRegion nameNode = JnarioFormatter.this.textRegionExtensions.regionFor(it).feature(JnarioPackage.Literals.EXAMPLE_COLUMN__NAME);
        final JvmTypeReference typeNode = it.getType();
        int _xifexpression = (int) 0;
        boolean _equals = Objects.equal(typeNode, null);
        if (_equals) {
          _xifexpression = nameNode.getLength();
        } else {
          int _offset = nameNode.getOffset();
          int _length = nameNode.getLength();
          int _plus = (_offset + _length);
          int _offset_1 = JnarioFormatter.this.textRegionExtensions.regionForEObject(typeNode).getOffset();
          _xifexpression = (_plus - _offset_1);
        }
        final int headerLength = _xifexpression;
        Integer _elvis = null;
        final Function1<ExampleCell, Integer> _function = new Function1<ExampleCell, Integer>() {
          @Override
          public Integer apply(final ExampleCell it) {
            return JnarioFormatter.this.getMultilineLength(format, JnarioFormatter.this.textRegionExtensions.regionForEObject(it.getExpression()));
          }
        };
        final Function2<Integer, Integer, Integer> _function_1 = new Function2<Integer, Integer, Integer>() {
          @Override
          public Integer apply(final Integer p1, final Integer p2) {
            return Integer.valueOf(Math.max((p1).intValue(), (p2).intValue()));
          }
        };
        Integer _reduce = IterableExtensions.<Integer>reduce(ListExtensions.<ExampleCell, Integer>map(it.getCells(), _function), _function_1);
        if (_reduce != null) {
          _elvis = _reduce;
        } else {
          _elvis = Integer.valueOf(0);
        }
        final Integer maxExprLength = _elvis;
        final int maxLength = Math.max(headerLength, (maxExprLength).intValue());
        final int columnLength = ((1 + maxLength) - headerLength);
        final Procedure1<IHiddenRegionFormatter> _function_2 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            it.oneSpace();
          }
        };
        format.<ExampleColumn>prepend(it, _function_2);
        final Procedure1<IHiddenRegionFormatter> _function_3 = new Procedure1<IHiddenRegionFormatter>() {
          @Override
          public void apply(final IHiddenRegionFormatter it) {
            JnarioFormatter.this.spaces(it, columnLength);
          }
        };
        format.prepend(JnarioFormatter.this.textRegionExtensions.regionFor(it).keyword("|"), _function_3);
        final Consumer<ExampleCell> _function_4 = new Consumer<ExampleCell>() {
          @Override
          public void accept(final ExampleCell it) {
            final Procedure1<IHiddenRegionFormatter> _function = new Procedure1<IHiddenRegionFormatter>() {
              @Override
              public void apply(final IHiddenRegionFormatter it) {
                it.oneSpace();
              }
            };
            format.<XExpression>prepend(it.getExpression(), _function);
            int _multilineLastSegmentLength = JnarioFormatter.this.getMultilineLastSegmentLength(format, JnarioFormatter.this.textRegionExtensions.regionForEObject(it.getExpression()));
            final int length = ((1 + maxLength) - _multilineLastSegmentLength);
            final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
              @Override
              public void apply(final IHiddenRegionFormatter it) {
                JnarioFormatter.this.spaces(it, length);
              }
            };
            format.<XExpression>append(it.getExpression(), _function_1);
          }
        };
        it.getCells().forEach(_function_4);
      }
    };
    columns.forEach(_function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = new Procedure1<IHiddenRegionFormatter>() {
      @Override
      public void apply(final IHiddenRegionFormatter it) {
        it.newLine();
      }
    };
    format.<ExampleColumn>append(IterableExtensions.<ExampleColumn>last(columns), _function_1);
  }
  
  public void spaces(final IHiddenRegionFormatter init, final int i) {
    final Function2<String, Integer, String> _function = new Function2<String, Integer, String>() {
      @Override
      public String apply(final String p1, final Integer p2) {
        return (p1 + " ");
      }
    };
    init.setSpace(IterableExtensions.<Integer, String>fold(new IntegerRange(1, i), "", _function));
  }
  
  private String[] getSplittedMultilineCell(final IFormattableDocument format, final ITextSegment segment) {
    String _text = format.getRegion().getText();
    int _offset = segment.getOffset();
    int _offset_1 = format.getRegion().getOffset();
    int _minus = (_offset - _offset_1);
    int _offset_2 = segment.getOffset();
    int _length = segment.getLength();
    int _plus = (_offset_2 + _length);
    int _offset_3 = format.getRegion().getOffset();
    int _minus_1 = (_plus - _offset_3);
    return _text.substring(_minus, _minus_1).split("\r?\n");
  }
  
  private int getMultilineLastSegmentLength(final IFormattableDocument format, final ITextSegment segment) {
    return IterableExtensions.<String>last(((Iterable<String>)Conversions.doWrapArray(this.getSplittedMultilineCell(format, segment)))).trim().length();
  }
  
  private Integer getMultilineLength(final IFormattableDocument format, final ITextSegment segment) {
    final Function1<String, Integer> _function = new Function1<String, Integer>() {
      @Override
      public Integer apply(final String it) {
        return Integer.valueOf(it.trim().length());
      }
    };
    final Function2<Integer, Integer, Integer> _function_1 = new Function2<Integer, Integer, Integer>() {
      @Override
      public Integer apply(final Integer p1, final Integer p2) {
        return Integer.valueOf(Math.max((p1).intValue(), (p2).intValue()));
      }
    };
    return IterableExtensions.<Integer>reduce(ListExtensions.<String, Integer>map(((List<String>)Conversions.doWrapArray(this.getSplittedMultilineCell(format, segment))), _function), _function_1);
  }
  
  /**
   * Hack: No node for type Void - prevent NullPointerException
   */
  @Override
  public void _format(final JvmParameterizedTypeReference type, final IFormattableDocument format) {
    ICompositeNode _findActualNodeFor = NodeModelUtils.findActualNodeFor(type);
    boolean _notEquals = (!Objects.equal(_findActualNodeFor, null));
    if (_notEquals) {
      super._format(type, format);
    }
  }
  
  @Override
  public void format(final Object type, final IFormattableDocument format) {
    if (type instanceof JvmTypeParameter) {
      _format((JvmTypeParameter)type, format);
      return;
    } else if (type instanceof JvmFormalParameter) {
      _format((JvmFormalParameter)type, format);
      return;
    } else if (type instanceof XtextResource) {
      _format((XtextResource)type, format);
      return;
    } else if (type instanceof XAssignment) {
      _format((XAssignment)type, format);
      return;
    } else if (type instanceof XBinaryOperation) {
      _format((XBinaryOperation)type, format);
      return;
    } else if (type instanceof XDoWhileExpression) {
      _format((XDoWhileExpression)type, format);
      return;
    } else if (type instanceof XFeatureCall) {
      _format((XFeatureCall)type, format);
      return;
    } else if (type instanceof XMemberFeatureCall) {
      _format((XMemberFeatureCall)type, format);
      return;
    } else if (type instanceof XPostfixOperation) {
      _format((XPostfixOperation)type, format);
      return;
    } else if (type instanceof XWhileExpression) {
      _format((XWhileExpression)type, format);
      return;
    } else if (type instanceof XFunctionTypeRef) {
      _format((XFunctionTypeRef)type, format);
      return;
    } else if (type instanceof JvmGenericArrayTypeReference) {
      _format((JvmGenericArrayTypeReference)type, format);
      return;
    } else if (type instanceof JvmParameterizedTypeReference) {
      _format((JvmParameterizedTypeReference)type, format);
      return;
    } else if (type instanceof JvmWildcardTypeReference) {
      _format((JvmWildcardTypeReference)type, format);
      return;
    } else if (type instanceof XBasicForLoopExpression) {
      _format((XBasicForLoopExpression)type, format);
      return;
    } else if (type instanceof XBlockExpression) {
      _format((XBlockExpression)type, format);
      return;
    } else if (type instanceof XCastedExpression) {
      _format((XCastedExpression)type, format);
      return;
    } else if (type instanceof XClosure) {
      _format((XClosure)type, format);
      return;
    } else if (type instanceof XCollectionLiteral) {
      _format((XCollectionLiteral)type, format);
      return;
    } else if (type instanceof XConstructorCall) {
      _format((XConstructorCall)type, format);
      return;
    } else if (type instanceof XForLoopExpression) {
      _format((XForLoopExpression)type, format);
      return;
    } else if (type instanceof XIfExpression) {
      _format((XIfExpression)type, format);
      return;
    } else if (type instanceof XInstanceOfExpression) {
      _format((XInstanceOfExpression)type, format);
      return;
    } else if (type instanceof XReturnExpression) {
      _format((XReturnExpression)type, format);
      return;
    } else if (type instanceof XSwitchExpression) {
      _format((XSwitchExpression)type, format);
      return;
    } else if (type instanceof XSynchronizedExpression) {
      _format((XSynchronizedExpression)type, format);
      return;
    } else if (type instanceof XThrowExpression) {
      _format((XThrowExpression)type, format);
      return;
    } else if (type instanceof XTryCatchFinallyExpression) {
      _format((XTryCatchFinallyExpression)type, format);
      return;
    } else if (type instanceof XTypeLiteral) {
      _format((XTypeLiteral)type, format);
      return;
    } else if (type instanceof XVariableDeclaration) {
      _format((XVariableDeclaration)type, format);
      return;
    } else if (type instanceof XAnnotation) {
      _format((XAnnotation)type, format);
      return;
    } else if (type instanceof JvmTypeConstraint) {
      _format((JvmTypeConstraint)type, format);
      return;
    } else if (type instanceof XExpression) {
      _format((XExpression)type, format);
      return;
    } else if (type instanceof XImportDeclaration) {
      _format((XImportDeclaration)type, format);
      return;
    } else if (type instanceof XImportSection) {
      _format((XImportSection)type, format);
      return;
    } else if (type instanceof EObject) {
      _format((EObject)type, format);
      return;
    } else if (type == null) {
      _format((Void)null, format);
      return;
    } else if (type != null) {
      _format(type, format);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(type, format).toString());
    }
  }
}
