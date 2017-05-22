package benchmark.test;

import benchmark.Benchmark;
import benchmark.result.Result;
import benchmark.result.SingleResult;
import benchmark.test.testInitializer.TestInitializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wiewiogr on 22.05.17.
 */
public class SingleTest implements Test{
    Benchmark benchmark = null;
    Method method = null;
    Result result = null;
    private TestInitializer initializer;
    String name;


    public SingleTest(TestInitializer initializer, String name, Benchmark benchmark, Method method){
        this.initializer = initializer;
        this.name = name;
        this.benchmark = benchmark;
        this.method = method;
    }

    public void runMethod(){
        initializer.initializeBenchmark(benchmark);
        runMethodWithoutArguments();
    }

    @Override
    public String getFormattedResult() {
        return result.getformattedResult();
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
}
