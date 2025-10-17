package com.cesarschool.cucumber;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/gestaoDeProfissionais.feature",
    glue = "com.cesarschool.cucumber.gestaoProfissionais",
    plugin = {"pretty", "html:target/cucumber-report.html"},
    monochrome = true,
    snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class CucumberTest {
}