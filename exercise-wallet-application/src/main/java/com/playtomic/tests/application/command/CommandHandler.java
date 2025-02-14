package com.playtomic.tests.application.command;

public interface CommandHandler<C extends Command, R> {
    R handle(C command);
}
