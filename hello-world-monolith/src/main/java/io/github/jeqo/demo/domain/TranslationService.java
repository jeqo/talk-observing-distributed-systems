package io.github.jeqo.demo.domain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TranslationService {
  private final Map<String, String> translationMap;

  public TranslationService() {
    translationMap = new HashMap<>();
    translationMap.put("es", "Hola");
    translationMap.put("it", "Ciao");
  }

  public String translate(String lang) {
    return translationMap.getOrDefault(lang, "Hello");
  }
}
