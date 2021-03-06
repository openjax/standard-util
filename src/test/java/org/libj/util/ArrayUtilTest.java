/* Copyright (c) 2016 LibJ
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.libj.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Objects;

import org.junit.Test;
import org.libj.lang.Strings;

public class ArrayUtilTest {
  private static Object[] createRandomNestedArray() {
    final Object[] array = new Object[4];
    for (int i = 0; i < array.length; ++i)
      array[i] = Math.random() < 0.2 ? createRandomNestedArray() : Strings.getRandomAlpha(4);

    return array;
  }

  @Test
  public void testFlattenArrayRetainingReferences() {
    final Object[] array = {"a", "b", new Object[] {"c", "d"}, "e", new Object[] {"f", new Object[] {"g", "h"}}, "i"};
    final Object[] result = ArrayUtil.flatten(array);
    assertEquals("[" + java.util.Arrays.deepToString(array).replace("[", "").replace("]", "") + "]", java.util.Arrays.deepToString(result));

    final Object[] expected = {array[0], array[1], array[2], ((Object[])array[2])[0], ((Object[])array[2])[1], array[3], array[4], ((Object[])array[4])[0], ((Object[])array[4])[1], ((Object[])((Object[])array[4])[1])[0], ((Object[])((Object[])array[4])[1])[1], array[5]};
    final Object[] resultRetainingReferences = ArrayUtil.flatten(array, true);
    assertArrayEquals(expected, resultRetainingReferences);
  }

  @Test
  public void testFlattenArray() {
    for (int i = 0; i < 100; ++i) {
      final Object[] array = createRandomNestedArray();
      final Object[] result = ArrayUtil.flatten(array);
      assertEquals("[" + java.util.Arrays.deepToString(array).replace("[", "").replace("]", "") + "]", java.util.Arrays.deepToString(result));
    }
  }

  @Test
  public void testBinaryClosestSearch() {
    final int[] sorted = {1, 3, 5, 9, 19};
    try {
      ArrayUtil.binaryClosestSearch(sorted, 4, 3, -10);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertEquals(0, ArrayUtil.binaryClosestSearch(sorted, -10));
    assertEquals(0, ArrayUtil.binaryClosestSearch(sorted, -1));
    assertEquals(0, ArrayUtil.binaryClosestSearch(sorted, 0));
    assertEquals(1, ArrayUtil.binaryClosestSearch(sorted, 2));
    assertEquals(2, ArrayUtil.binaryClosestSearch(sorted, 4));
    assertEquals(3, ArrayUtil.binaryClosestSearch(sorted, 6));
    assertEquals(3, ArrayUtil.binaryClosestSearch(sorted, 9));
    assertEquals(4, ArrayUtil.binaryClosestSearch(sorted, 10));
    assertEquals(4, ArrayUtil.binaryClosestSearch(sorted, 14));
    assertEquals(4, ArrayUtil.binaryClosestSearch(sorted, 18));
    assertEquals(4, ArrayUtil.binaryClosestSearch(sorted, 19));
    assertEquals(5, ArrayUtil.binaryClosestSearch(sorted, 20));
    assertEquals(5, ArrayUtil.binaryClosestSearch(sorted, 40));
  }

  @Test
  public void testTransform() {
    assertArrayEquals(new String[] {"ONE", "TWO", "THREE"}, ArrayUtil.replaceAll(String::toUpperCase, "one", "two", "three"));
    assertArrayEquals(new String[] {}, ArrayUtil.replaceAll(String::toUpperCase, new String[] {}));
  }

  @Test
  public void testFilter() {
    final String[] expected = {"ONE", "TWO", "THREE"};
    final String[] filtered = ArrayUtil.filter(Objects::nonNull, new String[] {"ONE", null, "TWO", null, "THREE"});
    assertArrayEquals(expected, filtered);
  }

  @Test
  public void testConcat() {
    final String[] one = {"a", "b", "c"};
    final String[] two = {"d", "e", "f"};
    final String[] concat = ArrayUtil.concat(one, two);
    assertArrayEquals(new String[] {"a", "b", "c", "d", "e", "f"}, concat);
  }

  @Test
  public void testSubArray() {
    final String[] array = {"a", "b", "c", "d", "e", "f"};
    assertArrayEquals(new String[] {"c", "d", "e"}, ArrayUtil.subArray(array, 2, 3));
    assertArrayEquals(new String[] {"a", "b", "c", "d", "e", "f"}, ArrayUtil.subArray(array, 0));
    assertArrayEquals(new String[] {"c", "d", "e", "f"}, ArrayUtil.subArray(array, 2));
    assertArrayEquals(new String[] {}, ArrayUtil.subArray(array, 200));
  }

  @Test
  public void testSplice() {
    final String[] array = {"a", "b", "c", "d", "e", "f"};

    assertArrayEquals(new String[] {"c", "d", "e", "f"}, ArrayUtil.splice(array, 0, 2));
    assertArrayEquals(new String[] {"a", "d", "e", "f"}, ArrayUtil.splice(array, 1, 2));
    assertArrayEquals(new String[] {"a", "b", "e", "f"}, ArrayUtil.splice(array, 2, 2));
    assertArrayEquals(new String[] {"a", "b", "c", "f"}, ArrayUtil.splice(array, 3, 2));
    assertArrayEquals(new String[] {"a", "b", "c", "d"}, ArrayUtil.splice(array, 4, 2));
    try {
      ArrayUtil.splice(array, 5, 2);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }

    try {
      ArrayUtil.splice(array, -2, 3);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }
    assertArrayEquals(new String[] {"a", "b", "c", "d"}, ArrayUtil.splice(array, -2, 2));
    assertArrayEquals(new String[] {"a", "b", "c", "f"}, ArrayUtil.splice(array, -3, 2));
    assertArrayEquals(new String[] {"a", "b", "e", "f"}, ArrayUtil.splice(array, -4, 2));
    assertArrayEquals(new String[] {"a", "d", "e", "f"}, ArrayUtil.splice(array, -5, 2));
    assertArrayEquals(new String[] {"c", "d", "e", "f"}, ArrayUtil.splice(array, -6, 2));
    try {
      ArrayUtil.splice(array, -7, 2);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }

    assertArrayEquals(new String[] {}, ArrayUtil.splice(array, 0));
    assertArrayEquals(new String[] {"a"}, ArrayUtil.splice(array, 1));
    assertArrayEquals(new String[] {"a", "b"}, ArrayUtil.splice(array, 2));
    assertArrayEquals(new String[] {"a", "b", "c"}, ArrayUtil.splice(array, 3));
    assertArrayEquals(new String[] {"a", "b", "c", "d"}, ArrayUtil.splice(array, 4));
    assertArrayEquals(new String[] {"a", "b", "c", "d", "e"}, ArrayUtil.splice(array, 5));
    assertArrayEquals(new String[] {"a", "b", "c", "d", "e", "f"}, ArrayUtil.splice(array, 6));
    try {
      ArrayUtil.splice(array, 7);
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }

    assertArrayEquals(new String[] {"a", "b", "c", "d", "e"}, ArrayUtil.splice(array, -1));
    assertArrayEquals(new String[] {"a", "b", "c", "d"}, ArrayUtil.splice(array, -2));
    assertArrayEquals(new String[] {"a", "b", "c"}, ArrayUtil.splice(array, -3));
    assertArrayEquals(new String[] {"a", "b"}, ArrayUtil.splice(array, -4));
    assertArrayEquals(new String[] {"a"}, ArrayUtil.splice(array, -5));
    assertArrayEquals(new String[] {}, ArrayUtil.splice(array, -6));
    try {
      ArrayUtil.splice(array, -7);
      fail("Expected NegativeArraySizeException");
    }
    catch (final NegativeArraySizeException e) {
    }

    assertArrayEquals(new String[] {"x", "y", "z", "c", "d", "e", "f"}, ArrayUtil.splice(array, 0, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "x", "y", "z", "d", "e", "f"}, ArrayUtil.splice(array, 1, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "x", "y", "z", "e", "f"}, ArrayUtil.splice(array, 2, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "c", "x", "y", "z", "f"}, ArrayUtil.splice(array, 3, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "c", "d", "x", "y", "z"}, ArrayUtil.splice(array, 4, 2, "x", "y", "z"));
    try {
      ArrayUtil.splice(array, 5, 2, "x", "y", "z");
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }

    assertArrayEquals(new String[] {"a", "b", "c", "d", "x", "y", "z"}, ArrayUtil.splice(array, -2, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "c", "x", "y", "z", "f"}, ArrayUtil.splice(array, -3, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "b", "x", "y", "z", "e", "f"}, ArrayUtil.splice(array, -4, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"a", "x", "y", "z", "d", "e", "f"}, ArrayUtil.splice(array, -5, 2, "x", "y", "z"));
    assertArrayEquals(new String[] {"x", "y", "z", "c", "d", "e", "f"}, ArrayUtil.splice(array, -6, 2, "x", "y", "z"));
    try {
      ArrayUtil.splice(array, -7, 2, "x", "y", "z");
      fail("Expected ArrayIndexOutOfBoundsException");
    }
    catch (final ArrayIndexOutOfBoundsException e) {
    }
  }

  @Test
  public void testCreateRepeat() {
    final String[] array = ArrayUtil.createRepeat("hello", 8);
    assertArrayEquals(new String[] {"hello", "hello", "hello", "hello", "hello", "hello", "hello", "hello"}, array);
  }

  @Test
  public void testReverseBytes() {
    final byte[] array = new byte[100];
    for (byte i = 0; i < array.length; ++i)
      array[i] = i;

    ArrayUtil.reverse(array);
    for (byte i = 0; i < array.length; ++i)
      assertEquals((byte)(array.length - i - 1), array[i]);
  }

  @Test
  public void testReverseShort() {
    final short[] array = new short[100];
    for (short i = 0; i < array.length; ++i)
      array[i] = i;

    ArrayUtil.reverse(array);
    for (short i = 0; i < array.length; ++i)
      assertEquals((short)(array.length - i - 1), array[i]);
  }

  @Test
  public void testReverseInt() {
    final int[] array = new int[100];
    for (int i = 0; i < array.length; ++i)
      array[i] = i;

    ArrayUtil.reverse(array);
    for (int i = 0; i < array.length; ++i)
      assertEquals(array.length - i - 1, array[i]);
  }

  @Test
  public void testReverseLong() {
    final long[] array = new long[100];
    for (long i = 0; i < array.length; ++i)
      array[(int)i] = i;

    ArrayUtil.reverse(array);
    for (long i = 0; i < array.length; ++i)
      assertEquals(array.length - i - 1, array[(int)i]);
  }

  @Test
  public void testReverseFloat() {
    final float[] array = new float[100];
    for (float i = 0; i < array.length; ++i)
      array[(int)i] = i;

    ArrayUtil.reverse(array);
    for (float i = 0; i < array.length; ++i)
      assertEquals(array.length - i - 1, array[(int)i], 0);
  }

  @Test
  public void testReverseDouble() {
    final double[] array = new double[100];
    for (double i = 0; i < array.length; ++i)
      array[(int)i] = i;

    ArrayUtil.reverse(array);
    for (double i = 0; i < array.length; ++i)
      assertEquals(array.length - i - 1, array[(int)i], 0);
  }

  @Test
  public void testReverseBoolean() {
    final boolean[] array = new boolean[100];
    for (int i = 0; i < array.length; ++i)
      array[i] = i % 2 < 0;

    ArrayUtil.reverse(array);
    for (double i = 0; i < array.length; ++i)
      assertEquals(array.length - i - 1 < 0, array[(int)i]);
  }

  @Test
  public void testReverseObject() {
    final String[] array = new String[100];
    for (int i = 0; i < array.length; ++i)
      array[i] = String.valueOf(i);

    ArrayUtil.reverse(array);
    for (int i = 0; i < array.length; ++i)
      assertEquals(String.valueOf(array.length - i - 1), array[i]);
  }

  @Test
  public void testShiftByte() {
    final byte[] expected = {3, 4, 5, 1, 2};
    final byte[] actual = {1, 2, 3, 4, 5};
    ArrayUtil.shift(actual, 2);
    assertArrayEquals(Arrays.toString(actual), expected, actual);
  }

  @Test
  public void testShiftShort() {
    final short[] expected = {3, 4, 5, 1, 2};
    final short[] actual = {1, 2, 3, 4, 5};
    ArrayUtil.shift(actual, 2);
    assertArrayEquals(Arrays.toString(actual), expected, actual);
  }

  @Test
  public void testShiftInt() {
    final int[] expected = {3, 4, 5, 1, 2};
    final int[] actual = {1, 2, 3, 4, 5};
    ArrayUtil.shift(actual, 2);
    assertArrayEquals(Arrays.toString(actual), expected, actual);
  }

  @Test
  public void testShiftLong() {
    final long[] expected = {3, 4, 5, 1, 2};
    final long[] actual = {1, 2, 3, 4, 5};
    ArrayUtil.shift(actual, 2);
    assertArrayEquals(Arrays.toString(actual), expected, actual);
  }

  @Test
  public void testShiftFloat() {
    final float[] expected = {3, 4, 5, 1, 2};
    final float[] actual = {1, 2, 3, 4, 5};
    ArrayUtil.shift(actual, 2);
    assertArrayEquals(Arrays.toString(actual), expected, actual, 0);
  }

  @Test
  public void testShiftDouble() {
    final double[] expected = {3, 4, 5, 1, 2};
    final double[] actual = {1, 2, 3, 4, 5};
    ArrayUtil.shift(actual, 2);
    assertArrayEquals(Arrays.toString(actual), expected, actual, 0);
  }

  @Test
  public void testShiftBoolean() {
    final boolean[] expected = {false, true, true, false, true};
    final boolean[] actual = {false, true, false, true, true};
    ArrayUtil.shift(actual, 2);
    assertArrayEquals(Arrays.toString(actual), expected, actual);
  }

  @Test
  public void testShiftChar() {
    final char[] expected = {3, 4, 5, 1, 2};
    final char[] actual = {1, 2, 3, 4, 5};
    ArrayUtil.shift(actual, 2);
    assertArrayEquals(Arrays.toString(actual), expected, actual);
  }

  @Test
  public void testShiftString() {
    final String[] expected = {"3", "4", "5", "1", "2"};
    final String[] actual = {"1", "2", "3", "4", "5"};
    ArrayUtil.shift(actual, 2);
    assertArrayEquals(Arrays.toString(actual), expected, actual);
  }
}