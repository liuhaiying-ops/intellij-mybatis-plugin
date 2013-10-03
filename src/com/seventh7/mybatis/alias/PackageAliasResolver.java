package com.seventh7.mybatis.alias;

import com.google.common.collect.Sets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.search.GlobalSearchScope;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * @author yanglin
 */
public abstract class PackageAliasResolver extends AliasResolver{

  public PackageAliasResolver(Project project) {
    super(project);
  }

  @NotNull @Override
  public Set<AliasDesc> getClssAliasDescriptions() {
    JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
    Set<AliasDesc> result = Sets.newHashSet();
    for (String pkgName : getPackages()) {
      if (null == pkgName) {
        continue;
      }
      PsiPackage pkg = javaPsiFacade.findPackage(pkgName);
      if (null != pkg) {
        addAliasDesc(result, pkg);
        PsiPackage[] packages = pkg.getSubPackages();
        for (PsiPackage tmp : packages) {
          if (null != tmp) {
            addAliasDesc(result, tmp);
          }
        }
      }
    }
    return result;
  }

  private void addAliasDesc(Set<AliasDesc> result, PsiPackage pkg) {
    for (PsiClass clzz : pkg.getClasses()) {
      addAliasDesc(result, clzz, clzz.getName());
    }
  }

  @NotNull
  public abstract Collection<String> getPackages();
}
