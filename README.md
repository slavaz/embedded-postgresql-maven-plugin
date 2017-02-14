# embedded-postgresql-maven-plugin
[![Build status](https://travis-ci.org/slavaz/embedded-postgresql-maven-plugin.svg?branch=master)](https://travis-ci.org/slavaz/embedded-postgresql-maven-plugin/)

## Description

Embedded PostgreSQL Maven Plugin provides a platform neutral way for running postgres server in integration tests.
This plugin  is based on [Embedded PostgreSQL Server library](https://github.com/yandex-qatools/postgresql-embedded)

## Usage example

Starts PostgreSQL server, creates a database and a user with specified password

    ...
    <build>
        ..
        <plugins>
          <plugin>
            <groupId>@project.groupId@</groupId>
            <artifactId>@project.artifactId@</artifactId>
            <version>@project.version@</version>
            <configuration>
              <pgServerVersion>9.5</pgServerVersion>
              <pgServerPort>15432</pgServerPort>
              <dbName>testdb</dbName>
              <userName>testuser</userName>
              <password>userpass</password>
            </configuration>
            <executions>
              <execution>
                <id>start-pgsql</id>
                <phase>pre-integration-test</phase>
                <goals>
                  <goal>start</goal>
                </goals>
              </execution>
              <execution>
                <id>stop-pgsql</id>
                <phase>post-integration-test</phase>
                <goals>
                  <goal>stop</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
    </build>


## Supported versions

The plugin currently supports next versions: 9.3.15, 9.4.10, 9.5.5, 9.6.1

You also may use aliases:
9.3 -> 9.3.15
9.4 -> 9.4.10
9.5 -> 9.5.5
9.6 -> 9.6.1
latest -> 9.6

## Goals
                  
1. `start` -- start the server
2. `stop` -- stop the server
