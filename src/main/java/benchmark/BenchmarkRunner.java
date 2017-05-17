package benchmark;

import benchmark.annotations.Args;
import benchmark.annotations.Time;
import org.reflections.Reflections;

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

    private Test prepareBenchmark(Method method){
        Class methodClass = method.getDeclaringClass();
        Benchmark benchmark = null;
        try {
            benchmark = (Benchmark) methodClass.newInstance();
            benchmark.setUp();
        } catch (Exception ignored) {}
        Test test = new Test();
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
            tests.add(prepareBenchmark(method));
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
