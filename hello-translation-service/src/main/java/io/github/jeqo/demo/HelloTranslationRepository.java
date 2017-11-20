package io.github.jeqo.demo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class HelloTranslationRepository {
  private final Map<String, String> translationMap;

  public HelloTranslationRepository() {
    translationMap = new HashMap<>();
    translationMap.put("es", "Hola");
    translationMap.put("it", "Ciao");
  }

  public String translate(String lang) {
    return translationMap.getOrDefault(lang, "Hello");
  }
}
