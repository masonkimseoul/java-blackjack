package blackjack.domain.card;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.testutil.CustomDeck;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandGeneratorTest {
    @DisplayName("카드를 2장 가진 Hand를 생성한다.")
    @Test
    void generateHand() {
        //given
        List<Number> numbers = List.of(Number.ACE, Number.EIGHT);
        List<Shape> shapes = List.of(Shape.SPADE, Shape.CLOVER);
        Deck deck = new CustomDeck(numbers, shapes);
        HandGenerator handGenerator = new HandGenerator(deck);

        //when
        Hand hand = handGenerator.generate();

        //then
        assertThat(hand.getCards().size()).isEqualTo(2);
    }
}
