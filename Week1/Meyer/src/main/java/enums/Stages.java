package enums;

import stragglers.Utils;
import assignment.meyer.Player;
import assignment.meyer.Sequence;
import static assignment.meyer.Sequence.getSequenceSingleton;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Henning
 */
public enum Stages {
    WELCOME {
        @Override
        public void action() {
            System.out.println(""
                    + "Welcome to Meyer"
                    + "\n"
                    + "1. Player Selection"
                    + "\n"
                    + "2. Help"
            );

            String option = Sequence.getSequenceSingleton().getScanner().nextLine();

            switch (option) {
                case ("1"):
                    Stages.PLAYERSELECTION.action();
                    break;
                case ("2"):
                    Help.helpAction();
                    break;
            }
        }
    },
    PLAYERSELECTION {
        @Override
        public void action() {
            Sequence sequence = Sequence.getSequenceSingleton();
            if (sequence.getPlayerList().size() < 2) {
                System.out.println("Name: \n");
                String option = sequence.getScanner().nextLine();

                System.out.println("You wrote: "
                        + option
                        + "\n"
                        + "Is this the correct name?"
                        + "\n"
                        + "1. Yes "
                        + "\n"
                        + "2. No"
                        + "\n"
                        + "3. See current players"
                        + "\n"
                        + "4. Go back"
                        + "\n"
                        + "\n");

                String boolOption = sequence.getScanner().nextLine();
                switch (boolOption) {
                    case ("1"):
                        Player newPlayer = new Player(option);

                        System.out.println(""
                                + "Do you want player to be controlled by a person or a computer?"
                                + "\n"
                                + "\n"
                                + "1. Human"
                                + "\n"
                                + "2. Computer"
                                + "\n"
                                + "\n"
                        );
                        String optionHumanComputer = sequence.getScanner().nextLine();
                        switch (optionHumanComputer) {
                            case ("1"):
                                newPlayer.setPlayerType("HUMAN");
                                List<Player> playerList1 = sequence.getPlayerList();
                                playerList1.add(newPlayer);
                                sequence.setPlayerList(playerList1);
                                this.action();
                                break;
                            case ("2"):
                                newPlayer.setPlayerType("COMPUTER");
                                List<Player> playerList2 = sequence.getPlayerList();
                                playerList2.add(newPlayer);
                                sequence.setPlayerList(playerList2);
                                this.action();
                                break;
                            default:
                                Utils.getUtilsSingleton().defaultErrorMessage(optionHumanComputer);
                                this.action();
                                break;
                        }
                        break;
                    case ("2"):
                        this.action();
                        break;

                    case ("3"):
                        sequence.getPlayerList().forEach(player -> {
                            System.out.println(player.getName());
                        });
                        Utils.getUtilsSingleton().pressAnyKey();
                        System.out.println("\n\n\n\n");
                        this.action();
                        break;

                    case ("4"):
                        WELCOME.action();
                        break;
                    default:
                        Utils.getUtilsSingleton().defaultErrorMessage(boolOption);
                        this.action();
                        break;
                }
            } else {
                sequence.setCurrentPlayer(sequence.getPlayerList().get(0));
            }
        }
    },
    ROLL {
        @Override
        public void action() {
            Sequence sequence = Sequence.getSequenceSingleton();
            System.out.println(""
                    + "It's " + sequence.getCurrentPlayer().getName() + "'s turn"
                    + "\n"
                    + sequence.getCurrentPlayer().getName() + " currently has " + sequence.getCurrentPlayer().getLife() + " hp left"
                    + "\n"
                    + "\n"
                    + "1. Roll"
                    + "\n"
                    + "2. Current highest score and rolls to beat"
                    + "\n"
            );

            String option;
            if (sequence.getCurrentPlayer().getPlayerType().equals("HUMAN")) {
                option = sequence.getScanner().nextLine();
            } else {
                option = "1";
            }
            switch (option) {
                case ("1"):
                    Actions.ROLL.action();
                    break;
                case ("2"):
                    System.out.println("\n"
                            +"The highest roll was: " + RollTypes.reverseValueEnumNameExtraction(Optional.empty())
                            +"\n\n"
                            +"The rolls that beat this score are: \n");
                    RollTypes.rollsToBeat().stream().forEach(o->{
                        System.out.println(o);
                    });
                    System.out.println("");
                    this.action();
                    break;
                default:
                    Utils.getUtilsSingleton().defaultErrorMessage(option);
                    this.action();
                    break;
            }
        }
    },
    GUESS {
        @Override
        public void action() {
            Actions.GUESS.action();
        }
    },
    END_TURN {
        @Override
        public void action() {
            Sequence sequence = Sequence.getSequenceSingleton();
            int index = sequence.getPlayerList().indexOf(sequence.getCurrentPlayer());
            sequence.setCurrentPlayer(
                    sequence.getPlayerList().size() == index + 1
                    ? sequence.getPlayerList().get(0) : sequence.getPlayerList().get(index + 1));
            sequence.setTurn(sequence.getTurn()+1);
        }
    },
    PLAYER_ELIMINATION {
        @Override
        public void action() {
            System.out.println(""
                    + "\n\n"
                    + getSequenceSingleton().getCurrentPlayer().getName() + " has been eliminated!"
                    + "\n\n"
                    + "Press any key to continue");
            ManagePlayers.REMOVE.action(getSequenceSingleton().getCurrentPlayer());
            getSequenceSingleton().getScanner().nextLine();
        }
    },
    GAMEOVER {
        @Override
        public void action() {
            System.out.println(""
                    + "\n\n"
                    + "_____________"
                    + "\n"
                    + "GAME OVER - " + getSequenceSingleton().getPlayerList().get(0).getName() + " Wins!"
                    + "\n\n"
                    + "Thanks for playing!");
        }
    };

    public abstract void action();
}
