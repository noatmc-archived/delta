package delta.util.autocrystal;

import java.util.Comparator;

public class Sorter implements Comparator<Position> {
    @Override
    public int compare(Position o1, Position o2) {
        return o2.damage.compareTo(o1.damage);
    }
}
