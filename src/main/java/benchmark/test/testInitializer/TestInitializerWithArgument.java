package benchmark.test.testInitializer;

import benchmark.Benchmark;

/**
 * Created by wiewiogr on 22.05.17.
 */
public class TestInitializerWithArgument implements TestInitializer{

    private int argument;

    public TestInitializerWithArgument(int argument){
        this.argument = argument;
    }

    @Override
    public Benchmark initializeBenchmark(Benchmark benchmark) {
        benchmark.setUpWithArgs(argument);
        return benchmark;
    }
}
