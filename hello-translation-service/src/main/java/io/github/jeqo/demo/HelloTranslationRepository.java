package io.github.jeqo.demo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
class HelloTranslationRepository {
  private final Map<String, String> translationMap;

  HelloTranslationRepository() {
    translationMap = new HashMap<>();
    translationMap.put("es", "Hola");
    translationMap.put("it", "Ciao");
  }

  String translate(String lang) {
    return translationMap.getOrDefault(lang, "Hello");
  }
}
