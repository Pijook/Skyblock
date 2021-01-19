package pl.trollcraft.Skyblock.essentials;

import java.util.concurrent.TimeUnit;

public class Watch {

    private static long startTime;
    private static long endTime;

    public static void start(){
        endTime = 0;
        startTime = System.nanoTime();
    }

    public static void end(){
        endTime = System.nanoTime();
    }

    public static long getTime(){
        long time = endTime - startTime;
        time = TimeUnit.SECONDS.convert(time, TimeUnit.NANOSECONDS);

        return time;
    }
}
