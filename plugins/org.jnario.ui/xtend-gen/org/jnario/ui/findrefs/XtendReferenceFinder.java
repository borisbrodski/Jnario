package org.jnario.ui.findrefs;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.ui.editor.findrefs.DefaultReferenceFinder;
import org.eclipse.xtext.ui.editor.findrefs.IReferenceFinder;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class XtendReferenceFinder extends DefaultReferenceFinder implements IReferenceFinder {
  private IQualifiedNameConverter nameConverter;
  
  @Inject
  public XtendReferenceFinder(final IResourceDescriptions indexData, final IResourceServiceProvider.Registry serviceProviderRegistry, final IQualifiedNameConverter nameConverter) {
    super(indexData, serviceProviderRegistry);
    this.nameConverter = nameConverter;
  }
  
  @Override
  public void findReferences(final Set<URI> targetURIs, final IResourceDescription resourceDescription, final IAcceptor<IReferenceDescription> acceptor, final IProgressMonitor monitor, final IReferenceFinder.ILocalResourceAccess localResourceAccess) {
    final Function1<URI, URI> _function = new Function1<URI, URI>() {
      @Override
      public URI apply(final URI it) {
        return it.trimFragment();
      }
    };
    final Function1<URI, Boolean> _function_1 = new Function1<URI, Boolean>() {
      @Override
      public Boolean apply(final URI it) {
        URI _uRI = resourceDescription.getURI();
        return Boolean.valueOf(Objects.equal(it, _uRI));
      }
    };
    boolean _exists = IterableExtensions.<URI>exists(IterableExtensions.<URI, URI>map(targetURIs, _function), _function_1);
    if (_exists) {
      return;
    }
    final HashSet<QualifiedName> names = CollectionLiterals.<QualifiedName>newHashSet();
    for (final URI uri : targetURIs) {
      final IUnitOfWork<Boolean, ResourceSet> _function_2 = new IUnitOfWork<Boolean, ResourceSet>() {
        @Override
        public Boolean exec(final ResourceSet it) throws Exception {
          boolean _xblockexpression = false;
          {
            final JvmType obj = EcoreUtil2.<JvmType>getContainerOfType(it.getEObject(uri, true), JvmType.class);
            boolean _xifexpression = false;
            boolean _notEquals = (!Objects.equal(obj, null));
            if (_notEquals) {
              QualifiedName _qualifiedName = XtendReferenceFinder.this.nameConverter.toQualifiedName(obj.getIdentifier().toLowerCase());
              _xifexpression = names.add(_qualifiedName);
            }
            _xblockexpression = _xifexpression;
          }
          return Boolean.valueOf(_xblockexpression);
        }
      };
      localResourceAccess.<Boolean>readOnly(uri, _function_2);
    }
    final Function1<QualifiedName, QualifiedName> _function_3 = new Function1<QualifiedName, QualifiedName>() {
      @Override
      public QualifiedName apply(final QualifiedName it) {
        return it.toLowerCase();
      }
    };
    final Set<QualifiedName> importedNames = IterableExtensions.<QualifiedName>toSet(IterableExtensions.<QualifiedName, QualifiedName>map(resourceDescription.getImportedNames(), _function_3));
    final Function1<QualifiedName, Boolean> _function_4 = new Function1<QualifiedName, Boolean>() {
      @Override
      public Boolean apply(final QualifiedName it) {
        return Boolean.valueOf(importedNames.contains(it));
      }
    };
    boolean _exists_1 = IterableExtensions.<QualifiedName>exists(names, _function_4);
    if (_exists_1) {
      final IUnitOfWork<Object, ResourceSet> _function_5 = new IUnitOfWork<Object, ResourceSet>() {
        @Override
        public Object exec(final ResourceSet it) throws Exception {
          final Function1<URI, Boolean> _function = new Function1<URI, Boolean>() {
            @Override
            public Boolean apply(final URI uri) {
              return Boolean.valueOf(targetURIs.contains(uri));
            }
          };
          final Function1<URI, Boolean> isInTargetURIs = _function;
          final IAcceptor<IReferenceDescription> _function_1 = new IAcceptor<IReferenceDescription>() {
            @Override
            public void accept(final IReferenceDescription it) {
              acceptor.accept(it);
            }
          };
          XtendReferenceFinder.this.findLocalReferencesInResource(new Predicate<URI>() {
              public boolean apply(URI input) {
                return isInTargetURIs.apply(input);
              }
          }, it.getResource(resourceDescription.getURI(), true), _function_1);
          return null;
        }
      };
      localResourceAccess.<Object>readOnly(resourceDescription.getURI(), _function_5);
    }
  }
}
