package org.myan.examples;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by myan on 2019/3/18.
 */
public final class Utils {
    public static <T> T traceTime(Supplier<T> methord, TimeTracer tracer){
        long start = System.currentTimeMillis();
        try{
            T result = methord.get();
            return result;
        }finally{
            tracer.onTrace(System.currentTimeMillis() - start);
        }
    }

    public interface TimeTracer {
        void onTrace(long cast);
    }
}
