/* Copyright (c) 2017 FastJAX
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

package org.fastjax.util;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class IdentityArrayListTest {
  @Test
  public void test() {
    final ArrayList<String> regularSet = new ArrayList<>();
    final IdentityArrayList<String> identitySet = new IdentityArrayList<>();

    final String a = "a";
    regularSet.add(a);
    identitySet.add(a);

    Assert.assertTrue(regularSet.contains(new String("a")));
    Assert.assertTrue(identitySet.contains(a));
    Assert.assertFalse(identitySet.contains(new String("a")));

    final IdentityArrayList<String> cloneIdentitySet = identitySet.clone();
    Assert.assertFalse(cloneIdentitySet.contains(new String("a")));
    Assert.assertTrue(cloneIdentitySet.contains(a));
  }
}