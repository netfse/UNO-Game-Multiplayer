package Strategic;

import Player.*;
import java.util.ArrayList;
import Card.Card;

public interface Strategic {
    Card chooseCards(Player player, ArrayList<Card> cards, Card cardOnTop);

    Boolean callUNO(Player player, int timeLimit);
}
