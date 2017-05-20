package benchmark;

import benchmark.annotations.Args;
import benchmark.annotations.Time;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 17.05.17.
 */
public class BenchmarkRunner {

    List<Test> tests = new ArrayList<>();

    private List<Method> findMethodsToBenchmark(){
        Reflections reflections = new Reflections();
        List<Method> methods = new ArrayList<>();
        for(Class benchClass :reflections.getSubTypesOf(Benchmark.class)){
            for(Method method: benchClass.getMethods()){
                if(method.getAnnotation(Time.class) != null) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    private List<Test> prepareBenchmark(Method method){
        List<Test> newTests = new ArrayList<>();
        Class methodClass = method.getDeclaringClass();
        try {
            if(methodClass.getMethod("setUpWithArgs", int.class).getAnnotation(Args.class) != null) {
                int[] args = methodClass.getMethod("setUpWithArgs", int.class).getAnnotation(Args.class).arguments();
                for(int i = 0; i < args.length; i++){
                   newTests.add(createTestWithSetUpArguments(method, methodClass, args[i]));
                }
            } else {
                newTests.add(createSingleTest(method, methodClass));
            }
        } catch (Exception ignored) {}
        return newTests;
    }

    private Test createTestWithSetUpArguments(Method method, Class methodClass,  int args) throws IllegalAccessException, InstantiationException {
        Benchmark benchmark;
        benchmark = (Benchmark) methodClass.newInstance();
        benchmark.setUpWithArgs(args);
        Test test = new Test();

        test.name = new StringBuilder()
                .append(method.getName())
                .append("(")
                .append(args)
                .append(")")
                .toString();
        test.benchmark = benchmark;
        test.method = method;
        if(method.getAnnotation(Args.class) != null){
            int[] arguments = method.getAnnotation(Args.class).arguments();
            test.times = arguments.length;
            test.arguments = arguments;
        }
        return test;
    }

    private Test createSingleTest(Method method, Class methodClass) throws InstantiationException, IllegalAccessException {
        Benchmark benchmark;
        benchmark = (Benchmark) methodClass.newInstance();
        benchmark.setUp();
        Test test = new Test();
        test.name = method.getName();
        test.benchmark = benchmark;
        test.method = method;
        if(method.getAnnotation(Args.class) != null){
            int[] arguments = method.getAnnotation(Args.class).arguments();
            test.times = arguments.length;
            test.arguments = arguments;
        }
        return test;
    }

    private void prepareMethods(){
        for(Method method : findMethodsToBenchmark()){
            tests.addAll(prepareBenchmark(method));
        }
    }

    private void runAllTest(){
        for(Test test : tests){
            test.runMethod();
        }

    }

    public void runBenchmarks(){
        prepareMethods();
        runAllTest();
    }

    public void showResults(){
        for(Test test : tests){
            if(test.result != null)
                System.out.println(test.result.getformattedResult());
        }
    }


    public static void run(){
        BenchmarkRunner runner = new BenchmarkRunner();
        runner.runBenchmarks();
        runner.showResults();
    }

    public static void main(String[] args) {
        run();
    }
}
