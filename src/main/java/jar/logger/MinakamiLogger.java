package jar.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ロガー
 * @see src/main/resources logback.xml
 * @author Keisuke
 *
 */
public class MinakamiLogger {

	private static final Logger logger =
			LoggerFactory.getLogger(MinakamiLogger.class);

	public static void info(String message) {
		logger.info(message);
	}
	public static void debug(String message) {
		logger.debug(message);
	}
	public static void error(String message) {
		logger.error(message);
	}
	public static void error(String message, Throwable t) {
		logger.error(message, t);
	}
	public static void trace(String message) {
		logger.trace(message);
	}
	public static void warn(String message) {
		logger.warn(message);
	}
}
