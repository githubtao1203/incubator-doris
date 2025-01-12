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

#pragma once

#include <string>
#include <vector>

#include "olap/olap_define.h"
#include "util/uid_util.h"

namespace doris {

struct StorePath {
    StorePath() : capacity_bytes(-1) { }
    StorePath(const std::string& path_, int64_t capacity_bytes_)
        : path(path_), capacity_bytes(capacity_bytes_) { }
    std::string path;
    int64_t capacity_bytes;
};

OLAPStatus parse_conf_store_paths(const std::string& config_path, std::vector<StorePath>* path);

struct EngineOptions {
    // list paths that tablet will be put into.
    std::vector<StorePath> store_paths;
    UniqueId backend_uid;
};

}
