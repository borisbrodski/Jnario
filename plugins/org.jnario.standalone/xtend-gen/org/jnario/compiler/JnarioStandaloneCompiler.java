/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jnario.compiler;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.common.types.descriptions.IStubGenerator;
import org.eclipse.xtext.common.types.descriptions.JvmTypesResourceDescriptionStrategy;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.mwe.NameBasedFilter;
import org.eclipse.xtext.mwe.PathTraverser;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.parser.IEncodingProvider;
import org.eclipse.xtext.resource.FileExtensionProvider;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.containers.FlatResourceSetBasedAllContainersState;
import org.eclipse.xtext.resource.impl.ResourceSetBasedResourceDescriptions;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.compiler.IGeneratorConfigProvider;
import org.eclipse.xtext.xbase.compiler.JvmModelGenerator;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jnario.compiler.AbstractBatchCompiler;
import org.jnario.feature.FeatureStandaloneSetup;
import org.jnario.spec.SpecStandaloneSetup;
import org.jnario.suite.SuiteStandaloneSetup;

@SuppressWarnings("all")
public class JnarioStandaloneCompiler extends AbstractBatchCompiler {
  private static final Logger log = Logger.getLogger(JnarioStandaloneCompiler.class.getName());
  
  @Inject
  private IStubGenerator stubGenerator;
  
  private List<Injector> injectors;
  
  private Map<String, Injector> injectorMap;
  
  public static JnarioStandaloneCompiler create() {
    JnarioStandaloneCompiler _xblockexpression = null;
    {
      FeatureStandaloneSetup _featureStandaloneSetup = new FeatureStandaloneSetup();
      SpecStandaloneSetup _specStandaloneSetup = new SpecStandaloneSetup();
      SuiteStandaloneSetup _suiteStandaloneSetup = new SuiteStandaloneSetup();
      final List<? extends ISetup> setups = Collections.<ISetup>unmodifiableList(CollectionLiterals.<ISetup>newArrayList(_featureStandaloneSetup, _specStandaloneSetup, _suiteStandaloneSetup));
      _xblockexpression = JnarioStandaloneCompiler.fromSetups(setups);
    }
    return _xblockexpression;
  }
  
  public Map<String, Injector> initWithInjectors(final List<Injector> injectors) {
    Map<String, Injector> _xblockexpression = null;
    {
      this.injectors = injectors;
      final Function1<Injector, Pair<String, Injector>> _function = new Function1<Injector, Pair<String, Injector>>() {
        @Override
        public Pair<String, Injector> apply(final Injector it) {
          Pair<String, Injector> _xblockexpression = null;
          {
            final String fileExtension = it.<FileExtensionProvider>getInstance(FileExtensionProvider.class).getPrimaryFileExtension();
            _xblockexpression = Pair.<String, Injector>of(fileExtension, it);
          }
          return _xblockexpression;
        }
      };
      _xblockexpression = this.injectorMap = CollectionLiterals.<String, Injector>newHashMap(
        ((Pair<? extends String, ? extends Injector>[])Conversions.unwrapArray(ListExtensions.<Injector, Pair<String, Injector>>map(injectors, _function), Pair.class)));
    }
    return _xblockexpression;
  }
  
  public static JnarioStandaloneCompiler fromSetups(final List<? extends ISetup> setups) {
    JnarioStandaloneCompiler _jnarioStandaloneCompiler = new JnarioStandaloneCompiler();
    final Procedure1<JnarioStandaloneCompiler> _function = new Procedure1<JnarioStandaloneCompiler>() {
      @Override
      public void apply(final JnarioStandaloneCompiler it) {
        final Function1<ISetup, Injector> _function = new Function1<ISetup, Injector>() {
          @Override
          public Injector apply(final ISetup it) {
            return it.createInjectorAndDoEMFRegistration();
          }
        };
        it.injectors = IterableExtensions.<Injector>toList(JnarioStandaloneCompiler.eagerMap(setups, _function));
        IterableExtensions.<Injector>head(it.injectors).injectMembers(it);
        final Function1<Injector, Pair<String, Injector>> _function_1 = new Function1<Injector, Pair<String, Injector>>() {
          @Override
          public Pair<String, Injector> apply(final Injector it) {
            Pair<String, Injector> _xblockexpression = null;
            {
              final String fileExtension = it.<FileExtensionProvider>getInstance(FileExtensionProvider.class).getPrimaryFileExtension();
              _xblockexpression = Pair.<String, Injector>of(fileExtension, it);
            }
            return _xblockexpression;
          }
        };
        it.injectorMap = CollectionLiterals.<String, Injector>newHashMap(
          ((Pair<? extends String, ? extends Injector>[])Conversions.unwrapArray(ListExtensions.<Injector, Pair<String, Injector>>map(it.injectors, _function_1), Pair.class)));
      }
    };
    return ObjectExtensions.<JnarioStandaloneCompiler>operator_doubleArrow(_jnarioStandaloneCompiler, _function);
  }
  
