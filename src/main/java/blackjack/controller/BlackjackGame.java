package blackjack.controller;

import blackjack.domain.card.Deck;
import blackjack.domain.card.RandomDeck;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.BetAmount;
import blackjack.domain.participant.Round;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.PlayerAction;
import blackjack.domain.participant.Players;
import blackjack.domain.result.RoundResult;
import blackjack.domain.result.Pot;
import blackjack.domain.result.Referee;
import blackjack.view.InputView;
import blackjack.view.OutputView;
import java.util.List;
import java.util.function.Supplier;

public class BlackjackGame {
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() {
        Round round = createRoundWithDeck();
        Pot pot = createPot(round);
        outputView.printInitialHand(round);
        participantsHitCard(round);
        outputView.printParticipantsHandWithScore(round);
        printPotResult(round);
    }

    private Round createRoundWithDeck() {
        Deck deck = RandomDeck.getInstance();
        return retryOnException(() -> createRoundWithNames(deck));
    }

    private Round createRoundWithNames(Deck deck) {
        List<Name> playersName = readPlayersName();
        return new Round(playersName, deck);
    }

    private Pot createPot(Round round) {
        Players players = round.getPlayers();
        List<BetAmount> betAmounts = players.getPlayers().stream()
                .map(player -> retryOnException(() -> createBetAmount(player.getName())))
                .toList();
        return round.generatePot(betAmounts);
    }

    private BetAmount createBetAmount(String playerName) {
        String amount = inputView.readPlayerBetAmount(playerName);
        return new BetAmount(amount);
    }

    private List<Name> readPlayersName() {
        List<String> playersName = inputView.readPlayersName();
        return playersName.stream()
                .map(Name::new)
                .toList();
    }

    private void participantsHitCard(Round round) {
        playersHitCard(round.getPlayers());
        dealerHitCard(round.getDealer());
    }

    private void playersHitCard(Players players) {
        while (players.hasNext()) {
            hitIfCurrentPlayerWantCard(players);
        }
    }

    private void hitIfCurrentPlayerWantCard(Players players) {
        Player player = players.getPlayerAtOrder();
        PlayerAction playerAction = retryOnException(() -> getPlayerAction(player));
        if (playerAction == PlayerAction.HIT) {
            player.addCard(RandomDeck.getInstance());
        }
        outputView.printPlayerHand(player);
        players.increaseOrder(playerAction);
    }

    private PlayerAction getPlayerAction(Player player) {
        String playerName = player.getName();
        String command = inputView.readPlayerActionCommand(playerName);
        return PlayerAction.getAction(command);
    }

    private void dealerHitCard(Dealer dealer) {
        Deck deck = RandomDeck.getInstance();
        int hitCount = 0;
        while (dealer.canHit()) {
            dealer.addCard(deck);
            hitCount++;
        }
        outputView.printDealerHitCount(hitCount);
    }

    private void printPotResult(Round round) {
        Referee referee = Referee.getInstance();
        RoundResult roundResult = round.generateResult(referee);
        outputView.printRoundResult(roundResult);
    }

    private <T> T retryOnException(Supplier<T> operation) {
        try {
            return operation.get();
        } catch (IllegalArgumentException e) {
            outputView.printException(e);
            return retryOnException(operation);
        }
    }
}
