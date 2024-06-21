package com.kanven.practice.file.fetcher;

import sun.misc.Unsafe;


class FileEntryStatus {

    private volatile int state = SUSPEND;

    boolean suspend() {
        return compareAndSetState(RUNNING, SUSPEND);
    }

    boolean pending() {
        boolean result = compareAndSetState(SUSPEND, PENDING);
        if (!result) {
            result = compareAndSetState(RUNNING, PENDING);
        }
        return result;
    }

    boolean running() {
        return compareAndSetState(PENDING, RUNNING);
    }

    boolean isSuspend() {
        return state == SUSPEND;
    }

    boolean isPending() {
        return state == PENDING;
    }

    public boolean isRunning() {
        return state == RUNNING;
    }

    private boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    private static final Unsafe unsafe = Unsafe.getUnsafe();

    private static final long stateOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                    (FileEntryStatus.class.getDeclaredField("state"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * 挂起态
     */
    private static final int SUSPEND = -1;

    /**
     * 等待态
     */
    private static final int PENDING = 0;

    /**
     * 运行态
     */
    private static final int RUNNING = 1;

}
