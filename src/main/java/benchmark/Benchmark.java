package benchmark;

/**
 * Created by student on 17.05.17.
 */
public interface Benchmark {
    default void setUp() {}
    default void beforeBenchmark() {}
    default void afterBenchmark() {}
}
