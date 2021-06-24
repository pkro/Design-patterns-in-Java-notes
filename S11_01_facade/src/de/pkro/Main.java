package de.pkro;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    // low level usage of subsystems without facade:
    Buffer buffer = new Buffer(30, 20);
    Viewport viewport = new Viewport(buffer, 30, 20, 0, 0);
    Console console = new Console(30, 20);
    console.addViewport(viewport);
    console.render();

    // with facade method, single buffer, single viewport
    Console console1 = Console.newConsole(30, 20);
    console1.render();
  }
}

class Buffer  {
  private char [] characters;
  private int lineWidth;

  public Buffer(int lineHeight, int lineWidth) {
    this.lineWidth = lineWidth;
    this.characters = new char[lineWidth*lineHeight];
  }

  public char charAt(int x, int y) {
    return characters[y*lineWidth+x];
  }
}

class Viewport {
  private final Buffer buffer;
  private final int width;
  private final int height;
  private final int offsetX;
  private final int offsetY;

  public Viewport(Buffer buffer, int width, int height, int offsetX, int offsetY) {
    this.buffer = buffer;
    this.width = width;
    this.height = height;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  public char charAt(int x, int y) {
    return buffer.charAt(x+offsetX, y+offsetY);
  }
}

class Console {
  private List<Viewport> viewports = new ArrayList<>();
  int width, height;

  public Console(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void addViewport(Viewport viewport) {
    viewports.add(viewport);
  }

  public void render() {
    for(int y=0; y< height; y++) {
      for(int x=0; x<width; x++) {
        for(Viewport vp: viewports) {
          System.out.println(vp.charAt(x,y));
        }
        System.out.println(System.lineSeparator());
      }
    }
  }

  // the actual facade
  public static Console newConsole(int width, int height) {
    Buffer buffer = new Buffer(width, height);
    Viewport viewport = new Viewport(buffer, width, height, 0, 0);
    Console console = new Console(width, height);
    return console;
  }

}