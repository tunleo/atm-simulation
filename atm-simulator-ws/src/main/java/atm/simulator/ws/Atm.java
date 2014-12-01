package atm.simulator.ws;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Atm {
	public static void main(final String[] args) throws Exception {
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        final Server server = new Server(Integer.valueOf(webPort));
        final WebAppContext root = new WebAppContext();

        setLogLevel("org.eclipse.jetty");
        root.setContextPath("/atm-simulator-ws/");
        root.setParentLoaderPriority(true);

        final String webappDirLocation = "src/main/webapp/";
        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);

        server.setHandler(root);

        server.start();
        server.join();
    }
	
	public static void setLogLevel(String logName){
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(logName); 
		loggerConfig.setLevel(Level.INFO);
		ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
	}
}
