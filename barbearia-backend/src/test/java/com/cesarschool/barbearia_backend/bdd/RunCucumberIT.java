package com.cesarschool.barbearia_backend.bdd;

import org.junit.platform.suite.api.*;
import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features") // arquivos .feature em src/test/resources/features
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.cesarschool.barbearia_backend.bdd")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report.html, json:target/cucumber.json")
public class RunCucumberIT { }
