package logging;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogHandler {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final LogManager lgmngr = LogManager.getLogManager();

	public static void setup() throws IOException {
		LOGGER.log(Level.INFO, "My first Log Message");
	}

	public static void log(String message) {
		LOGGER.log(Level.INFO, message);
	}
}
