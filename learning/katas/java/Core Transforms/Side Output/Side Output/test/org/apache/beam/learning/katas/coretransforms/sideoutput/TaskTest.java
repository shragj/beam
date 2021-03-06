/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.beam.learning.katas.coretransforms.sideoutput;

import java.io.Serializable;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.junit.Rule;
import org.junit.Test;

public class TaskTest implements Serializable {

  @Rule
  public final transient TestPipeline testPipeline = TestPipeline.create();

  @Test
  public void sideOutput() {
    PCollection<Integer> numbers = testPipeline.apply(Create.of(10, 50, 120, 20, 200, 0));

    TupleTag<Integer> numBelow100Tag = new TupleTag<Integer>() {};
    TupleTag<Integer> numAbove100Tag = new TupleTag<Integer>() {};

    PCollectionTuple resultsTuple = Task.applyTransform(numbers, numBelow100Tag, numAbove100Tag);

    PAssert.that(resultsTuple.get(numBelow100Tag))
        .containsInAnyOrder(0, 10, 20, 50);

    PAssert.that(resultsTuple.get(numAbove100Tag))
        .containsInAnyOrder(120, 200);

    testPipeline.run().waitUntilFinish();
  }

}