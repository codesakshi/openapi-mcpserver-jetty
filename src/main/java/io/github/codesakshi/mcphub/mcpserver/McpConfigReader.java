package io.github.codesakshi.mcphub.mcpserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

import io.github.codesakshi.mcphub.config.McpConfig;

public class McpConfigReader {

    public Properties getConfigProperties(String configFilePath) {

        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(configFilePath)) {

            if (configFilePath.endsWith(".yaml") || configFilePath.endsWith(".yml")) {

                Yaml yaml = new Yaml();
                Map<String, Object> yamlMap = yaml.load(inputStream);

                convertYamlToProperties(properties, yamlMap, "");

            } else if (configFilePath.endsWith(".properties")) {

                properties.load(inputStream);
            } else {
                throw new IllegalArgumentException("Unsupported config file format");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read config file", e);
        }

        return properties;
    }

    private static void convertYamlToProperties(Properties target, Object parent, String prefix) {

        if (null != parent) {

            if (parent instanceof Map) {

                Map<String, Object> yamlMap = (Map<String, Object>) parent;

                for (Map.Entry<String, Object> entry : yamlMap.entrySet()) {
                    String newPrefix = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
                    convertYamlToProperties(target, entry.getValue(), newPrefix);
                }

            } else if (parent instanceof Iterable) {

                Iterable<Object> yamlList = (Iterable<Object>) parent;
                int index = 0;
                for (Object item : yamlList) {

                    String newPrefix = prefix.isEmpty() ? "" : prefix;
                    newPrefix +=  index == 0 ? "" :  "[" + index + "]";

                    convertYamlToProperties(target, item, newPrefix);
                    index++;
                }

            } else {

                target.put(prefix, parent.toString());
            }
        }
    }

    public String getServerName(Properties properties) {
        return properties.getProperty("mcp-server.server-name", "default-server");
    }

    public String getAppContextPath(Properties properties) {
        return properties.getProperty("mcp-server.app-context-path", "/");
    }

    public int getAppPort(Properties properties) {
        return Integer.parseInt(properties.getProperty("mcp-server.app-port", "8080"));
    }

    public Collection<McpConfig> readMcpConfigurations(Properties properties) {

        Map<String, McpConfig> configMap = new HashMap<>();
        String prefix = "mcp-server.openapi";

        McpConfig mcpConfig = readMcpConfigurations(properties, prefix);
        if (mcpConfig != null) {
            if (configMap.containsKey(mcpConfig.getMcpServerName())) {
                throw new IllegalArgumentException(
                        "Duplicate MCP server name found: " + mcpConfig.getMcpServerName());
            } else {
                configMap.put(mcpConfig.getMcpServerName(), mcpConfig);
            }
        }

        prefix = "mcp-server.openapi[0]";

        mcpConfig = readMcpConfigurations(properties, prefix);
        if (mcpConfig != null) {
            if (configMap.containsKey(mcpConfig.getMcpServerName())) {
                throw new IllegalArgumentException(
                        "Duplicate MCP server name found: " + mcpConfig.getMcpServerName());
            } else {
                configMap.put(mcpConfig.getMcpServerName(), mcpConfig);
            }
        }

        for (int i = 1;; i++) {
            prefix = "mcp-server.openapi[" + i + "]";

            mcpConfig = readMcpConfigurations(properties, prefix);
            if (null == mcpConfig) {
                break; // NO more mcp config
            }

            if (configMap.containsKey(mcpConfig.getMcpServerName())) {
                throw new IllegalArgumentException(
                        "Duplicate MCP server name found: " + mcpConfig.getMcpServerName());
            } else {
                configMap.put(mcpConfig.getMcpServerName(), mcpConfig);
            }
        }

        return configMap.values();
    }

    private McpConfig readMcpConfigurations(Properties properties, String prefix) {

        String serverName = properties.getProperty(prefix + ".server-name");
        if (null == serverName || serverName.isEmpty()) {
            return null;
        }

        McpConfig mcpConfig = new McpConfig();
        mcpConfig.setMcpServerName(serverName);

        mcpConfig.setMcpServerVersion(properties.getProperty(prefix + ".server-version"));
        mcpConfig.setServletPathSpec(properties.getProperty(prefix + ".servlet-path-spec", "/"));
        mcpConfig.setMcpSseEndPoint(properties.getProperty(prefix + ".sse-endpoint", "sse"));
        mcpConfig.setApiDocLocation(properties.getProperty(prefix + ".api-doc-location"));
        mcpConfig.setApiHostUrl(properties.getProperty(prefix + ".api-host-url"));
        mcpConfig.setApiServerIndex(Integer.parseInt(properties.getProperty(prefix + ".api-server-index", "0")));
        mcpConfig.setOptimizeSchema(Boolean.parseBoolean(properties.getProperty(prefix + ".optimize-schema", "true")));
        mcpConfig.setApiAuthorization(properties.getProperty(prefix + ".api-authorization"));

        mcpConfig.setApiConnectionTimeoutMs(
                Integer.parseInt(properties.getProperty(prefix + ".api-connection-timeout-ms", "30000")));
        mcpConfig.setApiReadTimeoutMs(
                Integer.parseInt(properties.getProperty(prefix + ".api-read-timeout-ms", "10000")));
        mcpConfig.setApiMaxRetries(Integer.parseInt(properties.getProperty(prefix + ".api-max-retries", "3")));
        mcpConfig.setApiRetryDelayMs(Integer.parseInt(properties.getProperty(prefix + ".api-retry-delay-ms", "100")));

        // Populate mcpConfig fields from config map using the prefix
        return mcpConfig;
    }

}
