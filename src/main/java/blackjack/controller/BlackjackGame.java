package blackjack.controller;

import blackjack.domain.card.Deck;
import blackjack.domain.card.RandomDeck;
import blackjack.domain.participant.BetAmount;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.PlayerAction;
import blackjack.domain.participant.Players;
import blackjack.domain.participant.Round;
import blackjack.domain.result.HandResult;
import blackjack.domain.result.PlayersPots;
import blackjack.domain.result.Referee;
import blackjack.view.InputView;
import blackjack.view.OutputView;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BlackjackGame {
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public void run() {
        Deck deck = new RandomDeck();
        Round round = createRound(deck);
        PlayersPots playersPots = createPlayersPots(round);
        outputView.printInitialHand(round);
        participantsHitCard(round, deck);
        outputView.printParticipantsHandWithScore(round);
        printParticipantsPots(round, playersPots);
    }

    private Round createRound(Deck deck) {
        return retryOnException(() -> createRoundWithNames(deck));
    }

    private Round createRoundWithNames(Deck deck) {
        List<Name> playersName = readPlayersName();
        return new Round(playersName, deck);
    }

    private PlayersPots createPlayersPots(Round round) {
        Players players = round.getPlayers();
        List<BetAmount> betAmounts = players.getPlayers().stream()
                .map(player -> retryOnException(() -> createBetAmount(player.getName())))
                .toList();
        return round.generatePlayersPots(betAmounts);
    }

    private BetAmount createBetAmount(String playerName) {
        int amount = inputView.readPlayerBetAmount(playerName);
        return new BetAmount(amount);
    }

    private List<Name> readPlayersName() {
        List<String> playersName = inputView.readPlayersName();
        return playersName.stream()
                .map(Name::new)
                .toList();
    }

    private void participantsHitCard(Round round, Deck deck) {
        playersHitCard(round.getPlayers(), deck);
        dealerHitCard(round.getDealer(), deck);
    }

    private void playersHitCard(Players players, Deck deck) {
        while (players.hasNext()) {
            hitIfCurrentPlayerWantCard(players, deck);
        }
    }

    private void hitIfCurrentPlayerWantCard(Players players, Deck deck) {
        Player player = players.getPlayerAtOrder();
        PlayerAction playerAction = retryOnException(() -> getPlayerAction(player));
        if (playerAction.isHit()) {
            player.addCard(deck);
        }
        outputView.printPlayerHand(player);
        players.increaseOrder(playerAction);
    }

    private PlayerAction getPlayerAction(Player player) {
        String playerName = player.getName();
        String command = inputView.readPlayerActionCommand(playerName);
        return PlayerAction.getAction(command);
    }

    private void dealerHitCard(Dealer dealer, Deck deck) {
        int hitCount = 0;
        while (dealer.canHit()) {
            dealer.addCard(deck);
            hitCount++;
        }
        outputView.printDealerHitCount(hitCount);
    }

    private void printParticipantsPots(Round round, PlayersPots playersPots) {
        Referee referee = Referee.getInstance();
        Map<Player, HandResult> roundResult = round.generateResult(referee);
        playersPots = playersPots.calculatePlayersPots(roundResult);
        outputView.printAllPots(playersPots);
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
