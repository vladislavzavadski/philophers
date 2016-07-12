package by.training.philosopers;

import by.training.philosopers.domain.Fork;
import by.training.philosopers.util.Philosopher;
import by.training.philosopers.util.Waiter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vladislav on 11.07.16.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Map<Integer, Fork> forkMap = new HashMap<>();
        Waiter waiter = new Waiter();
        forkMap.put(1, new Fork(1));
        forkMap.put(2, new Fork(2));
        forkMap.put(3, new Fork(3));
        forkMap.put(4, new Fork(4));
        forkMap.put(5, new Fork(5));

        Philosopher philosopher = new Philosopher(1, forkMap, waiter);
        Philosopher philosopher1 = new Philosopher(2, forkMap, waiter);
        Philosopher philosopher2 = new Philosopher(3, forkMap, waiter);
        Philosopher philosopher3 = new Philosopher(4, forkMap, waiter);
        Philosopher philosopher4 = new Philosopher(5, forkMap, waiter);

        new Thread(philosopher).start();
        new Thread(philosopher1).start();
        new Thread(philosopher2).start();
        new Thread(philosopher3).start();
        new Thread(philosopher4).start();
        new Thread(waiter).start();

        Thread.sleep(20000);

        philosopher.stop();
        philosopher1.stop();
        philosopher2.stop();
        philosopher3.stop();
        philosopher4.stop();
        waiter.stop();
    }
}
