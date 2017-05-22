package benchmark.test;

import benchmark.Benchmark;
import benchmark.result.MultipleResults;
import benchmark.result.Result;
import benchmark.result.SingleResult;
import benchmark.test.testInitializer.TestInitializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wiewiogr on 22.05.17.
 */
public class MultipleTest implements Test{
    Benchmark benchmark = null;
    Method method = null;
    Result result = null;
    TestInitializer initializer;
    int times = 1;
    int[] arguments;
    String name;

    
    public MultipleTest(TestInitializer initializer, String name, Benchmark benchmark, Method method, int times, int[] arguments){
        this.initializer = initializer;
        this.name = name;
        this.benchmark = benchmark;
        this.method = method;
        this.times = times;
        this.arguments = arguments;
    }
    
    public void runMethod(){
        initializer.initializeBenchmark(benchmark);
        //benchmark.setUp();
        runMethodWithArguments();
    }

    @Override
    public String getFormattedResult() {
        return result.getformattedResult();
    }

    private long benchmarkSingleMethodWithArgument(int argument) throws InvocationTargetException, IllegalAccessException {
        Double time = 0.0;
        for(int i = 0; i < timesRun; i++ ){
            benchmark.beforeBenchmark();
            long before = System.nanoTime();
            method.invoke(benchmark, argument);
            long after = System.nanoTime();
            time += 1.0*(after - before)/timesRun;
            benchmark.afterBenchmark();
        }
        return time.longValue();
    }

    private void runMethodWithArguments(){
        long[] runTimes = new long[times];
        try {
            for(int i = 0; i < times; i++) {
                benchmarkSingleMethodWithArgument(arguments[i]); //rozgrzeweczka
                runTimes[i] = benchmarkSingleMethodWithArgument(arguments[i]);
            }
            result = new MultipleResults(runTimes, arguments, name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
