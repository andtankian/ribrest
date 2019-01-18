package br.com.andrewribeiro.ribrest;

import br.com.andrewribeiro.ribrest.core.annotations.RibrestAppListener;
import br.com.andrewribeiro.ribrest.core.annotations.RibrestFilter;
import br.com.andrewribeiro.ribrest.core.annotations.RibrestModel;
import br.com.andrewribeiro.ribrest.core.applisteners.AppListener;
import br.com.andrewribeiro.ribrest.logs.RibrestLog;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.Path;

/**
 *
 * @author Andrew Ribeiro
 */
public class RibrestScanner {

    List getNamesOfClassesWithAnnotation(Class annotation) {
        return new FastClasspathScanner().scan()
                .getNamesOfClassesWithAnnotation(annotation);
    }

    List getClassesInstances(List classesNames) {
        List classes = new ArrayList();
        classesNames.forEach((scannedClass) -> {
            try {
                Class clazz = Class.forName((String) scannedClass);
                classes.add(clazz);
            } catch (ClassNotFoundException ex) {
            }
        });

        return classes;
    }

    String[] getClassesPackageNames(List classes) {
        classes = classes != null ? classes : new ArrayList();
        String[] packages = new String[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            packages[i] = ((String) classes.get(i)).substring(0, ((String) classes.get(i)).lastIndexOf("."));
        }

        return packages;
    }

    String[] getResourcesPackagesNames() {
        RibrestLog.log("Trying to scan independent resources annoted with @javax.ws.rs.Path...");
        List classesNames = getNamesOfClassesWithAnnotation(Path.class);
        RibrestLog.log(new StringBuilder("Scanned ").append(classesNames.size()).append(" independent resource classes, they are: ")
                .append(classesNames.toString()).toString());
        String[] packageNames = getClassesPackageNames(classesNames);
        RibrestLog.log(new StringBuilder("Identified the following packagenames for the scanned independent resources: ")
                .append(Arrays.toString(packageNames)).toString());
        return packageNames;
    }

    String[] getFiltersPackagesNames() {
        RibrestLog.log("Trying to scan filters annoted with @RibrestFilter");
        List classesNames = getNamesOfClassesWithAnnotation(RibrestFilter.class);
        RibrestLog.log(new StringBuilder("Scanned ").append(classesNames.size()).append(" filters, they are: ")
                .append(classesNames.toString()).toString());
        String[] packageNames = getClassesPackageNames(classesNames);
        RibrestLog.log(new StringBuilder("Identified the following packagenames for the scanned filters: ")
                .append(Arrays.toString(packageNames)).toString());
        return packageNames;
    }

    String[] getAllPackagesToBeRegistered() {
        return Stream.concat(Arrays.stream(getResourcesPackagesNames()), Arrays.stream(getFiltersPackagesNames()))
                .toArray(String[]::new);
    }

    List getModelClassesInstances() {
        RibrestLog.log("Trying to scan @RibrestModel annotated classes...");
        List modelClassesInstances = getClassesInstances(getNamesOfClassesWithAnnotation(RibrestModel.class));
        RibrestLog.log(new StringBuilder("Scanned ").append(modelClassesInstances.size()).append(" models, they are: ")
                .append(modelClassesInstances).toString());
        return modelClassesInstances;
    }

    List<Class<AppListener>> getAppListenersClassesInstances() {
        RibrestLog.log("Trying to scan @RibrestAppListener annotated classes...");
        List appListenersClassesNames = getNamesOfClassesWithAnnotation(RibrestAppListener.class);
        RibrestLog.log(new StringBuilder("Scanned ").append(appListenersClassesNames.size()).append(" applisteners, they are: ")
                .append(appListenersClassesNames).toString());
        return (List<Class<AppListener>>) appListenersClassesNames.stream().map((appListenerClassName) -> {
            try {
                return Class.forName((String) appListenerClassName);
            } catch (ClassNotFoundException exception) {
                throw new RuntimeException(new StringBuilder("AppListener class \"").append(appListenerClassName).append("\" was not found.").toString());
            }
        }).collect(Collectors.toList());
    }

}
