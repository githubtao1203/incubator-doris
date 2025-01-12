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

#include "olap/page_cache.h"

#include <gtest/gtest.h>

namespace doris {

class StoragePageCacheTest : public testing::Test {
public:
    StoragePageCacheTest() { }
    virtual ~StoragePageCacheTest() {
    }
};

TEST(StoragePageCacheTest, normal) {
    StoragePageCache cache(10 * 1024);

    StoragePageCache::CacheKey key("abc", 0);

    char* buf = new char[1024];
    {
        PageCacheHandle handle;
        Slice data(buf, 1024);
        cache.insert(key, data, &handle);

        ASSERT_EQ(handle.data().data, buf);
    }
    // cache hit
    {
        PageCacheHandle handle;
        auto found = cache.lookup(key, &handle);
        ASSERT_TRUE(found);
        ASSERT_EQ(buf, handle.data().data);
    }
    // cache miss
    {
        PageCacheHandle handle;
        StoragePageCache::CacheKey miss_key("abc", 1);
        auto found = cache.lookup(miss_key, &handle);
        ASSERT_FALSE(found);
    }
    // put too many page to eliminate first page
    for (int i = 0; i < 10; ++i) {
        StoragePageCache::CacheKey key("bcd", i);
        PageCacheHandle handle;
        Slice data(new char[1024], 1024);
        cache.insert(key, data, &handle);
    }
    // cache miss for eliminated key
    {
        PageCacheHandle handle;
        auto found = cache.lookup(key, &handle);
        ASSERT_FALSE(found);
    }
}

} // namespace doris

int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv); 
    return RUN_ALL_TESTS();
}
