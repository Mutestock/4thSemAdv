package assignment.meyer;

import enums.Stages;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Henning
 */
public class Sequence {

    private final Scanner SCANNER = new Scanner(System.in);
    private List<Player> playerList = new ArrayList();
    private Player currentPlayer;
    private static Sequence singleton;
    private boolean setupComplete = false;
    private Map<Integer, Map<Player, int[]>> diceHistory = new HashMap<>();
    private Map<Integer, Map<Player, int[]>> bluffHistory = new HashMap<>();
    private int currentTurnHighestValue;
    private int turn = 0;

    public static Sequence getSequenceSingleton() {
        if (singleton == null) {
            singleton = new Sequence();
        }
        return singleton;
    }

    public void run() {
        if (setupComplete == false) {
            Stages.WELCOME.action();
            Stages.PLAYERSELECTION.action();
            setupComplete = true;
        }
        Stages.ROLL.action();
        Stages.GUESS.action();
        
        if (getSequenceSingleton().getCurrentPlayer().getLife() <= 0) {
            Stages.PLAYER_ELIMINATION.action();
            if (getSequenceSingleton().getPlayerList().size() == 1) {
                Stages.GAMEOVER.action();
            }
        } else {
            Stages.END_TURN.action();
            run();
        }
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Scanner getScanner() {
        return this.SCANNER;
    }

    public Map<Integer, Map<Player, int[]>> getDiceHistory() {
        return this.diceHistory;
    }

    public void setDiceHistory(Map<Integer, Map<Player, int[]>> diceHistory) {
        this.diceHistory = diceHistory;
    }

    public void addToDiceHistory(int[] diceRolls) {
        if (this.diceHistory.containsKey(turn)) {
            if (this.diceHistory.get(turn).containsKey(currentPlayer)) {
                this.turn += 1;
            }
        }

        Map<Player, int[]> turnHistory = new HashMap();
        turnHistory.put(currentPlayer, diceRolls);

        this.diceHistory.put(turn, turnHistory);
    }

    public int getTurn() {
        return this.turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Map<Integer, Map<Player, int[]>> getBluffHistory() {
        return this.bluffHistory;
    }

    public void setBluffHistory(Map<Integer, Map<Player, int[]>> bluffHistory) {
        this.bluffHistory = bluffHistory;
    }

    public void addToBluffHistory(int[] diceRolls) {
        Map<Player, int[]> turnHistory = new HashMap();
        turnHistory.put(currentPlayer, diceRolls);
        this.bluffHistory.put(turn, turnHistory);
    }

    public int getCurrentTurnHighestValue() {
        return this.currentTurnHighestValue;
    }

    public void setCurrentTurnHighestValue(int currentTurnHighestValue) {
        this.currentTurnHighestValue = currentTurnHighestValue;
    }

}
