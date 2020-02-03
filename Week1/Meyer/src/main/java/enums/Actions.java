package enums;

import stragglers.Utils;
import assignment.meyer.Player;
import assignment.meyer.Sequence;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author Henning
 */
public enum Actions {
    ROLL {
        @Override
        public void action() {
            Random random = new Random();
            Sequence sequence = Sequence.getSequenceSingleton();
            int insertion1;
            int insertion2;

            if (sequence.getDiceHistory().containsKey(sequence.getTurn())) {
                int[] alreadyRolledValues = sequence.getDiceHistory()
                        .get(sequence.getTurn())
                        .entrySet()
                        .stream()
                        .findFirst()
                        .get()
                        .getValue();
                insertion1 = alreadyRolledValues[0];
                insertion2 = alreadyRolledValues[1];
            } else {
                insertion1 = random.nextInt(6) + 1;
                insertion2 = random.nextInt(6) + 1;
            }
            int[] rolls = {insertion1, insertion2};
            boolean underHighestScore = false;

            System.out.println(""
                    + "You Rolled: " + rolls[0] + " and " + rolls[1] + "\n"
                    + "AKA " + RollTypes.reverseValueEnumNameExtraction(Optional.of(rolls))
                    + "\n"
            );
            if (!(sequence.getDiceHistory().isEmpty())) {
                if (RollTypes.valueTranslation(rolls) < sequence.getCurrentTurnHighestValue()) {
                    underHighestScore = true;
                    System.out.println("Warning: Your roll is below the threshold. You have to bluff in order to not lose HP");
                } else if (RollTypes.valueTranslation(rolls) == sequence.getCurrentTurnHighestValue()) {
                    System.out.println("Your roll matches the highest roll");
                } else {
                    System.out.println("Your roll is above the threshold");
                }
            }
            System.out.print(""
                    + "1. Accept Roll"
            );
            if (underHighestScore == true) {
                System.out.print(". It's part of my master plan");
            }
            System.out.println("\n"
                    + "2. Bluff");
            sequence.addToDiceHistory(rolls);
            String option = sequence.getScanner().nextLine();

            switch (option) {
                case ("1"):
                    break;
                case ("2"):
                    BLUFF.action();
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
            Sequence sequence = Sequence.getSequenceSingleton();
            Player opposition = sequence.getPlayerList().stream().filter(o -> !o.equals(sequence.getCurrentPlayer())).collect(Collectors.toList()).get(0);
            int[] recognizedRoll;
            boolean underHighestScore = false;

            //Can't assume that a key exists when checking or it will break. This is a bit ugly.
            //Limit else scope.
            if (sequence.getBluffHistory().containsKey(sequence.getTurn())) {
                if (sequence.getBluffHistory().get(sequence.getTurn()).containsKey(sequence.getCurrentPlayer())) {
                    recognizedRoll = sequence.getBluffHistory().get(sequence.getTurn()).get(sequence.getCurrentPlayer());
                } else {
                    recognizedRoll = sequence.getDiceHistory().get(sequence.getTurn()).get(sequence.getCurrentPlayer());
                }
            } else {
                recognizedRoll = sequence.getDiceHistory().get(sequence.getTurn()).get(sequence.getCurrentPlayer());
            }

            //Looked for a better way at doing this. Couldn't find anything. Decided to keep it simple.
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

            System.out.println(""
                    + "Greetings " + opposition.getName()
                    + "\n\n"
                    + "Your opponent " + sequence.getCurrentPlayer().getName() + " claims to have rolled: " + recognizedRoll[0] + " and " + recognizedRoll[1]
                    + "\n"
                    + "AKA: " + RollTypes.reverseValueEnumNameExtraction(Optional.of(recognizedRoll))
                    + "\n");

            if (!(sequence.getDiceHistory().isEmpty())) {
                if (RollTypes.valueTranslation(recognizedRoll) < sequence.getCurrentTurnHighestValue()) {
                    System.out.println("This roll is below the threshold...Your opponent is being weird");
                    underHighestScore = true;
                } else if (RollTypes.valueTranslation(recognizedRoll) == sequence.getCurrentTurnHighestValue()) {
                    System.out.println("This roll matches the highest roll");
                } else {
                    System.out.println("This roll is above the threshold");
                }
            }

            System.out.println(""
                    + "\n"
                    + "1. Accept claim and take turn"
                    + "\n"
                    + "2. The claim is a lie"
                    + "\n"
            );

            String optionClaim = sequence.getScanner().nextLine();
            switch (optionClaim) {
                case ("1"):
                    if (underHighestScore == true) {
                        System.out.println(""
                                + sequence.getCurrentPlayer().getName() + " lost 1 HP for being below the threshold. "
                                + "\n");
                        sequence.getCurrentPlayer().setLife(sequence.getCurrentPlayer().getLife() - 1);
                        lifeLossVariableReset();
                    }
                    bluffRewriteHistory();
                    break;
                case ("2"):
                    bluffValidation(opposition);
                    break;
                default:
                    Utils.getUtilsSingleton().defaultErrorMessage(optionClaim);
                    this.action();
                    break;
            }
        }
    },
    BLUFF {
        @Override
        public void action() {
            Sequence sequence = Sequence.getSequenceSingleton();
            System.out.println(""
                    + "You*ve chosen to bluff"
                    + "\n"
                    + "Please confirm"
                    + "\n"
                    + "\n"
                    + "1. Confirm "
                    + "\n"
                    + "2. Nevermind "
                    + "\n"
            );
            String optionConfirm = sequence.getScanner().nextLine();
            switch (optionConfirm) {
                case ("1"):
                    System.out.println(""
                            + "Pick your poison"
                            + "\n"
                            + "\n"
                            + "1. Meyer"
                            + "\n"
                            + "2. Lille-Meyer"
                            + "\n"
                            + "3. Par 6"
                            + "\n"
                            + "4. Par 5"
                            + "\n"
                            + "5. Par 4/3/2/1"
                            + "\n"
                            + "6. 6+5"
                            + "\n"
                            + "7. 6+4"
                            + "\n"
                            + "8. Something else"
                    );
                    String optionPoison = sequence.getScanner().nextLine();
                    switch (optionPoison) {
                        case ("1"):
                            sequence.addToBluffHistory(RollTypes.MEYER.roll());
                            break;
                        case ("2"):
                            sequence.addToBluffHistory(RollTypes.LILLE_MEYER.roll());
                            break;
                        case ("3"):
                            sequence.addToBluffHistory(RollTypes.PAR6.roll());
                            break;
                        case ("4"):
                            sequence.addToBluffHistory(RollTypes.PAR5.roll());
                            break;
                        case ("5"):
                            sequence.addToBluffHistory(RollTypes.PAR_OTHER.roll());
                            break;
                        case ("6"):
                            sequence.addToBluffHistory(RollTypes.ROLL65.roll());
                            break;
                        case ("7"):
                            sequence.addToBluffHistory(RollTypes.ROLL64.roll());
                            break;
                        case ("8"):
                            System.out.println(""
                                    + "You do know that this option makes literally no sense at all, right?"
                                    + "\n"
                                    + "1. You're not my mom. You can't tell me what to do"
                                    + "\n"
                                    + "2. Oops"
                                    + "\n"
                            );
                            String optionStupid = sequence.getScanner().nextLine();
                            switch (optionStupid) {
                                case ("1"):
                                    sequence.addToBluffHistory(RollTypes.OTHERS.roll());
                                    break;
                                case ("2"):
                                    this.action();
                                    break;
                                default:
                                    Utils.getUtilsSingleton().defaultErrorMessage(optionStupid);
                                    this.action();
                                    break;
                            }
                            break;
                        default:
                            Utils.getUtilsSingleton().defaultErrorMessage(optionPoison);
                            this.action();
                            break;
                    }
                    break;
                case ("2"):
                    break;
                default:
                    Utils.getUtilsSingleton().defaultErrorMessage(optionConfirm);
                    this.action();
                    break;
            }
        }
    },
    //Returning to past action?
    HELP {
        @Override
        public void action() {
            Help.helpAction();
        }
    };

    private static void bluffRewriteHistory() {
        Sequence sequence = Sequence.getSequenceSingleton();
        if (sequence.getBluffHistory().containsKey(sequence.getTurn())) {
            Map<Integer, Map<Player, int[]>> switcheroo = sequence.getDiceHistory();
            switcheroo.replace(sequence.getTurn(), sequence.getBluffHistory().get(sequence.getTurn()));
            sequence.setDiceHistory(switcheroo);
        }
    }

    private static void lifeLossVariableReset() {
        Sequence sequence = Sequence.getSequenceSingleton();
        sequence.setBluffHistory(new HashMap<>());
        sequence.setDiceHistory(new HashMap<>());
        sequence.setCurrentTurnHighestValue(0);
        sequence.setTurn(-1);
    }

    private static void bluffValidation(Player opposition) {
        Sequence sequence = Sequence.getSequenceSingleton();
        if (sequence.getBluffHistory().containsKey(sequence.getTurn())) {
            if (sequence.getBluffHistory().get(sequence.getTurn()).containsKey(sequence.getCurrentPlayer())) {
                sequence.getCurrentPlayer().setLife(sequence.getCurrentPlayer().getLife() - 1);
                System.out.println(""
                        + "\n"
                        + opposition.getName() + " claims that " + sequence.getCurrentPlayer().getName() + " is bluffing"
                        + "\n\n"
                        + "Press any key to continue"
                );
                sequence.getScanner().nextLine();
                System.out.println(""
                        + "\n"
                        + "It was a bluff"
                        + "\n"
                        + sequence.getCurrentPlayer().getName()
                        + " loses 1 hp");
                System.out.println("\n\n"
                        + "Press any key to continue");
                sequence.getScanner().nextLine();
            } else {
                opposition.setLife(opposition.getLife() - 1);
                System.out.println(""
                        + "\n"
                        + opposition.getName() + " claims that " + sequence.getCurrentPlayer().getName() + " is bluffing"
                        + "\n\n"
                        + "Press any key to continue"
                );
                sequence.getScanner().nextLine();
                System.out.println("It's a miss. "
                        + "\n"
                        + opposition.getName()
                        + " loses 1 hp"
                        + "\n\n"
                        + "Press any key to continue");
                sequence.getScanner().nextLine();
            }
        } else {
            opposition.setLife(opposition.getLife() - 1);
            System.out.println(""
                    + "\n"
                    + opposition.getName() + " claims that " + sequence.getCurrentPlayer().getName() + " is bluffing"
                    + "\n\n"
                    + "Press any key to continue"
            );
            sequence.getScanner().nextLine();
            System.out.println("It's a miss. "
                    + "\n"
                    + opposition.getName()
                    + " loses 1 hp"
                    + "\n\n"
                    + "Press any key to continue");
            sequence.getScanner().nextLine();
        }
        lifeLossVariableReset();
    }

    public abstract void action();
}
