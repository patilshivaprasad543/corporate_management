package com.corporate.travel.dto;

public class OrganizationDto {

    public static class CompanyOption {
        private Long id;
        private String name;
        private String code;
        private String country;
        private String currencyCode;

        public CompanyOption() {}

        public CompanyOption(Long id, String name, String code, String country, String currencyCode) {
            this.id = id;
            this.name = name;
            this.code = code;
            this.country = country;
            this.currencyCode = currencyCode;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getCurrencyCode() { return currencyCode; }
        public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    }

    public static class DepartmentOption {
        private Long id;
        private String name;
        private String code;

        public DepartmentOption() {}

        public DepartmentOption(Long id, String name, String code) {
            this.id = id;
            this.name = name;
            this.code = code;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }
}
