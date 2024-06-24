package com.kanven.practice.file.sched.executor;

import com.kanven.practice.file.extension.SpiMate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpiMate(name = "default")
public class ExecutorFilter implements Filter {

    private final Pattern pattern;

    public ExecutorFilter(String reg) {
        this.pattern = Pattern.compile(reg);
    }

    @Override
    public boolean filter(String line) {
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

}
