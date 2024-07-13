package com.azat4dev.booking.shared.domain.producers;

import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Observed
@RequiredArgsConstructor
public class OutboxEventsReaderOneAtTime implements OutboxEventsReader {

    private final OutboxEventsReader reader;
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final BlockingQueue<Boolean> queue = new LinkedBlockingQueue<>(2);
    private ConsumerThread consumer = null;

    @Override
    public void trigger() {

        if (isClosed.get()) {
            return;
        }

        queue.offer(true);

        synchronized (this) {
            if (consumer != null) {
                return;
            }

            consumer = new ConsumerThread(queue, reader);
            consumer.start();
        }
    }

    @Override
    public void close() {
        synchronized (this) {
            if (consumer == null) {
                return;
            }

            consumer.interrupt();
            consumer = null;
        }
    }

    @AllArgsConstructor
    private static class ConsumerThread extends Thread {

        private final BlockingQueue<Boolean> queue;
        private final OutboxEventsReader reader;

        @Override
        public void run() {
            while (true) {
                try {
                    if (Thread.interrupted()) {
                        return;
                    }

                    reader.trigger();
                    queue.take();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
