import java.util.ArrayList;
import java.util.NoSuchElementException;

/** Max heap. HuffmanCode.Tree reverses weights so the lightest tree wins. */
public class Heap<E extends Comparable<? super E>> {
    private final ArrayList<E> list = new ArrayList<>();

    public int getSize() { return list.size(); }

    public void add(E value) {
        if (value == null) throw new NullPointerException("value");
        list.add(value);
        int index = list.size() - 1;
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (list.get(index).compareTo(list.get(parent)) <= 0) break;
            swap(index, parent);
            index = parent;
        }
    }

    public E remove() {
        if (list.isEmpty()) throw new NoSuchElementException("Empty heap");
        E result = list.get(0);
        E last = list.remove(list.size() - 1);
        if (list.isEmpty()) return result;
        list.set(0, last);
        int index = 0;
        while (index < list.size() / 2) {
            int child = index * 2 + 1;
            if (child + 1 < list.size()
                    && list.get(child + 1).compareTo(list.get(child)) > 0) child++;
            if (list.get(index).compareTo(list.get(child)) >= 0) break;
            swap(index, child);
            index = child;
        }
        return result;
    }

    private void swap(int a, int b) {
        E temporary = list.get(a);
        list.set(a, list.get(b));
        list.set(b, temporary);
    }
}
