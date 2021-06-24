package de.pkro;

import java.util.*;
import static org.junit.Assert.assertEquals;

public class Main {

  public static void main(String[] args) {
    SingleValue singleValue = new SingleValue(11);
    ManyValues otherValues = new ManyValues();
    otherValues.add(22);
    otherValues.add(33);
    assertEquals(66,
                 new MyList(List.of(singleValue, otherValues)).sum());
  }

}

interface ValueContainer extends Iterable<Integer> {}

class SingleValue implements ValueContainer {
  public int value;

  // please leave this constructor as-is
  public SingleValue(int value) {
    this.value = value;
  }

  @Override
  public Iterator<Integer> iterator() {
    return Collections.singleton(value).iterator();
  }
}

class ManyValues extends ArrayList<Integer> implements ValueContainer {}

class MyList extends ArrayList<ValueContainer> {
  // please leave this constructor as-is
  public MyList(Collection<? extends ValueContainer> c) {
    super(c);
  }

  public int sum() {
    int res = 0;
    for (ValueContainer v : this) {
      for(int s: v) {
        res += s;
      }
    }
    return res;
  }
}
