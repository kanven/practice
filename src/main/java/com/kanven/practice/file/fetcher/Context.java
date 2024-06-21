package com.kanven.practice.file.fetcher;

import com.kanven.practice.file.fetcher.sched.Resources;

public class Context {

    private static final Context instance = new Context();

    private Resources<FileEntry> resources;

    private Context() {

    }

    public static Context getInstance() {
        return instance;
    }

    public Resources<FileEntry> getResources() {
        return resources;
    }

}
