package sortStrategy;


import java.util.List;

/**
 * Quicksort first divides a large list into two smaller sub-lists, the low elements and the high elements.
 * Then recursively sort the sub-lists.
 * The steps are: <br/><ul>
 * <li>Pick an element, called a pivot, from the list.</li><br/>
 * <li>Reorder the list so that all elements with values less than the pivot come before the pivot,
 * while all elements with values greater than the pivot come after it.<br/>
 * After this partitioning, the pivot is in its final position. This is called the partition operation.</li><br/>
 * <li>Recursively apply the above steps to the sub-list of elements with smaller values and separately
 * the sub-list of elements with greater values.</li>
 * </ul>
 *
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class QuickSortStrategy implements SortStrategy {
  @Override
  public void sort(List list) {
    if (list == null || list.size() == 1) {
      return;
    }
    quickSort(list, 0, list.size()-1);
  }


  /**
   * Reorder the list so that all elements with values less than the pivot come before the pivot,
   * while all elements with values greater than the pivot come after it.
   * @param list The list to be sorted
   * @param left The most left side index of the list from which the sorting starts
   * @param right The most right side index of the list to which the sorting ends
   * @param pivot The pivot index
   * @return The new index of the pivot
   */
  private int partition(List list, int left, int right, int pivot) {

    Object pivotValue = list.get(pivot);
    int index = left;
    // move pivot to the end
    swap(list, pivot, right);

    for (int i = left; i < right; i++) {
      int compare = 0;

      if(pivotValue instanceof Comparable) {
        compare = ((Comparable)list.get(i)).compareTo((Comparable)pivotValue);
      }

      if (compare <= 0) {
        swap(list, i, index);
        index = index + 1;
      }
    }
    // move pivot to its final place
    swap(list, index, right);

    return index;
  }

  /**
   * Interchange two elements from list.
   * @param list The source list
   * @param sourceIndex Index of first element to be swapped
   * @param destinationIndex Index of second element to be swapped
   */
  private void swap(List list, int sourceIndex, int destinationIndex) {
    Object temp = list.get(sourceIndex);
    list.set(sourceIndex, list.get(destinationIndex));
    list.set(destinationIndex, temp);
  }

  /**
   * Recursive method for implementing the sorting algorithm.
   * @param list The list to be sorted
   * @param left The most left side index of the list from which the sorting starts
   * @param right The most right side index of the list to which the sorting ends
   */
  private void quickSort(List list, int left, int right) {
    // if the list has 2 or more items
    if (left < right) {
      int pivotIndex = left + (right - left) / 2;

      int newPivotIndex = partition(list, left, right, pivotIndex);

      // recursively sort elements smaller than the pivot
      quickSort(list, left, newPivotIndex - 1);

      // recursively sort elements bigger (or equal) than the pivot
      quickSort(list, newPivotIndex + 1, right);
    }
  }

}
