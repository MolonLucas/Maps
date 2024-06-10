package com.example.myapplication;

import java.util.List;

public class RandomUserResponse {
    public List<Result> results;

    public class Result {
        public Location location;

        public class Location {
            public Street street;
            public Coordinates coordinates;

            public class Street {
                public String name;
            }

            public class Coordinates {
                public String latitude;
                public String longitude;
            }
        }
    }
}