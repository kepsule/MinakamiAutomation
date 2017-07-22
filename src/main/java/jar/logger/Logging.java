package jar.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see src/main/resources logback.xml
 * @author Keisuke
 *
 */
public class Logging {

	private static final Logger logger =
			LoggerFactory.getLogger(Logging.class);

	public static void info(String message) {
		logger.info(message);
	}
	public static void debug(String message) {
		logger.debug(message);
	}
	public static void error(String message) {
		logger.error(message);
	}
	public static void trace(String message) {
		logger.trace(message);
	}
	public static void warn(String message) {
		logger.warn(message);
	}
}
