package benchmark.test.testInitializer;

import benchmark.Benchmark;

/**
 * Created by wiewiogr on 22.05.17.
 */
public class TestInitializerWithoutArguments implements TestInitializer {
    @Override
    public Benchmark initializeBenchmark(Benchmark benchmark) {
        benchmark.setUp();
        return benchmark;
    }
}
