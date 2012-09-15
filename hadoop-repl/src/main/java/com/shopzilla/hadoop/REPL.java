/**
 * Copyright (C) 2004 - 2012 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */

package com.shopzilla.hadoop;

import jline.Completor;
import jline.ConsoleReader;

import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Jeremy Lucas
 * @since 9/11/12
 */
public abstract class REPL {

    protected static final String  DEFAULT_PROMPT = "> ";

    private ConsoleReader consoleReader;

    public REPL() throws ExitSignal {
        try {
            consoleReader = new ConsoleReader();
        } catch (final Exception ex) {
            throw new ExitSignal(1, ex.getMessage());
        }
    }

    protected void addCompletors(final Completor... completors) {
        for (final Completor completor : completors) {
            consoleReader.addCompletor(completor);
        }
    }

    protected void removeAllCompletors() {
        final LinkedList completors = new LinkedList(consoleReader.getCompletors());
        for (final Object completor : completors) {
            consoleReader.removeCompletor((Completor) completor);
        }
    }

    protected void removeCompletors(final Completor... completors) {
        for (final Completor completor : completors) {
            consoleReader.removeCompletor(completor);
        }
    }

    public void loop() throws ExitSignal  {
        loop(DEFAULT_PROMPT);
    }

    public void loop(final String prompt) throws ExitSignal {
        while (true) {
            try {
                print(evaluate(read(prompt)));
            } catch (final IOException ex) {
                throw new ExitSignal(1, ex.getMessage());
            }
        }
    }

    protected String read(final String prompt) throws IOException {
        return consoleReader.readLine(prompt).trim();
    }

    abstract protected String evaluate(final String cmd) throws ExitSignal;

    protected void print(final String out) throws ExitSignal {
        if (out != null) System.out.println(out);
    }

    public static class ExitSignal extends RuntimeException {

        private final String message;
        private final int exitCode;

        public ExitSignal(final int exitCode, final String message) {
            this.exitCode = exitCode;
            this.message = message;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getMessage() {
            return message;
        }
    }
}