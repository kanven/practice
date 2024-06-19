package com.kanven.practice.file.watcher;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.nio.file.Path;

@Data
public class Event {

    private Path parent;

    private Path child;

    private EventType type;

    public Event() {

    }

    public Event(EventType type, Path parent, Path child) {
        this.type = type;
        this.parent = parent;
        this.child = child;
    }

    public enum EventType {
        NEW,
        MODIFY,
        DELETED;
    }

}