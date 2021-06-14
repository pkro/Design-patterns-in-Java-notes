package de.pkro;

import org.junit.Test;

import javax.print.DocFlavor;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Main {

  public static void main(String[] args) {

    SingletonDatabase sd = SingletonDatabase.getInstance();

  }

  @Test // not a unit test as it depends on live data; rather an integrationtest
  public void singletonTotalPopulationTest() {
    SingletonRecordFinder rf = new SingletonRecordFinder();
    List<String> names = List.of("Seoul", "Tokyo");
    int tp = rf.getTotalPopulation(names);
    assertEquals(12700000 + 33200000, tp);
  }

  @Test
  public void dependentPopulationTest() {
    DummyDatabase db = new DummyDatabase();
    ConfigurableRecordFinder rf = new ConfigurableRecordFinder(db);
    assertEquals(3, rf.getTotalPopulation(List.of("alpha", "beta")));
  }
}

interface Database {
  int getPopulation(String name);
}

class DummyDatabase implements Database {
  private Dictionary<String, Integer> data = new Hashtable<>();

  public DummyDatabase() {
    data.put("alpha", 1);
    data.put("beta", 2);
    data.put("gamma", 3);
  }

  @Override
  public int getPopulation(String name) {
    return data.get(name);
  }
}
class SingletonDatabase implements Database {
  private static final SingletonDatabase INSTANCE = new SingletonDatabase();
  private static int instanceCount = 0;
  private Dictionary<String, Integer> capitals = new Hashtable<>();

  private SingletonDatabase() {
    instanceCount++;
    System.out.println("Initializing database");

    try {
      List<String> lines = Files.readAllLines(Paths.get("capitals.txt"));
      partition(lines, 2)
          .forEach(
              sublist -> {
                capitals.put(sublist.get(0), Integer.parseInt(sublist.get(1)));
              });
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println(capitals);
  }

  public static SingletonDatabase getInstance() {
    return INSTANCE;
  }

  public static int getCount() {
    return instanceCount;
  }

  public int getPopulation(String name) {
    return capitals.get(name);
  }

  private List<List<String>> partition(List<String> list, int partSize)
      throws IllegalArgumentException {
    if (list.size() % partSize != 0) {
      throw new IllegalArgumentException("List must be a multiple of partSize");
    }
    int runner = 0;
    List<List<String>> res = new ArrayList<>();
    List<String> temp = new ArrayList<>();
    for (String s : list) {
      temp.add(s);
      runner++;
      if (runner == partSize) {
        res.add(temp);
        temp = new ArrayList<>();
        runner = 0;
      }
    }
    return res;
  }
}

class SingletonRecordFinder {
  public int getTotalPopulation(List<String> names) {
    int result = 0;
    for (String name : names) {
      result += SingletonDatabase.getInstance().getPopulation(name);
    }
    return result;
  }
}


class ConfigurableRecordFinder {
  private Database database;

  public ConfigurableRecordFinder(Database database) {
    this.database = database;
  }

  public int getTotalPopulation(List<String> names) {
    int result = 0;
    for (String name : names) {
      result += database.getPopulation(name);
    }
    return result;
  }
}
