package com.explosion_wands.tick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TickQueueManager {
    private static final List<TickQueue> ACTIVE_QUEUE = new ArrayList<>();
    private static final List<TickQueue> PENDING_ADD = new ArrayList<>();
    public static TickQueue createQueue(int amount, int amountPerTick) {
        TickQueue queue = new TickQueue(amount, amountPerTick);
        PENDING_ADD.add(queue);
        return queue;
    }

    public static void tick() {
        if(!PENDING_ADD.isEmpty()) {
            ACTIVE_QUEUE.addAll(PENDING_ADD);
            PENDING_ADD.clear();
        }
        Iterator<TickQueue> iterator = ACTIVE_QUEUE.iterator();

        while(iterator.hasNext()) {
            TickQueue queue = iterator.next();
            boolean finished = queue.tick();

            if(finished) {
                iterator.remove();
            }
        }
    }
}
