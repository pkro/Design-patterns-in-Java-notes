package de.pkro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {
    ChatRoom chatRoom = new ChatRoom();
    Person p = new Person("Peer");
    Person k = new Person("Klaus");
    Person y = new Person("Yvonne");
    chatRoom.join(p);
    chatRoom.join(k);
    chatRoom.join(y);

    p.privateMessage("Yvonne", "Unsoliciticed advance");

    y.say("Someone is bothering me with PMs help!!!");
  }
}

class Person {
  public ChatRoom room;
  String name;
  private List<String> chatLog = new ArrayList<>();

  public Person(String name) {
    this.name = name;
  }

  public void receive(String sender, String message) {
    String s = sender + ": " + message;
    System.out.println("[" + name + "'s chat session] " + s);
  }

  public void say(String message) {
    room.broadcast(name, message);
  }

  public void privateMessage(String who, String message) {
    room.message(name, who, message);
  }
}

class ChatRoom {
  private List<Person> people;

  public ChatRoom() {
    this.people = new ArrayList<>();
  }

  public void join(Person person) {
    people.add(person);
    person.room = this;
    broadcast("room", person.name + " has joined the room");
  }

  public void broadcast(String source, String message) {
    for (Person person : people) {
      //if (!source.equals(person.name)) { // the sender should also see the message IMO
        person.receive(source, message);
      //}
    }
  }

  public void message(String source, String destination, String message) {
        people.stream()
            .filter(p -> p.name.equals(destination))
            .findFirst()
            .ifPresent(person -> person.receive(source, message));
  }
}
