package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutSystemTest
{
  @Test
  @DisplayName("compute the cost of zero items")
  void zero_items_test() {
    assertEquals(0, new CheckoutSystem().checkout(Collections.EMPTY_LIST));
  }

  @Test
  @DisplayName("compute the cost of one apple")
  void one_apple_test() {
    assertEquals(60, new CheckoutSystem().checkout(List.of("apple")));
  }

  @Test
  @DisplayName("compute the cost of one orange")
  void one_orange_test() {
    assertEquals(25, new CheckoutSystem().checkout(List.of("orange")));
  }

  @Test
  @DisplayName("compute the cost of one apple and one orange")
  void one_apple_and_one_orange_test() {
    assertEquals(85, new CheckoutSystem().checkout(List.of("apple","orange")));
  }

  @Test
  @DisplayName("support 'buy one apple get one free' offer")
  void buy_one_apple_get_one_free() {
    assertEquals(85, new CheckoutSystem().checkout(List.of("apple", "apple", "orange")));
  }

  @Test
  @DisplayName("compute the cost of three apples and two oranges")
  void three_apples_and_two_oranges_test() {
    assertEquals(170, new CheckoutSystem().checkout(List.of("apple", "apple", "orange", "apple", "orange")));
  }

  @Test
  @DisplayName("support 'three oranges for the price of two' offer")
  void three_oranges_for_the_price_of_two() {
    assertEquals(110, new CheckoutSystem().checkout(List.of("orange", "apple", "orange", "orange")));
  }

  @Test
  @DisplayName("support multiple offers")
  void three_oranges_and_two_apples_for_the_price_of_two_oranges_and_one_apple() {
    assertEquals(110, new CheckoutSystem().checkout(List.of("orange", "apple", "orange", "orange", "apple")));
  }

  @Test
  @DisplayName("handle one unrecognised item")
  void one_unrecognised_item_test() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new CheckoutSystem().checkout(List.of("unrecognised-item")),
        "ERROR - the following items were not recognised: 'unrecognised-item'.");
  }

  @Test
  @DisplayName("handle more than one unrecognised item")
  void more_than_one_unrecognised_item_test() {
    Throwable throwable = assertThrows(
        IllegalArgumentException.class,
        () -> new CheckoutSystem().checkout(List.of("one-unrecognised-item", "another-unrecognised-item")));

    assertEquals("ERROR - the following items were not recognised: one-unrecognised-item,another-unrecognised-item.",
        throwable.getMessage());
  }

}
