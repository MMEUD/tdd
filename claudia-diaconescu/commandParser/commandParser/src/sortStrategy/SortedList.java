package sortStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The 'Context' class from the strategy's pattern.
 *
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class SortedList {
  List list;
  SortStrategy sortStrategy;

  /**
   * Constructs a sorted list instance
   * @param sortStrategy The strategy to be used for sorting the list
   */
  public SortedList(SortStrategy sortStrategy) {
    this.sortStrategy = sortStrategy;
    this.list = new ArrayList();
  }

  /**
   * Constructs a sorted list instance
   * @param listObject The list to be sorted
   * @param sortStrategy The strategy to be used for sorting the list
   */
  public SortedList(Object[] listObject, SortStrategy sortStrategy) {
    this.list = Arrays.asList(listObject);
    this.sortStrategy = sortStrategy;
  }

  /**
   * Constructs a sorted list instance
   * @param collection The collection of objects to be sorted
   * @param sortStrategy The strategy to be used for sorting the list
   */
  public SortedList(Collection collection, SortStrategy sortStrategy) {
    this(collection.toArray(), sortStrategy);
  }

  /**
   * Applies the sorting method using selected strategy
   */
  public void sort() {
    sortStrategy.sort(list);
  }

  /**
   * Adds a new value to the list
   * @param value The value to be added
   */
  public void add(Object value) {
    list.add(value);
  }

  /**
   * Gets the size of the list
   * @return The size of the list as int
   */
  public int size() {
    return list.size();
  }

  /**
   * Gets element from the given index from list
   * @param index The required index from index
   * @return The element from the wanted position in list
   */
  public Object get(int index) {
    if(index<size()) {
      return list.get(index);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public List getList() {
    return this.list;
  }
}
