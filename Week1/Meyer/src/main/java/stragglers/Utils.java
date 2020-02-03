package stragglers;

import assignment.meyer.Sequence;
import org.omg.CORBA.Any;

/**
 *
 * @author Henning
 */
public class Utils {

    private static Utils UTILS_SINGLETON;

    public static Utils getUtilsSingleton() {
        if (UTILS_SINGLETON == null) {
            UTILS_SINGLETON = new Utils();
        }
        return UTILS_SINGLETON;
    }

    public void defaultErrorMessage(String option) {
        Sequence sequence = Sequence.getSequenceSingleton();
        System.out.println(
                option
                + " is not a valid option"
                + "\n"
                + "Press any key to continue");
        sequence.getScanner().nextLine();
        System.out.println("\n\n\n\n");
    }

    public void pressAnyKey() {
        Sequence sequence = Sequence.getSequenceSingleton();
        System.out.println("\n\n"
                + "Press any key to continue");
        sequence.getScanner().nextLine();
    }

    public void playerSwap() {
        throw new UnsupportedOperationException();
    }

    public void rinseVariablesForRestart() {
        throw new UnsupportedOperationException();
    }

}
