package benchmark.result;

/**
 * Created by student on 17.05.17.
 */
public class SingleResult implements Result{
    long averageTime;
    String name;
    public SingleResult(long time, String name){
        averageTime = time;
        this.name = name;
    }

    @Override
    public String getformattedResult() {
        return new StringBuilder().
                append(name).
                append(" took ").
                append(averageTime).
                append(" ns.").
                toString();
    }
}
