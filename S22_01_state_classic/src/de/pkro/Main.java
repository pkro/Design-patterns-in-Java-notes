package de.pkro;

// classic but weird and not practical Gang of four implementation of state machine
public class Main {

  public static void main(String[] args) {
    LightSwitch lightSwitch = new LightSwitch();
    lightSwitch.on();
    lightSwitch.on();
    lightSwitch.off();
    lightSwitch.off();
    lightSwitch.off();
  }
}

class State {
  // this only gets called when trying to call on from the OnState,
  // as only "off" is overridden there and "on" is called in this superclass
  void on(LightSwitch ls) {
    System.out.println("Light is already on");
  }

  // this only gets called when trying to call off from the OffState,
  // as only "on" is overridden there and "off" is called in this superclass
  void off(LightSwitch ls) {
    System.out.println("Light is already off");
  }
}

class OnState extends State {
  public OnState() {
    System.out.println("Light turned on");
  }

  @Override
  void off(LightSwitch ls) {
    System.out.println("Switching light off");
    ls.setState(new OffState());
  }
}

class OffState extends State {
  public OffState() {
    System.out.println("Light turned off");
  }

  @Override
  void on(LightSwitch ls) {
    System.out.println("Switching light on");
    ls.setState(new OnState());
  }
}

class LightSwitch {
  private State state; // OnState, OffState

  public LightSwitch() {
    this.state = new OffState();
  }

  // the weird GOF part
  void on() {
    state.on(this);
  }

  void off() {
    state.off(this);
  }

  public void setState(State s) {
    this.state = s;
  }
}
