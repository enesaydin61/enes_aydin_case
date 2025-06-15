package com.insider.ai;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionItem {

  private Action action;
  private String xpath;
  private String input;

}


