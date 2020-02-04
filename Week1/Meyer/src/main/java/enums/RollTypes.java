package enums;

import assignment.meyer.Sequence;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author Henning
 */
public enum RollTypes{
    MEYER {
        @Override
        public int[] roll() {
            int[] result = {1, 2};
            return result;
        }

        @Override
        public int getTranslatedValue() {
            return 7;
        }
    },
    LILLE_MEYER {
        @Override
        public int[] roll() {
            int[] result = {1, 3};
            return result;
        }

        @Override
        public int getTranslatedValue() {
            return 6;
        }
    },
    PAR6 {
        @Override
        public int[] roll() {
            int[] result = {6, 6};
            return result;
        }

        @Override
        public int getTranslatedValue() {
            return 5;
        }
    },
    PAR5 {
        @Override
        public int[] roll() {
            int[] result = {5, 5};
            return result;
        }

        @Override
        public int getTranslatedValue() {
            return 4;
        }
    },
    PAR_OTHER {
        @Override
        public int[] roll() {
            Random random = new Random();
            int[] result = {0,0};
            switch (random.nextInt(3)) {
                case (0):
                    result[0] = 1;
                    result[1] = 1;
                    break;
                case (1):
                    result[0] = 2;
                    result[1] = 2;
                    break;
                case (2):
                    result[0] = 3;
                    result[1] = 3;
                    break;
                case (3):
                    result[0] = 4;
                    result[1] = 4;
                    break;
                default:
                    System.out.println("This is not supposed to happen");
                    break;
            }
            return result;
        }

        @Override
        public int getTranslatedValue() {
            return 3;
        }
    },
    ROLL65 {
        @Override
        public int[] roll() {
            int[] result = {6, 5};
            return result;
        }

        @Override
        public int getTranslatedValue() {
            return 2;
        }
    },
    ROLL64 {
        @Override
        public int[] roll() {
            int[] result = {6, 4};
            return result;
        }

        @Override
        public int getTranslatedValue() {
            return 1;
        }
    },
    OTHERS {
        @Override
        public int[] roll() {
            Random random = new Random();
            int[] result = {random.nextInt(5) + 1, random.nextInt(5) + 1};
            if (valueTranslation(result) != 0) {
                this.roll();
            }
            return result;
        }

        @Override
        public int getTranslatedValue() {
            return 0;
        }
    };

    public static List<String> rollsToBeat() {
        Sequence sequence = Sequence.getSequenceSingleton();
        if (sequence.getDiceHistory().containsKey(sequence.getTurn() - 1)) {
            highestValueUpdate();
            return rollsToBeatValueExtraction(sequence.getCurrentTurnHighestValue());
        }
        return rollsToBeatValueExtraction(0);
    }

    private static List<String> rollsToBeatValueExtraction(int rollTranslatedValue) {
        return EnumSet.allOf(RollTypes.class)
                .stream()
                .filter(o -> o.getTranslatedValue() >= rollTranslatedValue)
                .map(o -> o.name())
                .collect(Collectors.toList());
    }

    public static void highestValueUpdate() {
        Sequence sequence = Sequence.getSequenceSingleton();
        sequence
                .getDiceHistory()
                .entrySet()
                .stream()
                .forEach(o -> o.getValue()
                .entrySet()
                .stream()
                .forEach(j -> {
                    int insider = sequence.getCurrentTurnHighestValue()
                            < valueTranslation(j.getValue())
                            ? valueTranslation(j.getValue()) : sequence.getCurrentTurnHighestValue();
                    sequence.setCurrentTurnHighestValue(insider);
                }));
    }

    public static int valueTranslation(int[] diceRoll) {
        int score = 0;
        if ((diceRoll[0] == 1 && diceRoll[1] == 2) || diceRoll[0] == 2 && diceRoll[1] == 1) {
            score = MEYER.getTranslatedValue();
        } else if ((diceRoll[0] == 1 && diceRoll[1] == 3) || (diceRoll[0] == 3 && diceRoll[1] == 1)) {
            score = LILLE_MEYER.getTranslatedValue();
        } else if (diceRoll[0] == 6 && diceRoll[1] == 6) {
            score = PAR6.getTranslatedValue();
        } else if (diceRoll[0] == 5 && diceRoll[1] == 5) {
            score = PAR5.getTranslatedValue();
        } else if (diceRoll[0] == diceRoll[1]) {
            score = PAR_OTHER.getTranslatedValue();
        } else if ((diceRoll[0] == 6 && diceRoll[1] == 5) || (diceRoll[0] == 5 && diceRoll[1] == 6)) {
            score = ROLL65.getTranslatedValue();
        } else if ((diceRoll[0] == 6 && diceRoll[1] == 4) || (diceRoll[0] == 4 && diceRoll[1] == 6)) {
            score = ROLL64.getTranslatedValue();
        } else {
            score = OTHERS.getTranslatedValue();
        }
        return score;
    }

    public static String reverseValueEnumNameExtraction(Optional optional) {
        highestValueUpdate();
        int score;
        if (!(optional.isPresent())) {
            score = Sequence.getSequenceSingleton().getCurrentTurnHighestValue();
        } else {
            score = valueTranslation((int[]) optional.get());
        }
        switch (score) {
            case (7):
                return "Meyer";
            case (6):
                return "Lille Meyer";
            case (5):
                return "Par6";
            case (4):
                return "Par5";
            case (3):
                return "Par 4/3/2/1";
            case (2):
                return "6+5";
            case (1):
                return "6+4";
            case (0):
                return "Something without an actual value";
            default:
                System.out.println("Woops could not retrieve highest roll. Contact programmers and tell them that they are stupid");
        }
        return "reverseValueEnumNameExtraction returned this value. Not supposed to happen. Contact programmers and tell them that they're stupid";
    }
    public static int[] bluffDiceRollByInt(int score) {
        highestValueUpdate();
        switch (score) {
            case (7):
                return MEYER.roll();
            case (6):
                return LILLE_MEYER.roll();
            case (5):
                return PAR6.roll();
            case (4):
                return PAR5.roll();
            case (3):
                return PAR_OTHER.roll();
            case (2):
                return ROLL65.roll();
            case (1):
                return ROLL64.roll();
            case (0):
                return OTHERS.roll();
            default:
                System.out.println("Woops could not retrieve highest roll. Contact programmers and tell them that they are stupid");
        }
        return null;
    }

    public abstract int[] roll();

    public abstract int getTranslatedValue();

}
