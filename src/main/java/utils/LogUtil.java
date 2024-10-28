package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    private static final Logger logger = LogManager.getLogger(LogUtil.class);

    public static void debug(String message) {
        logger.debug(getCallingClass() + ": " + message);
    }

    public static void info(String message) {
        logger.info(getCallingClass() + ": " + message);
    }

    public static void warn(String message) {
        logger.warn(getCallingClass() + ": " + message);
    }

    public static void error(String message, Throwable t) {
        logger.error(getCallingClass() + ": " + message, t);
    }

    public static void fatal(String message, Throwable t) {
        logger.fatal(getCallingClass() + ": " + message, t);
    }

    private static String getCallingClass() {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames.skip(2).findFirst()) // Пропускаем 2 кадра: один для LogUtil и один для самого метода
                .map(StackWalker.StackFrame::getClassName)
                .orElse("Unknown class");
    }
}
