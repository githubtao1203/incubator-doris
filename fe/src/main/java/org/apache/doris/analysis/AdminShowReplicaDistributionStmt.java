// Copyright (c) 2018, Baidu.com, Inc. All Rights Reserved

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.analysis;

import org.apache.doris.catalog.Catalog;
import org.apache.doris.catalog.Column;
import org.apache.doris.catalog.ColumnType;
import org.apache.doris.cluster.ClusterNamespace;
import org.apache.doris.common.AnalysisException;
import org.apache.doris.common.ErrorCode;
import org.apache.doris.common.ErrorReport;
import org.apache.doris.common.UserException;
import org.apache.doris.mysql.privilege.PrivPredicate;
import org.apache.doris.qe.ConnectContext;
import org.apache.doris.qe.ShowResultSetMetaData;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public class AdminShowReplicaDistributionStmt extends ShowStmt {
    public static final ImmutableList<String> TITLE_NAMES = new ImmutableList.Builder<String>()
            .add("BackendId").add("ReplicaNum").add("Graph").add("Percent").build();

    private TableRef tblRef;
    private List<String> partitions = Lists.newArrayList();

    public AdminShowReplicaDistributionStmt(TableRef tblRef) {
        this.tblRef = tblRef;
    }

    @Override
    public void analyze(Analyzer analyzer) throws AnalysisException, UserException {
        super.analyze(analyzer);

        // check auth
        if (!Catalog.getCurrentCatalog().getAuth().checkGlobalPriv(ConnectContext.get(), PrivPredicate.ADMIN)) {
            ErrorReport.reportAnalysisException(ErrorCode.ERR_SPECIFIC_ACCESS_DENIED_ERROR, "ADMIN");
        }

        String dbName = null;
        if (Strings.isNullOrEmpty(tblRef.getName().getDb())) {
            dbName = analyzer.getDefaultDb();
            if (Strings.isNullOrEmpty(dbName)) {
                ErrorReport.reportAnalysisException(ErrorCode.ERR_NO_DB_ERROR);
            }
        } else {
            dbName = ClusterNamespace.getFullName(getClusterName(), tblRef.getName().getDb());
        }

        tblRef.getName().setDb(dbName);

        if (tblRef.getPartitions() != null && !tblRef.getPartitions().isEmpty()) {
            partitions.addAll(tblRef.getPartitions());
        }
    }

    public String getDbName() {
        return tblRef.getName().getDb();
    }

    public String getTblName() {
        return tblRef.getName().getTbl();
    }

    public List<String> getPartitions() {
        return partitions;
    }

    @Override
    public ShowResultSetMetaData getMetaData() {
        ShowResultSetMetaData.Builder builder = ShowResultSetMetaData.builder();
        for (String title : TITLE_NAMES) {
            builder.addColumn(new Column(title, ColumnType.createVarchar(30)));
        }
        return builder.build();
    }
}