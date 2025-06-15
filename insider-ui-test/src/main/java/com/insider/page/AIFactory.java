package com.insider.page;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.insider.ai.ActionItem;
import com.insider.provider.AppProvider;
import com.insider.provider.WebTestContextProvider;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AIFactory<P> {

  private Logger log() {
    return LoggerFactory.getLogger(AIFactory.class);
  }

  @SneakyThrows
  default <T extends PageActions> T ai(Class<T> page, String task) {
    ai(task);
    return AppProvider.getContext().getBean(page);
  }

  @SneakyThrows
  default P ai(String task) {
    var browser = WebTestContextProvider.get().getBrowser();
    browser.sleepSecond(3);

    var interactiveElements = getDomTree();

    String response = askLLM(interactiveElements, task);

    List<ActionItem> actionItems = new ObjectMapper().readValue(
        extractJsonArray(response),
        new TypeReference<>() {
        }
    );

    for (ActionItem actionItem : actionItems) {
      switch (actionItem.getAction()) {
        case SEND_KEYS:
          browser.sendKeys(
              browser.getRemoteWebDriver().findElement(By.xpath(actionItem.getXpath())),
              actionItem.getInput());
          break;
        case CLICK:
          browser.click(browser.getRemoteWebDriver().findElement(By.xpath(actionItem.getXpath())));
          break;
        case SELECT_BY_TEXT:
          var selectBox = browser.getRemoteWebDriver().findElement(By.xpath(actionItem.getXpath()));
          browser.selectByText(selectBox, actionItem.getInput());
          break;
        default:
          log().warn("Bilinmeyen aksiyon: {}", actionItem.getAction());
      }
    }

    return (P) this;
  }

  @SneakyThrows
  default String getDomTree() {
    var browser = WebTestContextProvider.get().getBrowser();

    var domTreeJs = AIFactory.class.getClassLoader().getResource("domTree.js");
    String domScript = Files.readString(Paths.get(Objects.requireNonNull(domTreeJs).toURI()));
    var interactiveElements = (ArrayList<Object>) ((JavascriptExecutor) browser.getRemoteWebDriver()).executeScript(
        "return ".concat(domScript));

    return new ObjectMapper().writeValueAsString(interactiveElements);
  }

  @SneakyThrows
  private String askLLM(String prompt, String task) {
    Client client = Client.builder()
        .apiKey(System.getenv("GEMINI_API_KEY"))
        .build();

    GenerateContentResponse response =
        client.models.generateContent("gemini-2.0-flash", prompt + String.format("""
            Given a JSON, respond with a JSON matching the template below based on the provided scenario. Return only valid JSON.
                                                         
            - The "actions" key should map to an array of action items, where each action item is an object with the following keys:
            - "action": one of CLICK, SEND_KEYS, SELECT_BY_TEXT, CHECKBOX_CHECKED, CLEAR, GO_BACK, GO_FORWARD
            - "xpath": a string representing the XPath value
            - "input": a string representing input data (if applicable)
                                                         
            Example template:
            {
              "actions": [
                {
                  "action": "ACTION",
                  "xpath": "XPATH_VALUE",
                  "input": "SOME_VALUE"
                }
              ]
            }
                                                         
            Scenario:
            %s
            """, task), null);
    return response.text();
  }

  private String extractJsonArray(String text) {
    int startIndex = text.indexOf("[");
    int endIndex = text.lastIndexOf("]");

    if (startIndex != -1 && endIndex != -1) {
      return text.substring(startIndex, endIndex + 1);
    }
    return null;
  }

}
