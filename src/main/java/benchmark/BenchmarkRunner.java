package benchmark;

import benchmark.annotations.Args;
import benchmark.annotations.Time;
import benchmark.test.MultipleTest;
import benchmark.test.SingleTest;
import benchmark.test.Test;
import benchmark.test.testInitializer.TestInitializerWithArgument;
import benchmark.test.testInitializer.TestInitializerWithoutArguments;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by student on 17.05.17.
 */
public class BenchmarkRunner {

    LinkedList<Test> tests = new LinkedList<>();

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
        Test test;
        String name = new StringBuilder()
                .append(method.getName())
                .append(" with set up args : ")
                .append(args)
                .toString();
        if(method.getAnnotation(Args.class) != null){
            int[] arguments = method.getAnnotation(Args.class).arguments();
            test = new MultipleTest(new TestInitializerWithArgument(args),
                    name, benchmark,
                    method, arguments.length, arguments);
        } else {
            test = new SingleTest(new TestInitializerWithArgument(args),
                    name, benchmark, method);
        }
        return test;
    }

    private Test createSingleTest(Method method, Class methodClass) throws InstantiationException, IllegalAccessException {
        Benchmark benchmark;
        benchmark = (Benchmark) methodClass.newInstance();
        Test test;
        if(method.getAnnotation(Args.class) != null){
            int[] arguments = method.getAnnotation(Args.class).arguments();
            test = new MultipleTest(new TestInitializerWithoutArguments(),
                    method.getName(), benchmark,
                    method, arguments.length, arguments);
        } else {
            test = new SingleTest(new TestInitializerWithoutArguments(),
                    method.getName(), benchmark, method);

        }
        return test;
    }

    private void prepareMethods(){
        for(Method method : findMethodsToBenchmark()){
            tests.addAll(prepareBenchmark(method));
        }
    }

    private void runAllTest(){
        while(!tests.isEmpty()){
            Test test = tests.pop();
            test.runMethod();
            System.out.println(test.getFormattedResult());
        }
    }

    public void runBenchmarks(){
        prepareMethods();
        runAllTest();
    }

//    public void showResults(){
//        for(Test test : tests){
//            if(test.result != null)
//                System.out.println(test.getFormattedResult());
//        }
//    }


    public static void run(){
        BenchmarkRunner runner = new BenchmarkRunner();
        runner.runBenchmarks();
        //runner.showResults();
    }

    public static void main(String[] args) {
        run();
    }
}
