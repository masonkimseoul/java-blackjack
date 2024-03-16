package blackjack.domain.participant;

import blackjack.exception.InvalidHitCommandException;
import java.util.Arrays;

public enum PlayerAction {
    HIT("y"),
    STAND("n");

    private final String command;

    PlayerAction(String command) {
        this.command = command;
    }

    public static PlayerAction getAction(String command) {
        return Arrays.stream(values())
                .filter(playerAction -> playerAction.isMatch(command))
                .findFirst()
                .orElseThrow(InvalidHitCommandException::new);
    }

    private boolean isMatch(String command) {
        return this.command.equals(command);
    }

    public boolean isHit() {
        return this == HIT;
    }

    public boolean isStand() {
        return this == STAND;
    }
}