  @Override
  protected ResourceSet loadXtendFiles(final ResourceSet resourceSet) {
    this.applyEncoding();
    EList<Adapter> _eAdapters = resourceSet.eAdapters();
    _eAdapters.add(
      new FlatResourceSetBasedAllContainersState(resourceSet) {
        @Override
        public Collection<URI> getContainedURIs(final String containerHandle) {
          final ArrayList<URI> uris = Lists.<URI>newArrayListWithCapacity(this.getResourceSet().getResources().size());
          EList<Resource> _resources = this.getResourceSet().getResources();
          for (final Resource r : _resources) {
            uris.add(r.getURI());
          }
          return uris;
        }
      });
    final List<NameBasedFilter> nameBasedFilter = this.getNameBasedFilters();
    final PathTraverser pathTraverser = new PathTraverser();
    final List<String> sourcePathDirectories = this.getSourcePathDirectories();
    final Predicate<URI> _function = new Predicate<URI>() {
      @Override
      public boolean apply(final URI src) {
        final Function1<NameBasedFilter, Boolean> _function = new Function1<NameBasedFilter, Boolean>() {
          @Override
          public Boolean apply(final NameBasedFilter it) {
            return Boolean.valueOf(it.matches(src));
          }
        };
        return IterableExtensions.<NameBasedFilter>exists(nameBasedFilter, _function);
      }
    };
    final Multimap<String, URI> pathes = pathTraverser.resolvePathes(sourcePathDirectories, _function);
    final Consumer<String> _function_1 = new Consumer<String>() {
      @Override
      public void accept(final String src) {
        final URI baseDir = URI.createFileURI((src + "/"));
        final String identifier = Joiner.on("_").join(baseDir.segments());
        final URI platformResourceURI = URI.createPlatformResourceURI((identifier + "/"), true);
        resourceSet.getURIConverter().getURIMap().put(platformResourceURI, baseDir);
        Collection<URI> _get = pathes.get(src);
        for (final URI uri : _get) {
          {
            boolean _isDebugEnabled = JnarioStandaloneCompiler.log.isDebugEnabled();
            if (_isDebugEnabled) {
              JnarioStandaloneCompiler.log.debug((("load xtend file \'" + uri) + "\'"));
            }
            final URI uriToUse = uri.replacePrefix(baseDir, platformResourceURI);
            resourceSet.getResource(uriToUse, true);
          }
        }
      }
    };
    pathes.keySet().forEach(_function_1);
    return resourceSet;
  }
  
  public void applyEncoding() {
    final Consumer<Injector> _function = new Consumer<Injector>() {
      @Override
      public void accept(final Injector it) {
        it.<IEncodingProvider.Runtime>getInstance(IEncodingProvider.Runtime.class).setDefaultEncoding(JnarioStandaloneCompiler.this.getFileEncoding());
      }
    };
    this.injectors.forEach(_function);
  }
  
  public List<NameBasedFilter> getNameBasedFilters() {
    final Function1<Injector, NameBasedFilter> _function = new Function1<Injector, NameBasedFilter>() {
      @Override
      public NameBasedFilter apply(final Injector it) {
        NameBasedFilter _xblockexpression = null;
        {
          final NameBasedFilter filter = new NameBasedFilter();
          filter.setExtension(it.<FileExtensionProvider>getInstance(FileExtensionProvider.class).getPrimaryFileExtension());
          _xblockexpression = filter;
        }
        return _xblockexpression;
      }
    };
    return JnarioStandaloneCompiler.<Injector, NameBasedFilter>eagerMap(this.injectors, _function);
  }
  
  @Override
  public File createStubs(final ResourceSet resourceSet) {
    final File outputDirectory = this.createTempDir("stubs");
    final JavaIoFileSystemAccess fileSystemAccess = this.javaIoFileSystemAccessProvider.get();
    fileSystemAccess.setOutputPath(outputDirectory.toString());
    final Consumer<Resource> _function = new Consumer<Resource>() {
      @Override
      public void accept(final Resource it) {
        IResourceDescription.Manager _findResourceDescriptionManager = JnarioStandaloneCompiler.this.findResourceDescriptionManager(it);
        IResourceDescription _resourceDescription = null;
        if (_findResourceDescriptionManager!=null) {
          _resourceDescription=_findResourceDescriptionManager.getResourceDescription(it);
        }
        final IResourceDescription description = _resourceDescription;
        boolean _notEquals = (!Objects.equal(description, null));
        if (_notEquals) {
          JnarioStandaloneCompiler.this.stubGenerator.doGenerateStubs(fileSystemAccess, description);
        }
      }
    };
    Lists.<Resource>newArrayList(resourceSet.getResources()).forEach(_function);
    return outputDirectory;
  }
  
