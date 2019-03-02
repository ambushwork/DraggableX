package com.netatmo.ylu.draggablex;

import java.util.ArrayList;
import java.util.List;

public class MockDataGenerator {

    public static List<MockItem> generateMockList(int size) {
        List<MockItem> mockItems = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mockItems.add(new MockItem(String.valueOf(i), "name" + String.valueOf(i)));
        }
        return mockItems;
    }


    public static class MockItem {
        private final String id;
        private final String name;

        public MockItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
