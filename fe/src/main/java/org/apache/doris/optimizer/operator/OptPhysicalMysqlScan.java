// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.optimizer.operator;

import org.apache.doris.optimizer.base.DistributionEnforcerProperty;
import org.apache.doris.optimizer.base.OptColumnRefSet;
import org.apache.doris.optimizer.base.OrderEnforcerProperty;
import org.apache.doris.optimizer.base.RequiredPhysicalProperty;

public class OptPhysicalMysqlScan extends OptPhysical {

    public OptPhysicalMysqlScan() {
        super(OptOperatorType.OP_PHYSICAL_MYSQL_SCAN);
    }

    @Override
    public OrderEnforcerProperty getChildReqdOrder(OptExpressionHandle handle, OrderEnforcerProperty reqdOrder, int childIndex) {
        return null;
    }

    @Override
    public DistributionEnforcerProperty getChildReqdDistribution(OptExpressionHandle handle, DistributionEnforcerProperty reqdDistribution, int childIndex) {
        return null;
    }

    @Override
    protected OptColumnRefSet deriveChildReqdColumns(OptExpressionHandle exprHandle, RequiredPhysicalProperty property, int childIndex) {
        return null;
    }
}