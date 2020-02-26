/**
 * Copyright (c) 2012 BMW Car IT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.doc;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.XBlockExpression;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.jnario.ExampleCell;
import org.jnario.ExampleColumn;
import org.jnario.ExampleRow;
import org.jnario.ExampleTable;
import org.jnario.Executable;
import org.jnario.JnarioClass;
import org.jnario.JnarioFile;
import org.jnario.doc.CssClassProvider;
import org.jnario.doc.DocumentationProvider;
import org.jnario.doc.ErrorMessageProvider;
import org.jnario.doc.Filter;
import org.jnario.doc.HtmlFile;
import org.jnario.doc.HtmlFileBuilder;
import org.jnario.doc.IconProvider;
import org.jnario.doc.WhiteSpaceNormalizer;
import org.jnario.report.Executable2ResultMapping;
import org.jnario.report.SpecExecution;

@SuppressWarnings("all")
public abstract class AbstractDocGenerator implements IGenerator {
  private static final Logger LOG = Logger.getLogger(AbstractDocGenerator.class);
  
  private static final String SEP = "_";
  
  @Inject
  @Extension
  private WhiteSpaceNormalizer _whiteSpaceNormalizer;
  
  @Inject
  @Extension
  private HtmlFileBuilder _htmlFileBuilder;
  
  @Inject
  @Extension
  private DocumentationProvider documentationProvider;
  
  @Inject
  @Extension
  private Executable2ResultMapping spec2ResultMapping;
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    this.doGenerate(input, fsa, this.spec2ResultMapping);
  }
  
  public void doGenerate(final Resource input, final IFileSystemAccess fsa, final Executable2ResultMapping spec2ResultMapping) {
    this.initResultMapping(spec2ResultMapping);
    final Consumer<JnarioFile> _function = new Consumer<JnarioFile>() {
      @Override
      public void accept(final JnarioFile it) {
        final Consumer<JnarioClass> _function = new Consumer<JnarioClass>() {
          @Override
          public void accept(final JnarioClass it) {
            AbstractDocGenerator.this._htmlFileBuilder.generate(it, fsa, AbstractDocGenerator.this.createHtmlFile(it));
          }
        };
        Iterables.<JnarioClass>filter(it.getXtendTypes(), JnarioClass.class).forEach(_function);
      }
    };
    Iterables.<JnarioFile>filter(input.getContents(), JnarioFile.class).forEach(_function);
  }
  
  protected Executable2ResultMapping initResultMapping(final Executable2ResultMapping spec2ResultMapping) {
    return this.spec2ResultMapping = spec2ResultMapping;
  }
  
  public HtmlFile createHtmlFile(final JnarioClass jnarioClass) {
    return HtmlFile.EMPTY_FILE;
  }
  
  protected String toTitle(final String string) {
    return Strings.toFirstUpper(this.decode(string));
  }
  
  protected String decode(final String string) {
    try {
      boolean _equals = Objects.equal(string, null);
      if (_equals) {
        return "";
      } else {
        return Strings.convertFromJavaString(string, true);
      }
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        final IllegalArgumentException e = (IllegalArgumentException)_t;
        AbstractDocGenerator.LOG.error("Exception when converting string", e);
        return string;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  protected String markdown2Html(final String string) {
    String _xblockexpression = null;
    {
      boolean _equals = Objects.equal(string, null);
      if (_equals) {
        return "";
      }
      String _normalize = this._whiteSpaceNormalizer.normalize(string);
      final String normalized = (_normalize + "\n");
      final String headingsCorrected = normalized.replaceAll("((^|\n)#+)([^# \\t])", "$1 $3");
      _xblockexpression = this.markdownToHtml(headingsCorrected).replaceAll("<pre><code>", "<pre class=\"prettyprint\">").replaceAll("</pre></code>", "</pre>");
    }
    return _xblockexpression;
  }
  
  public String markdownToHtml(final String markdown) {
    String _xblockexpression = null;
    {
      final MutableDataSet options = new MutableDataSet();
      final Parser parser = Parser.builder(options).build();
      final HtmlRenderer renderer = HtmlRenderer.builder(options).build();
      final Document document = parser.parse(markdown);
      final String html = renderer.render(document);
      _xblockexpression = html;
    }
    return _xblockexpression;
  }
  
  protected String _serialize(final XExpression expr, final List<Filter> filters) {
    return this.codeToHtml(this.serialize(expr)).trim();
  }
  
  protected String _serialize(final XBlockExpression expr, final List<Filter> filters) {
    String code = this.serialize(expr).trim();
    code = this.apply(filters, code);
    int _length = code.length();
    boolean _equals = (_length == 0);
    if (_equals) {
      return "";
    }
    int _length_1 = code.length();
    int _minus = (_length_1 - 1);
    code = code.substring(1, _minus);
    return this.codeToHtml(code);
  }
  
  protected String codeToHtml(final String code) {
    return this.toHtml(org.jnario.util.Strings.trimWhitespaceAtEnd(this._whiteSpaceNormalizer.normalize(code)).toString()).replace("\t", "  ");
  }
  
  protected String toHtml(final String input) {
    return StringEscapeUtils.escapeHtml(input);
  }
  
  protected String serialize(final EObject obj) {
    final ICompositeNode node = NodeModelUtils.getNode(obj);
    boolean _equals = Objects.equal(node, null);
    if (_equals) {
      return "";
    }
    return node.getText();
  }
  
  protected String id(final String id) {
    String _replaceAll = null;
    if (id!=null) {
      _replaceAll=id.replaceAll("\\W+", AbstractDocGenerator.SEP);
    }
    String _trim = null;
    if (_replaceAll!=null) {
      _trim=org.jnario.util.Strings.trim(_replaceAll, AbstractDocGenerator.SEP.charAt(0));
    }
    String _plus = (" id=\"" + _trim);
    return (_plus + "\"");
  }
  
  protected String apply(final List<Filter> filters, final String input) {
    String result = input;
    for (final Filter filter : filters) {
      result = filter.apply(result);
    }
    return result;
  }
  
  protected String root(final EObject jnarioClass) {
    final JnarioFile specFile = EcoreUtil2.<JnarioFile>getContainerOfType(jnarioClass, JnarioFile.class);
    final String packageName = specFile.getPackage();
    boolean _equals = Objects.equal(packageName, null);
    if (_equals) {
      return "";
    }
    final String[] fragments = packageName.split("\\.");
    final Function1<String, String> _function = new Function1<String, String>() {
      @Override
      public String apply(final String s) {
        return "../";
      }
    };
    final List<String> path = ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(fragments)), _function);
    return IterableExtensions.join(path, "");
  }
  
  protected CharSequence generate(final ExampleTable table) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<table class=\"table table-striped table-bordered table-condensed\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<thead>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<tr>");
    _builder.newLine();
    {
      EList<ExampleColumn> _columns = table.getColumns();
      for(final ExampleColumn headingCell : _columns) {
        _builder.append("\t\t");
        _builder.append("<th>");
        String _name = headingCell.getName();
        _builder.append(_name, "\t\t");
        _builder.append("</th>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("</tr>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</thead>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<tbody>");
    _builder.newLine();
    {
      EList<ExampleRow> _rows = table.getRows();
      for(final ExampleRow row : _rows) {
        _builder.append("\t");
        _builder.append("<tr>");
        _builder.newLine();
        {
          EList<ExampleCell> _cells = row.getCells();
          for(final ExampleCell cell : _cells) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("<td>");
            String _serialize = this.serialize(cell.getExpression(), CollectionLiterals.<Filter>emptyList());
            _builder.append(_serialize, "\t\t");
            _builder.append("</td>");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("</tr>");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("</tbody>");
    _builder.newLine();
    _builder.append("</table>");
    _builder.newLine();
    return _builder;
  }
  
  protected String htmlFileName(final String name) {
    return this._htmlFileBuilder.toHtmlFileName(name);
  }
  
  protected String documentation(final EObject obj) {
    return this.documentationProvider.getDocumentation(obj);
  }
  
  protected String fileName(final EObject eObject) {
    return eObject.eResource().getURI().lastSegment();
  }
  
  protected CharSequence pre(final EObject eObject, final String lang) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<pre class=\"prettyprint ");
    _builder.append(lang);
    _builder.append(" linenums\">");
    _builder.newLineIfNotEmpty();
    String _codeToHtml = this.codeToHtml(this.serialize(eObject));
    _builder.append(_codeToHtml);
    _builder.newLineIfNotEmpty();
    _builder.append("</pre>");
    _builder.newLine();
    return _builder;
  }
  
  protected String executionState(final Executable executable) {
    String _xblockexpression = null;
    {
      final SpecExecution result = this.spec2ResultMapping.getResult(executable);
      _xblockexpression = new IconProvider().doSwitch(result);
    }
    return _xblockexpression;
  }
  
  protected String executionStateClass(final Executable executable) {
    return new CssClassProvider().doSwitch(this.spec2ResultMapping.getResult(executable));
  }
  
  protected String errorMessage(final Executable executable) {
    return new ErrorMessageProvider().doSwitch(this.spec2ResultMapping.getResult(executable));
  }
  
  protected String serialize(final XExpression expr, final List<Filter> filters) {
    if (expr instanceof XBlockExpression) {
      return _serialize((XBlockExpression)expr, filters);
    } else if (expr != null) {
      return _serialize(expr, filters);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(expr, filters).toString());
    }
  }
}
