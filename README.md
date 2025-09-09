# OpenAPI MCP Jetty Server

A Jetty-based server implementation for testing the OpenAPI Model Context Protocol (MCP) Hub. This project provides a standalone server that can host multiple MCP services defined through OpenAPI specifications.

## Overview

The OpenAPI MCP Jetty Server is a Java-based implementation that allows you to run and test OpenAPI-defined services using the Model Context Protocol. It uses Jetty as the underlying server and supports configuration through properties files.

## Features

- Jetty-based HTTP server implementation
- Support for multiple MCP service configurations
- Configurable port and context path
- Log4j2 integration for logging
- Maven-based build system
- Java 17 compatibility

## Prerequisites

- Java 17 or higher
- Maven 3.8+ (for building)

## Building the Project

To build the project, run:

```bash
mvn clean package
```

This will create two JAR files in the `target` directory:
- `openapi-mcpserver-jetty-1.0.0.jar`: The basic JAR
- `openapi-mcpserver-jetty-1.0.0-executable.jar`: An executable JAR with all dependencies

## Running the Server

### Using the Executable JAR

```bash
java -jar target/openapi-mcpserver-jetty-1.0.0-executable.jar <config-file-path>
```

### Using the Scripts

Windows:
```bash
bin/petstore.cmd
```

Linux/Mac:
```bash
./bin/petstore.sh
```

## Configuration

The server requires a configuration file (either properties or YAML) that specifies:
- Server name
- Application context path
- Port number
- MCP service configurations

Example configuration files are provided in the `example` directory:
- `app-sample.properties`: Properties file format
- `app-sample.yaml`: YAML format

## Project Structure

```
mcp-server-jetty/
├── example/                  # Example configurations and scripts
│   ├── app-sample.properties
│   ├── app-sample.yaml
│   └── bin/
│       ├── petstore.cmd
│       └── petstore.sh
├── src/
│   └── main/
│       ├── java/            # Java source files
│       └── resources/       # Resource files
└── pom.xml                  # Maven build configuration
```

## Dependencies

- openapi-mcp-hub (1.0.1)
- Eclipse Jetty Server (12.1.0)
- Log4j2 (2.20.0)

## Usage
- Clone and build the project
- Run the project using script present in the `example\bin` folder. It will start mcp server using configuration file present in example folder.
- 


## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Developer

- **Name**: Anilal P S
- **Organization**: Code Sakshi
- **GitHub**: [codesakshi](https://github.com/codesakshi)
- **Email**: anilalps.dev@gmail.com

## Contributing

Issues and pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Links

- [GitHub Repository](https://github.com/codesakshi/simpleopenapi-mcpserver-jetty)
- [Issue Tracker](https://github.com/codesakshi/openapi-mcpserver-jetty/issues)
