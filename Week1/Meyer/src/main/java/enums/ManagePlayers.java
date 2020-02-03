package enums;

import assignment.meyer.Player;
import assignment.meyer.Sequence;
import java.util.stream.Collectors;

/**
 *
 * @author Henning
 */
public enum ManagePlayers {
    REMOVE {
        @Override
        public void action(Player player) {
            Sequence sequence = Sequence.getSequenceSingleton();
            sequence.setPlayerList(sequence
                    .getPlayerList()
                    .stream()
                    .filter(o -> !(o.equals(player)))
                    .collect(Collectors.toList()));
        }
    };

    public abstract void action(Player player);
}
