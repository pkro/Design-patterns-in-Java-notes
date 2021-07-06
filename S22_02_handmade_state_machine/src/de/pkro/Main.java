package de.pkro;

import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

enum State {
  OFF_HOOK, // starting state
  ON_HOOK, // terminal state
  CONNECTING,
  CONNECTED,
  ON_HOLD
}

enum Trigger {
  CALL_DIALED,
  HUNG_UP,
  CALL_CONNECTED,
  PLACED_ON_HOLD,
  TAKEN_OFF_HOLD,
  LEFT_MESSAGE,
  STOP_USING_PHONE
}

public class Main {
  private static Map<State, List<Pair<Trigger, State>>> rules = new HashMap<>();
  private static State currentState = State.OFF_HOOK;
  private static State exitState = State.ON_HOOK; // state when exiting application

  // reminder: static blocks are executed only once at the time the class is loaded into memory
  // thread safe
  static {
    rules.put(
        State.OFF_HOOK, // if in off hook state
        List.of( // we can trigger a state (dialing a call puts it into the connecting state)
                 // the triggers are not commands, but information for the state machine
                 // that the user inputs:
                 // "I just dialed a call" -> CALL_DIALED
                 // "The other party hung up" -> HUNG_UP etc
            new Pair<>(Trigger.CALL_DIALED, State.CONNECTING),
            new Pair<>(Trigger.STOP_USING_PHONE, State.ON_HOOK)));

    rules.put(
        State.CONNECTING,
        List.of(
            new Pair<>(Trigger.HUNG_UP, State.OFF_HOOK),
            new Pair<>(Trigger.CALL_CONNECTED, State.CONNECTED)));

    rules.put(
        State.CONNECTED,
        List.of(
            new Pair<>(Trigger.LEFT_MESSAGE, State.OFF_HOOK),
            new Pair<>(Trigger.HUNG_UP, State.OFF_HOOK),
            new Pair<>(Trigger.PLACED_ON_HOLD, State.ON_HOLD)));

    rules.put(
        State.ON_HOLD,
        List.of(
            new Pair<>(Trigger.TAKEN_OFF_HOLD, State.CONNECTED),
            new Pair<>(Trigger.HUNG_UP, State.OFF_HOOK)));
  }

  public static void main(String[] args) {
    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      System.out.println("The phone is currently " + currentState);
      for (int i = 0; i < rules.get(currentState).size(); i++) {
          Trigger trigger = rules.get(currentState).get(i).getValue0();
        System.out.println(i + ". " + trigger);
      }
      boolean parseOK;
      int choice = 0;

      do {
        try {
          System.out.println("Please enter a trigger:");
          choice = Integer.parseInt(console.readLine());
          parseOK = choice >=0 && choice < rules.get(currentState).size();
        } catch (Exception e) {
          parseOK = false;
        }
      } while(!parseOK);

      currentState = rules.get(currentState).get(choice).getValue1();
      if (currentState == exitState) {
        break;
      }
      System.out.println("Done!");
    }
  }
}
