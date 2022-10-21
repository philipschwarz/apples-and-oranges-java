package org.example;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static org.example.ItemType.*;

enum ItemType {
  Apple("apple", 60, Optional.of(new Offer(2,1))),
  Orange("orange", 25, Optional.of(new Offer(3,1))),
  Unrecognised("unrecognised-item", 0, Optional.empty()) ;

  final String name;
  final int price;
  final Optional<Offer> offer;

  ItemType(String itemName, int price, Optional<Offer> offer){
    this.name = itemName;
    this.price = price;
    this.offer = offer;
  }
}

record Offer(int quantity, int freeQuantity) {}

public class CheckoutSystem {

  /**
   * Given a list of item names, returns the total price of the items (in pence).
   *
   * Recognised items are "apple" and "orange"
   *
   * If one or more items are not recognised then an error message is returned
   * indicating the item names that were not recognised.
   *
   * @param items
   * @return Either the price of the items (in pence) or an error message
   *         indicating item names that were not recognised.
   */
  public int checkout(List<String> items) {
    Map<ItemType, List<String>> itemsByType = groupItemsByType(items);
    List<String> unrecognisedItems = getUnrecognisedItems(itemsByType);
    if (unrecognisedItems.isEmpty())
      return computePrice(itemsByType);
    else throw new IllegalArgumentException(
        "ERROR - the following items were not recognised: "
        + String.join(",",unrecognisedItems) + ".");
  }

  /**
   * Given a map from ItemType to a list of items of that type, returns the price of the
   * all the items in the map.
   */
  private int computePrice(Map<ItemType, List<String>> itemsByType) {
    return computePriceForEachItemType(itemsByType).sum();
  }

  /**
   * Given a map from ItemType to a list of items of that type, returns a list
   * containing the price for the items of each type.
   */
  private IntStream computePriceForEachItemType(Map<ItemType, List<String>> itemsByType) {
      return
          itemsByType
              .entrySet()
              .stream().mapToInt( entry -> {
                  var item = entry.getKey();
                  var quantity = entry.getValue().size();
                  return item.offer.map( offer ->
                      item.price * (quantity / offer.quantity() * (offer.quantity() - offer.freeQuantity()) + quantity % offer.quantity())
                  ).orElse(item.price * quantity);
                }
              );
  }

  /**
   * Given a list of item names, e.g. List("apple","orange", "apple"), returns a map from
   * ItemType (e.g. Apple) to the list of items of that type, e.g. List("apple","apple").
   */
  private Map<ItemType, List<String>> groupItemsByType(List<String> items) {
      return items.stream().collect(groupingBy(item -> {
        ItemType result;
        if (item.equals(Apple.name)) result = Apple;
        else if (item.equals(Orange.name)) result = Orange;
        else result = Unrecognised;
        return result;
      }));
  }

  /**
   * Given a map from ItemType to a list of items of that type, returns the map's value for
   * ItemType.Unrecognised, i.e. a list of the names of unrecognised items.
   */
  private List<String> getUnrecognisedItems(Map<ItemType, List<String>> itemsByType) {
      return itemsByType.getOrDefault(Unrecognised,Collections.EMPTY_LIST);
  }

}
