package com.neonlab.product.constants;

public class EntityConstant {

    public static class Product{
        public static final String NAME = "name";
        public static final String CATEGORY= "category";
        public static final String SUB_CATEGORY= "subCategory";
        public static final String SUB_CATEGORY2= "subCategory2";
        public static final String BRAND = "brand";
        public static final String CODE = "code";
        public static final String VARIETIES = "varieties";
        public static final String ENTITY_NAME = "Product";
    }
    public static class Variety{
        public static final String PRODUCT = "product";
        public static final String PRICE = "price";
        public static final String DESCRIPTION = "description";
        public static final String QUANTITY = "quantity";
        public static final String ENTITY_NAME = "Variety";
    }

    public static class Order{
        public static final String USER_ID = "userId";
        public static final String BOUGHT_PRODUCT_DETAILS = "boughtProductDetails";
        public static final String TOTAL_COST = "totalCost";
        public static final String ORDER_STATUS = "orderStatus";
        public static final String ADDRESS_ID = "addressId";
        public static final String PAYMENT_MODE = "paymentMode";
    }

    public static class Address{
        public static final String CITY = "city";
        public static final String STATE = "state";
    }

}
