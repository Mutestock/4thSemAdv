package stragglers;

import assignment.meyer.Sequence;
import enums.RollTypes;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Henning
 */
public class ComputerSpecific {

    private static Random random = new Random();

    public static String comBluffChoice(int[] diceRoll) {
        /* 
        Factors:
        1. Is it necessary?
        2. Cut off unimportant choices (values below threshold)
        3. 2d6 odds - 6^2 ~ 32
            3a. Out of which 12 of them are without values as in 37.5 %
        4. Need to set base aggressive. There's probably a better way of doing things... Gonna set it to 75% stay on current
        5. (Not gonna do this) How will I act depending on my current remaining HP
         */
        Sequence sequence = Sequence.getSequenceSingleton();
        RollTypes.highestValueUpdate();
        int highVal = sequence.getCurrentTurnHighestValue();
        int thisRollVal = RollTypes.valueTranslation(diceRoll);
        //I'm going to be lazy here..

        //Decide aggressive move
        System.out.println("highVal: " + highVal + " thisRollVal: " + thisRollVal);
        if (highVal <= thisRollVal) {
            if (random.nextInt(4) + 1 == 1) {
                return "2";
            } else {
                return "1";
            }
        } else {
            return "2";
        }
    }

    public static String bluffAggression() {
        Sequence sequence = Sequence.getSequenceSingleton();
        RollTypes.highestValueUpdate();
        int highVal = sequence.getCurrentTurnHighestValue();
        Map<Integer, Double> rollActualValVsPercentage = new HashMap<>();
        IntStream.of(12, 2, 2, 8, 2, 2, 2, 2).forEach(o -> {
            rollActualValVsPercentage.put(rollActualValVsPercentage.size(), Double.valueOf(o) / 32 * 100);
        });

        //This is to make sure that the computer doesn't make an unreasonable bluff.
        //Needs actual math here.
        //Expensive
        String num = "";
        num = bluffAggroLoop(highVal, rollActualValVsPercentage, num);
        while (Integer.parseInt(num) == 0) {
            num = bluffAggroLoop(highVal, rollActualValVsPercentage, num);
        }
        System.out.println("Bluff aggression output DELETE THIS ON PRODUCTION");
        System.out.println("Bluff aggro exited with " + num);
        return num;
    }

    private static String bluffAggroLoop(int highVal, Map<Integer, Double> rollActualValVsPercentage, String num) {
        for (int i = highVal; i <= 7; i++) {
            double total = rollActualValVsPercentage
                    .entrySet()
                    .stream()
                    .filter(o -> o.getKey() >= highVal)
                    .collect(Collectors.summingDouble(d -> d.getValue()));
            double percentage = rollActualValVsPercentage.get(i);
            double odds = percentage + ((total - percentage) / (7 - i));
            System.out.println("########");
            System.out.println("Total: " + total + " Percentage: " + percentage + " Odds: " + odds);
            System.out.println("########");
            if (ThreadLocalRandom.current().nextDouble(total) < odds) {
                num = Integer.toString(i);
                break;
            }
            rollActualValVsPercentage.remove(i);
        }
        return num;
    }

    public static String comBluffGuess(int[] recognizedRoll) {
        Sequence sequence = Sequence.getSequenceSingleton();
        RollTypes.highestValueUpdate();
        int highVal = sequence.getCurrentTurnHighestValue();
        Map<Integer, Double> rollActualValVsPercentage = new HashMap<>();
        IntStream.of(12, 2, 2, 8, 2, 2, 2, 2).forEach(o -> {
            rollActualValVsPercentage.put(rollActualValVsPercentage.size(), Double.valueOf(o) / 32 * 100);
        });
        int thisRollVal = RollTypes.valueTranslation(recognizedRoll);
        if (thisRollVal < highVal || thisRollVal == 0) {
            return "1";
        }
        for (int i = 0; i <= thisRollVal; i++) {
            double total = rollActualValVsPercentage
                    .entrySet()
                    .stream()
                    .collect(Collectors.summingDouble(d -> d.getValue()));
            double percentage = rollActualValVsPercentage.get(i);
            double odds = percentage + ((total - percentage) / (7 - i));
            System.out.println("####Guess####");
            System.out.println("Total: " + total + " Percentage: " + percentage + " Odds: " + odds);
            System.out.println("########");

            if (ThreadLocalRandom.current().nextDouble(total) < odds) {
                if (thisRollVal >= RollTypes.valueTranslation(RollTypes.bluffDiceRollByInt(i))) {
                    return "2";
                } else {
                    return "1";
                }
            }
            rollActualValVsPercentage.remove(i);
        }
        return "1";
    }
}
