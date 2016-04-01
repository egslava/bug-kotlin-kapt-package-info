# Kotlin bugreport. kapt-project can't have package-info.java in one package with Kotlin class that contains static fields
**How to reproduce** (in short): if you download project, you can't do `./gradlew clean && ./gradlew assembleDebug && ./gradlew assembleDebug` without errors.

# Description

For instance in package `ru.egslava.app` you have two files:
```java
package ru.egslava.app

open class KotlinClassWithStaticField {
    companion object {
        @JvmField val FIELD = object : Any(){}
        // originally for using with FasterXML/Jackson
        // @JvmField val TYPE = object : TypeReference<KotlinClassWithStaticField>() {}
    }
}
```

and `package-info.java` with that content:
```java
package ru.egslava.app;
```
you __can't build project **twice**__. I mean you can build project after cleaning it up. But you can't assemble it next time. i.e.:

1. `./gradlew clean && ./gradlew assembleDebug` will work
1. `./gradlew clean && ./gradlew assembleDebug && ./gradlew assembleDebug` won't work

In case if your project has annotation processor (I use AA) it's cause fail in it:
```
ViacheslavEgorenkov:bugreport egslava$ ./gradlew assembleDebug
Incremental java compilation is an incubating feature.
:runnable:preBuild UP-TO-DATE
:runnable:preDebugBuild UP-TO-DATE
:runnable:checkDebugManifest
:runnable:prepareDebugDependencies
:runnable:compileDebugAidl UP-TO-DATE
:runnable:compileDebugRenderscript UP-TO-DATE
:runnable:generateDebugBuildConfig UP-TO-DATE
:runnable:generateDebugAssets UP-TO-DATE
:runnable:mergeDebugAssets UP-TO-DATE
:runnable:generateDebugResValues UP-TO-DATE
:runnable:generateDebugResources UP-TO-DATE
:runnable:mergeDebugResources UP-TO-DATE
:runnable:processDebugManifest UP-TO-DATE
:runnable:processDebugResources UP-TO-DATE
:runnable:generateDebugSources UP-TO-DATE
:runnable:compileDebugKotlin UP-TO-DATE
:runnable:compileDebugJavaWithJavac
/Users/egslava/git/bugreport/runnable/build/tmp/kapt/debug/java_src/__gen/annotation/Cl.java:1: error: Something went wrong: Unexpected error in AndroidAnnotations 4.0.0!
package __gen.annotation; class Cl { @__gen.KotlinAptAnnotation boolean v; }
                                                                        ^
  You should check if there is already an issue about it on https://github.com/excilys/androidannotations/search?q=java.lang.NullPointerException&type=Issues
  If none exists, please open a new one with the following content and tell us if you can reproduce it or not. Don't forget to give us as much information as you can (like parts of your code in failure).
  Java version: javac 1.8.0_65
  Javac processors options: kapt.annotations=/Users/egslava/git/bugreport/runnable/build/tmp/kapt/debug/wrappers/annotations.debug.txt, kapt.kotlin.generated=/Users/egslava/git/bugreport/runnable/build/tmp/kapt/debug/kotlinGenerated, androidManifestFile=/Users/egslava/git/bugreport/runnable/build/intermediates/manifests/full/debug/AndroidManifest.xml
  Stacktrace: java.lang.NullPointerException
  	at com.sun.tools.javac.jvm.Code.width(Code.java:279)
  	at com.sun.tools.javac.jvm.ClassReader.initParameterNames(ClassReader.java:2082)
  	at com.sun.tools.javac.jvm.ClassReader.readMethod(ClassReader.java:2040)
  	at com.sun.tools.javac.jvm.ClassReader.readClass(ClassReader.java:2252)
  	at com.sun.tools.javac.jvm.ClassReader.readClassFile(ClassReader.java:2324)
  	at com.sun.tools.javac.jvm.ClassReader.fillIn(ClassReader.java:2511)
  	at com.sun.tools.javac.jvm.ClassReader.complete(ClassReader.java:2442)
  	at com.sun.tools.javac.jvm.ClassReader.access$000(ClassReader.java:76)
  	at com.sun.tools.javac.jvm.ClassReader$1.complete(ClassReader.java:240)
  	at com.sun.tools.javac.code.Symbol.complete(Symbol.java:574)
  	at com.sun.tools.javac.code.Symbol$ClassSymbol.complete(Symbol.java:1037)
  	at com.sun.tools.javac.code.Symbol$ClassSymbol.flags(Symbol.java:973)
  	at com.sun.tools.javac.code.Symbol$TypeSymbol.getEnclosedElements(Symbol.java:734)
  	at javax.lang.model.util.ElementScanner6.visitPackage(ElementScanner6.java:167)
  	at com.sun.tools.javac.code.Symbol$PackageSymbol.accept(Symbol.java:901)
  	at com.sun.tools.javac.processing.JavacRoundEnvironment$AnnotationSetScanner.scan(JavacRoundEnvironment.java:158)
  	at com.sun.tools.javac.processing.JavacRoundEnvironment$AnnotationSetScanner.scan(JavacRoundEnvironment.java:127)
  	at com.sun.tools.javac.processing.JavacRoundEnvironment.getElementsAnnotatedWith(JavacRoundEnvironment.java:121)
  	at org.jetbrains.kotlin.annotation.RoundEnvironmentWrapper.getElementsAnnotatedWith(RoundEnvironmentWrapper.kt:24)
  	at org.androidannotations.internal.model.ModelExtractor.extractRootElementsAnnotations(ModelExtractor.java:141)
  	at org.androidannotations.internal.model.ModelExtractor.extract(ModelExtractor.java:48)
  	at org.androidannotations.internal.AndroidAnnotationProcessor.extractAnnotations(AndroidAnnotationProcessor.java:188)
  	at org.androidannotations.internal.AndroidAnnotationProcessor.processThrowing(AndroidAnnotationProcessor.java:158)
  	at org.androidannotations.internal.AndroidAnnotationProcessor.process(AndroidAnnotationProcessor.java:126)
  	at org.jetbrains.kotlin.annotation.AnnotationProcessorWrapper.process(AnnotationProcessorWrapper.kt:136)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.callProcessor(JavacProcessingEnvironment.java:794)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.discoverAndRunProcs(JavacProcessingEnvironment.java:705)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.access$1800(JavacProcessingEnvironment.java:91)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment$Round.run(JavacProcessingEnvironment.java:1035)
  	at com.sun.tools.javac.processing.JavacProcessingEnvironment.doProcessing(JavacProcessingEnvironment.java:1176)
  	at com.sun.tools.javac.main.JavaCompiler.processAnnotations(JavaCompiler.java:1170)
  	at com.sun.tools.javac.main.JavaCompiler.compile(JavaCompiler.java:856)
  	at com.sun.tools.javac.main.Main.compile(Main.java:523)
  	at com.sun.tools.javac.api.JavacTaskImpl.doCall(JavacTaskImpl.java:129)
  	at com.sun.tools.javac.api.JavacTaskImpl.call(JavacTaskImpl.java:138)
  	at org.gradle.api.internal.tasks.compile.JdkJavaCompiler.execute(JdkJavaCompiler.java:45)
  	at org.gradle.api.internal.tasks.compile.JdkJavaCompiler.execute(JdkJavaCompiler.java:33)
  	at org.gradle.api.internal.tasks.compile.NormalizingJavaCompiler.delegateAndHandleErrors(NormalizingJavaCompiler.java:101)
  	at org.gradle.api.internal.tasks.compile.NormalizingJavaCompiler.execute(NormalizingJavaCompiler.java:50)
  	at org.gradle.api.internal.tasks.compile.NormalizingJavaCompiler.execute(NormalizingJavaCompiler.java:36)
  	at org.gradle.api.internal.tasks.compile.CleaningJavaCompilerSupport.execute(CleaningJavaCompilerSupport.java:34)
  	at org.gradle.api.internal.tasks.compile.CleaningJavaCompilerSupport.execute(CleaningJavaCompilerSupport.java:25)
  	at org.gradle.api.tasks.compile.JavaCompile.performCompilation(JavaCompile.java:157)
  	at org.gradle.api.tasks.compile.JavaCompile.compile(JavaCompile.java:139)
  	at org.gradle.api.tasks.compile.JavaCompile.compile(JavaCompile.java:93)
  	at com.android.build.gradle.tasks.factory.AndroidJavaCompile.compile(AndroidJavaCompile.java:39)
  	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
  	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
  	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
  	at java.lang.reflect.Method.invoke(Method.java:497)
  	at org.gradle.internal.reflect.JavaMethod.invoke(JavaMethod.java:75)
  	at org.gradle.api.internal.project.taskfactory.AnnotationProcessingTaskFactory$IncrementalTaskAction.doExecute(AnnotationProcessingTaskFactory.java:244)
  	at org.gradle.api.internal.project.taskfactory.AnnotationProcessingTaskFactory$StandardTaskAction.execute(AnnotationProcessingTaskFactory.java:220)
  	at org.gradle.api.internal.project.taskfactory.AnnotationProcessingTaskFactory$IncrementalTaskAction.execute(AnnotationProcessingTaskFactory.java:231)
  	at org.gradle.api.internal.project.taskfactory.AnnotationProcessingTaskFactory$StandardTaskAction.execute(AnnotationProcessingTaskFactory.java:209)
  	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.executeAction(ExecuteActionsTaskExecuter.java:80)
  	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.executeActions(ExecuteActionsTaskExecuter.java:61)
  	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.execute(ExecuteActionsTaskExecuter.java:46)
  	at org.gradle.api.internal.tasks.execution.PostExecutionAnalysisTaskExecuter.execute(PostExecutionAnalysisTaskExecuter.java:35)
  	at org.gradle.api.internal.tasks.execution.SkipUpToDateTaskExecuter.execute(SkipUpToDateTaskExecuter.java:64)
  	at org.gradle.api.internal.tasks.execution.ValidatingTaskExecuter.execute(ValidatingTaskExecuter.java:58)
  	at org.gradle.api.internal.tasks.execution.SkipEmptySourceFilesTaskExecuter.execute(SkipEmptySourceFilesTaskExecuter.java:52)
  	at org.gradle.api.internal.tasks.execution.SkipTaskWithNoActionsExecuter.execute(SkipTaskWithNoActionsExecuter.java:52)
  	at org.gradle.api.internal.tasks.execution.SkipOnlyIfTaskExecuter.execute(SkipOnlyIfTaskExecuter.java:53)
  	at org.gradle.api.internal.tasks.execution.ExecuteAtMostOnceTaskExecuter.execute(ExecuteAtMostOnceTaskExecuter.java:43)
  	at org.gradle.execution.taskgraph.DefaultTaskGraphExecuter$EventFiringTaskWorker.execute(DefaultTaskGraphExecuter.java:203)
  	at org.gradle.execution.taskgraph.DefaultTaskGraphExecuter$EventFiringTaskWorker.execute(DefaultTaskGraphExecuter.java:185)
  	at org.gradle.execution.taskgraph.AbstractTaskPlanExecutor$TaskExecutorWorker.processTask(AbstractTaskPlanExecutor.java:66)
  	at org.gradle.execution.taskgraph.AbstractTaskPlanExecutor$TaskExecutorWorker.run(AbstractTaskPlanExecutor.java:50)
  	at org.gradle.execution.taskgraph.DefaultTaskPlanExecutor.process(DefaultTaskPlanExecutor.java:25)
  	at org.gradle.execution.taskgraph.DefaultTaskGraphExecuter.execute(DefaultTaskGraphExecuter.java:110)
  	at org.gradle.execution.SelectedTaskExecutionAction.execute(SelectedTaskExecutionAction.java:37)
  	at org.gradle.execution.DefaultBuildExecuter.execute(DefaultBuildExecuter.java:37)
  	at org.gradle.execution.DefaultBuildExecuter.access$000(DefaultBuildExecuter.java:23)
  	at org.gradle.execution.DefaultBuildExecuter$1.proceed(DefaultBuildExecuter.java:43)
  	at org.gradle.execution.DryRunBuildExecutionAction.execute(DryRunBuildExecutionAction.java:32)
  	at org.gradle.execution.DefaultBuildExecuter.execute(DefaultBuildExecuter.java:37)
  	at org.gradle.execution.DefaultBuildExecuter.execute(DefaultBuildExecuter.java:30)
  	at org.gradle.initialization.DefaultGradleLauncher$4.run(DefaultGradleLauncher.java:154)
  	at org.gradle.internal.Factories$1.create(Factories.java:22)
  	at org.gradle.internal.progress.DefaultBuildOperationExecutor.run(DefaultBuildOperationExecutor.java:90)
  	at org.gradle.internal.progress.DefaultBuildOperationExecutor.run(DefaultBuildOperationExecutor.java:52)
  	at org.gradle.initialization.DefaultGradleLauncher.doBuildStages(DefaultGradleLauncher.java:151)
  	at org.gradle.initialization.DefaultGradleLauncher.access$200(DefaultGradleLauncher.java:32)
  	at org.gradle.initialization.DefaultGradleLauncher$1.create(DefaultGradleLauncher.java:99)
  	at org.gradle.initialization.DefaultGradleLauncher$1.create(DefaultGradleLauncher.java:93)
  	at org.gradle.internal.progress.DefaultBuildOperationExecutor.run(DefaultBuildOperationExecutor.java:90)
  	at org.gradle.internal.progress.DefaultBuildOperationExecutor.run(DefaultBuildOperationExecutor.java:62)
  	at org.gradle.initialization.DefaultGradleLauncher.doBuild(DefaultGradleLauncher.java:93)
  	at org.gradle.initialization.DefaultGradleLauncher.run(DefaultGradleLauncher.java:82)
  	at org.gradle.launcher.exec.InProcessBuildActionExecuter$DefaultBuildController.run(InProcessBuildActionExecuter.java:94)
  	at org.gradle.tooling.internal.provider.ExecuteBuildActionRunner.run(ExecuteBuildActionRunner.java:28)
  	at org.gradle.launcher.exec.ChainingBuildActionRunner.run(ChainingBuildActionRunner.java:35)
  	at org.gradle.launcher.exec.InProcessBuildActionExecuter.execute(InProcessBuildActionExecuter.java:43)
  	at org.gradle.launcher.exec.InProcessBuildActionExecuter.execute(InProcessBuildActionExecuter.java:28)
  	at org.gradle.launcher.exec.ContinuousBuildActionExecuter.execute(ContinuousBuildActionExecuter.java:78)
  	at org.gradle.launcher.exec.ContinuousBuildActionExecuter.execute(ContinuousBuildActionExecuter.java:48)
  	at org.gradle.launcher.exec.DaemonUsageSuggestingBuildActionExecuter.execute(DaemonUsageSuggestingBuildActionExecuter.java:51)
  	at org.gradle.launcher.exec.DaemonUsageSuggestingBuildActionExecuter.execute(DaemonUsageSuggestingBuildActionExecuter.java:28)
  	at org.gradle.launcher.cli.RunBuildAction.run(RunBuildAction.java:43)
  	at org.gradle.internal.Actions$RunnableActionAdapter.execute(Actions.java:170)
  	at org.gradle.launcher.cli.CommandLineActionFactory$ParseAndBuildAction.execute(CommandLineActionFactory.java:237)
  	at org.gradle.launcher.cli.CommandLineActionFactory$ParseAndBuildAction.execute(CommandLineActionFactory.java:210)
  	at org.gradle.launcher.cli.JavaRuntimeValidationAction.execute(JavaRuntimeValidationAction.java:35)
  	at org.gradle.launcher.cli.JavaRuntimeValidationAction.execute(JavaRuntimeValidationAction.java:24)
  	at org.gradle.launcher.cli.CommandLineActionFactory$WithLogging.execute(CommandLineActionFactory.java:206)
  	at org.gradle.launcher.cli.CommandLineActionFactory$WithLogging.execute(CommandLineActionFactory.java:169)
  	at org.gradle.launcher.cli.ExceptionReportingAction.execute(ExceptionReportingAction.java:33)
  	at org.gradle.launcher.cli.ExceptionReportingAction.execute(ExceptionReportingAction.java:22)
  	at org.gradle.launcher.Main.doAction(Main.java:33)
  	at org.gradle.launcher.bootstrap.EntryPoint.run(EntryPoint.java:45)
  	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
  	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
  	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
  	at java.lang.reflect.Method.invoke(Method.java:497)
  	at org.gradle.launcher.bootstrap.ProcessBootstrap.runNoExit(ProcessBootstrap.java:54)
  	at org.gradle.launcher.bootstrap.ProcessBootstrap.run(ProcessBootstrap.java:35)
  	at org.gradle.launcher.GradleMain.main(GradleMain.java:23)
  	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
  	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
  	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
  	at java.lang.reflect.Method.invoke(Method.java:497)
  	at org.gradle.wrapper.BootstrapMainStarter.start(BootstrapMainStarter.java:33)
  	at org.gradle.wrapper.WrapperExecutor.execute(WrapperExecutor.java:130)
  	at org.gradle.wrapper.GradleWrapperMain.main(GradleWrapperMain.java:48)
1 error
:runnable:compileDebugJavaWithJavac FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':runnable:compileDebugJavaWithJavac'.
> Compilation failed; see the compiler error output for details.

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.

BUILD FAILED

Total time: 9.874 secs
```
