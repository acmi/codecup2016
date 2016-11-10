/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.spi;

import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;

public class DefaultRepositorySelector
implements RepositorySelector {
    final LoggerRepository repository;

    public DefaultRepositorySelector(LoggerRepository loggerRepository) {
        this.repository = loggerRepository;
    }

    public LoggerRepository getLoggerRepository() {
        return this.repository;
    }
}