  public IResourceDescription.Manager findResourceDescriptionManager(final Resource resource) {
    return this.<IResourceDescription.Manager>getInstance(resource, IResourceDescription.Manager.class);
  }
  
  public <T extends Object> T getInstance(final Resource resource, final Class<T> type) {
    Injector _get = this.injectorMap.get(resource.getURI().fileExtension().toLowerCase());
    T _instance = null;
    if (_get!=null) {
      _instance=_get.<T>getInstance(type);
    }
    return _instance;
  }
  
  @Override
  public void generateJavaFiles(final ResourceSet resourceSet) {
    final JavaIoFileSystemAccess javaIoFileSystemAccess = this.javaIoFileSystemAccessProvider.get();
    final String outputPath = this.outputPath;
    javaIoFileSystemAccess.setOutputPath(outputPath);
    javaIoFileSystemAccess.setWriteTrace(this.writeTraceFiles);
    final ResourceSetBasedResourceDescriptions resourceDescriptions = this.getResourceDescriptions(resourceSet);
    final Iterable<IEObjectDescription> exportedObjectsByType = resourceDescriptions.getExportedObjectsByType(TypesPackage.Literals.JVM_DECLARED_TYPE);
    boolean _isInfoEnabled = JnarioStandaloneCompiler.log.isInfoEnabled();
    if (_isInfoEnabled) {
      final int size = IterableExtensions.size(exportedObjectsByType);
      if ((size == 0)) {
        JnarioStandaloneCompiler.log.info((("No sources to compile in \'" + this.sourcePath) + "\'"));
      } else {
        String _xifexpression = null;
        if ((size == 1)) {
          _xifexpression = "file";
        } else {
          _xifexpression = "files";
        }
        String _plus = ((("Compiling " + Integer.valueOf(size)) + " source ") + _xifexpression);
        String _plus_1 = (_plus + " to ");
        String _plus_2 = (_plus_1 + outputPath);
        JnarioStandaloneCompiler.log.info(_plus_2);
      }
    }
    final Function1<IEObjectDescription, Boolean> _function = new Function1<IEObjectDescription, Boolean>() {
      @Override
      public Boolean apply(final IEObjectDescription it) {
        String _userData = it.getUserData(JvmTypesResourceDescriptionStrategy.IS_NESTED_TYPE);
        return Boolean.valueOf(Objects.equal(_userData, null));
      }
    };
    final Consumer<IEObjectDescription> _function_1 = new Consumer<IEObjectDescription>() {
      @Override
      public void accept(final IEObjectDescription eObjectDescription) {
        EObject _eObjectOrProxy = eObjectDescription.getEObjectOrProxy();
        final JvmDeclaredType jvmGenericType = ((JvmDeclaredType) _eObjectOrProxy);
        final JvmModelGenerator generator = JnarioStandaloneCompiler.this.<JvmModelGenerator>getInstance(jvmGenericType.eResource(), JvmModelGenerator.class);
        final IGeneratorConfigProvider generatorConfig = JnarioStandaloneCompiler.this.<IGeneratorConfigProvider>getInstance(jvmGenericType.eResource(), IGeneratorConfigProvider.class);
        final IQualifiedNameProvider nameProvider = JnarioStandaloneCompiler.this.<IQualifiedNameProvider>getInstance(jvmGenericType.eResource(), IQualifiedNameProvider.class);
        final CharSequence generatedType = generator.generateType(jvmGenericType, generatorConfig.get(jvmGenericType));
        final QualifiedName qualifiedName = nameProvider.getFullyQualifiedName(jvmGenericType);
        boolean _isDebugEnabled = JnarioStandaloneCompiler.log.isDebugEnabled();
        if (_isDebugEnabled) {
          String _javaFileName = JnarioStandaloneCompiler.this.getJavaFileName(qualifiedName);
          String _plus = ((("write \'" + outputPath) + File.separator) + _javaFileName);
          String _plus_1 = (_plus + "\'");
          JnarioStandaloneCompiler.log.debug(_plus_1);
        }
        javaIoFileSystemAccess.generateFile(JnarioStandaloneCompiler.this.getJavaFileName(qualifiedName), generatedType);
      }
    };
    IterableExtensions.<IEObjectDescription>filter(exportedObjectsByType, _function).forEach(_function_1);
  }
  
  public String getJavaFileName(final QualifiedName typeName) {
    String _concat = Strings.concat("/", typeName.getSegments());
    return (_concat + ".java");
  }
  
  public static <T extends Object, R extends Object> List<R> eagerMap(final List<T> list, final Function1<? super T, ? extends R> transformation) {
    ArrayList<R> _xblockexpression = null;
    {
      int _size = list.size();
      final ArrayList<R> result = new ArrayList<R>(_size);
      for (final T t : list) {
        CollectionExtensions.<R>addAll(result, transformation.apply(t));
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
}
