package de.pkro;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }
}

class Property<T> {
    private T value;
    public Property(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        // do logging here
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property<?> property = (Property<?>) o;
        return Objects.equals(value, property.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

class Creature {
    private Property<Integer> agility = new Property<>(10);

    public void setAgility(int value) { // we keep the normal int as parameter
        // logging or whatever here
        agility.setValue(value);
    }

    public int getAgility() {
        return agility.getValue();
    }
}
/*
class Creature {
    private int agility;

    public Creature(int agility) {
        // can't be recoreded as it doesn't use
        this.agility = agility;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }
}
*/
 */