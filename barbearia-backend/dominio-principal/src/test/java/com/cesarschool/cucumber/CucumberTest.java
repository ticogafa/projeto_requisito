package com.cesarschool.cucumber;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/cesarschool/resources/agendamento.feature",
    glue = "com.cesarschool",
    plugin = {"pretty", "html:target/cucumber-report.html"},
    monochrome = true,
    snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class CucumberTest {
}