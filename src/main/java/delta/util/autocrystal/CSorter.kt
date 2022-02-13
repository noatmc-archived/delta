package delta.util.autocrystal

import java.util.Comparator

class CSorter: Comparator<Crystal> {
    override fun compare(o1: Crystal, o2: Crystal): Int {
        return o2.getDamage().compareTo(o1.getDamage())
    }
}