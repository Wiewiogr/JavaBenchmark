package benchmark.result;

/**
 * Created by student on 17.05.17.
 */
public class MultipleResults implements Result{
    private final long[] times;
    private final int[] arguments;
    private final String name;

    public MultipleResults(long[] times, int[] arguments, String name){

        this.times = times;
        this.arguments = arguments;
        this.name = name;
    }

    @Override
    public String getformattedResult() {
        StringBuilder builder = new StringBuilder().append(name).append("\n");
        for(int i = 0 ; i < times.length; i++){
            builder.append("\twith argument : ").
                    append(arguments[i]).
                    append(" took ").
                    append(times[i]).
                    append(" ns.\n");
        }
        return builder.toString();
    }
}
