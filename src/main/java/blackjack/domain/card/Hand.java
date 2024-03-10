package blackjack.domain.card;

import java.util.Collections;
import java.util.List;

public class Hand {
    private static final int BLACKJACK = 21;
    private static final int ACE_ALTERNATIVE_SCORE = 10;

    private final List<Card> cards;

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public boolean isBust() {
        return getOptimizedScore() > BLACKJACK;
    }

    public boolean isBlackjack() {
        return getOptimizedScore() == BLACKJACK;
    }

    public int getOptimizedScore() {
        int cardTotalScore = getCardTotalScore();
        if (cardTotalScore >= BLACKJACK) {
            return cardTotalScore;
        }
        int aceCountForAlter = countAceForAlter();
        return aceCountForAlter * ACE_ALTERNATIVE_SCORE + cardTotalScore;
    }

    public boolean isTotalScoreGreaterThan(int score) {
        return getOptimizedScore() > score;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    private int getCardTotalScore() {
        return cards.stream()
                .mapToInt(Card::getScore)
                .sum();
    }

    private int countAceForAlter() {
        int aceCount = (int) cards.stream()
                .filter(Card::isAce)
                .count();
        int cardTotalScore = getCardTotalScore();
        int availableAceAlterCount = (BLACKJACK - cardTotalScore) / ACE_ALTERNATIVE_SCORE;
        return Math.min(aceCount, availableAceAlterCount);
    }
}
