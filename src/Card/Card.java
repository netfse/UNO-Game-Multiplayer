package Card;

import java.util.HashMap;
import java.util.Map;

import CardManager.CardManager;

public class Card {

  private final String name;
  private String color;

  public Card(String _name, String _color) {
    name = _name;
    color = _color;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String _color) {
    color = _color;
  }

  public void useAction(Card card) {
    CardManager cardManager = CardManager.getInstance();
    cardManager.setTopCard(card);
  }

  private static final Map<String, String> COLOR_MAP = new HashMap<>();

  static {
    COLOR_MAP.put("Red", "\u001B[31m");
    COLOR_MAP.put("Blue", "\u001B[34m");
    COLOR_MAP.put("Green", "\u001B[32m");
    COLOR_MAP.put("Yellow", "\u001B[33m");
  }

  public String toString() {

    String colorCode = COLOR_MAP.getOrDefault(
        color,
        "");

    return String.format("%s[%s %s]\u001B[0m", colorCode, name, color);
  }

  public boolean sameColor(String colorInput) {
    return color.equals("wild") || colorInput.equals("wild") || color.equals(colorInput);
  }

  public boolean sameName(String nameInput) {
    return name.equals(nameInput);
  }
}