package benchmark;

import benchmark.result.MultipleResults;
import benchmark.result.Result;
import benchmark.result.SingleResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by student on 17.05.17.
 */
public class Test {
    Benchmark benchmark;
    Method method;
    int times = 1;
    int[] arguments;
    Result result;
    private static int timesRun = 5000000;
    String name;

    public void runMethod(){
        if(times == 1) {
            runMethodWithoutArguments();
        } else {
            runMethodWithArguments();
        }
    }

    private long benchmarkSingleMethod() throws InvocationTargetException, IllegalAccessException {
        Double time = 0.0;
        for(int i = 0; i < timesRun; i++ ){
            benchmark.beforeBenchmark();
            long before = System.nanoTime();
            method.invoke(benchmark);
            long after = System.nanoTime();
            time += 1.0*(after - before)/timesRun;
            benchmark.afterBenchmark();
        }
        return time.longValue();
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

    private void runMethodWithoutArguments(){
        try {
            benchmarkSingleMethod(); //rozgrzeweczka
            long time = benchmarkSingleMethod();
            result = new SingleResult(time, name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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
