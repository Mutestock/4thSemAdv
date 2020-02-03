package enums;

import assignment.meyer.Sequence;

/**
 *
 * @author Henning
 */
public enum Help {
    GAME_DETAILS {
        @Override
        public void action() {
            System.out.println("https://terningspil.dk/spilleregler-til-meyer/ \n");
            helpAction();
        }
    },
    MADE_BY {
        @Override
        public void action() {
            System.out.println("Some student taking an education in programming \n");
            helpAction();
        }
    },
    PATREON {
        @Override
        public void action() {
            System.out.println("https://www.patreon.com/nothing \n");
            helpAction();
        }
    };

    private static void helpSwitch(String option) {
        switch (option) {
            case ("1"): {
                GAME_DETAILS.action();
                break;
            }
            case ("2"): {
                MADE_BY.action();
                break;
            }
            case ("3"): {
                PATREON.action();
                break;
            }
            case ("4"): {
                Stages.WELCOME.action();
                break;
            }
            default:
                System.out.println("Unknown action. Please try something else");
                System.out.println("\n");
                helpAction();
                break;
        }
    }

    public static void helpAction() {
        System.out.println("Help"
                +"\n"
                + "1. Game Details \n"
                + "2. Made By \n"
                + "3. Patreon \n"
                + "4. Back \n"
                + "\n");

        Sequence sequence = Sequence.getSequenceSingleton();
        String option = sequence.getScanner().nextLine();
        helpSwitch(option);
    }

    public abstract void action();
}
