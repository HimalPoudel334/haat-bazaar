package com.example.testapp.basetypes;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Location {
    private final String country;
    private final String province;
    private final String district;
    private final String municipality;
    private final String toleName;
    private final int wardNo;
    private String zipCode;

    private Location(LocationBuilder builder) {
        this.country = builder.country;
        this.province = builder.province;
        this.district = builder.district;
        this.municipality = builder.municipality;
        this.toleName = builder.toleName;
        this.wardNo = builder.wardNo;
        this.zipCode = builder.zipCode;
    }

    public static class LocationBuilder {
        private String country = "Nepal";
        private String province;
        private String district;
        private String municipality;
        private String toleName;
        private int wardNo;
        private String zipCode;


        public LocationBuilder province(String province) {
            this.province = province;
            return this;
        }

        public LocationBuilder district(String district) {
            this.district = district;
            return this;
        }

        public LocationBuilder municipality(String municipality) {
            this.municipality = municipality;
            return this;
        }

        public LocationBuilder wardNo(int wardNo) {
            this.wardNo = wardNo;
            return this;
        }

        public LocationBuilder toleName(String toleName) {
            this.toleName = toleName;
            return this;
        }

        public LocationBuilder country(String country) {
            this.country = country;
            return this;
        }

        public LocationBuilder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Location build() {
            if (province == null || district == null || municipality == null || toleName == null || wardNo <= 0) {
                throw new IllegalStateException("All fields except country must be set.");
            }
            return new Location(this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s - %d - %s, %s, %s, %s, %s",
                municipality, wardNo, toleName, zipCode, district, province, country);
    }
}
