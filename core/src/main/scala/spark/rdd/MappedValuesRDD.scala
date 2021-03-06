/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spark.rdd


import spark.{TaskContext, Partition, RDD}

private[spark]
class MappedValuesRDD[K, V, U](prev: RDD[_ <: Product2[K, V]], f: V => U)
  extends RDD[(K, U)](prev) {

  override def getPartitions = firstParent[Product2[K, U]].partitions

  override val partitioner = firstParent[Product2[K, U]].partitioner

  override def compute(split: Partition, context: TaskContext): Iterator[(K, U)] = {
    firstParent[Product2[K, V]].iterator(split, context).map { case Product2(k ,v) => (k, f(v)) }
  }
}
