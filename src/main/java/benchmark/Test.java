package benchmark;

import benchmark.result.MultipleResults;
import benchmark.result.Result;
import benchmark.result.SingleResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private int timesRun = 1000000;


    public void runMethod(){
        if(times == 1) {
            runMethodWithoutArguments();
        } else {
            runMethodWithArguments();
        }
    }

    private long benchmarkSingleMethod() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Long> times = new ArrayList<>();
        for(int i = 0; i < timesRun; i++ ){
            long before = System.nanoTime();
            method.invoke(benchmark);
            long after = System.nanoTime();
            times.add(after - before);
        }
        long averageTime = times.stream().reduce((x,y) -> x+y).get()/timesRun;
        return averageTime;
    }
    private long benchmarkSingleMethodWithArgument(int argument) throws InvocationTargetException, IllegalAccessException {
        ArrayList<Long> times = new ArrayList<>();
        for(int i = 0; i < timesRun; i++ ){
            long before = System.nanoTime();
            method.invoke(benchmark, argument);
            long after = System.nanoTime();
            times.add(after - before);
        }
        long averageTime = times.stream().reduce((x,y) -> x+y).get()/timesRun;
        return averageTime;
    }

    private void runMethodWithoutArguments(){
        try {
            benchmarkSingleMethod(); //rozgrzeweczka
            long time = benchmarkSingleMethod();
            result = new SingleResult(time, method.getName());
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
            result = new MultipleResults(runTimes, arguments, method.getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
