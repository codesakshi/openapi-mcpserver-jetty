package io.github.codesakshi.mcphub.mcpserver;

import java.util.Collection;
import java.util.Properties;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import io.github.codesakshi.mcphub.config.McpConfig;
import io.github.codesakshi.mcphub.provider.McpServerProvider;
import io.github.codesakshi.mcphub.provider.McpServerProvider.McpSseServerInstance;

public class McpJettyServer {

    public static void main(String[] args) throws Exception {

        String configFilePath = ( args.length > 0 ) ? args[0] : null;

        if( null == configFilePath ) {
            throw new Exception("Config file path is not specified.");
        }

        McpConfigReader configReader = new McpConfigReader();
        Properties properties = configReader.getConfigProperties(configFilePath);

        String serverName = configReader.getServerName(properties);
        String appContextPath = configReader.getAppContextPath(properties);
        int port = configReader.getAppPort(properties);

        Collection<McpConfig> mcpConfigs = configReader.readMcpConfigurations(properties);

        // Create a Server instance
        Server server = new Server();

        // Add a connector to listen on a port
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port); // Listen on port 8080
        server.addConnector(connector);

        // Create a ServletContextHandler to handle servlets
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(appContextPath); // Root context path
        server.setHandler(context);

        McpServerProvider mcpServerProvider = new McpServerProvider();
        for (McpConfig mcpConfig : mcpConfigs) {
            McpSseServerInstance mcpSseServer = mcpServerProvider.createMcpSseServerInstance(appContextPath, mcpConfig);
            context.addServlet(new ServletHolder(mcpSseServer.getServlet()), mcpSseServer.getPathSpec());
        }

        server.start();
        server.join();
    }

}
