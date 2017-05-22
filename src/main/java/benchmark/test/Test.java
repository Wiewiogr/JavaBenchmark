package benchmark.test;

import benchmark.Benchmark;
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
public interface Test {
    static int timesRun = 5000000;

    void runMethod();
    String getFormattedResult();
}
