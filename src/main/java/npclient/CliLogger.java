package npclient;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CliLogger extends Logger {

    protected CliLogger(String name) {
        super(name);
    }

    public static CliLogger get(Class<?> c) {
        Logger root = Logger.getRootLogger();

        CliLogger logger = new CliLogger(c.getName());

        logger.repository = root.getLoggerRepository();
        logger.parent = root;

        return logger;
    }

    private static final String FQCN = CliLogger.class.getName();

    @Override
    public void debug(Object message) {
        message = "[" + Thread.currentThread().getName() + "] " + message;
        if (!this.repository.isDisabled(10000)) {
            if (Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel())) {
                this.forcedLog(FQCN, Level.DEBUG, message, null);
            }
        }
    }

    @Override
    public void error(Object message) {
        message = "[" + Thread.currentThread().getName() + "] " + message;
        if (!this.repository.isDisabled(40000)) {
            if (Level.ERROR.isGreaterOrEqual(this.getEffectiveLevel())) {
                this.forcedLog(FQCN, Level.ERROR, message, null);
            }

        }
    }

    @Override
    public void info(Object message) {
        message = "[" + Thread.currentThread().getName() + "] " + message;
        if (!this.repository.isDisabled(20000)) {
            if (Level.INFO.isGreaterOrEqual(this.getEffectiveLevel())) {
                this.forcedLog(FQCN, Level.INFO, message, null);
            }
        }
    }
}
