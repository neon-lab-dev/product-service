//package com.neonlab.product.models.responses;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.neonlab.common.models.searchCriteria.PageableSearchCriteria;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class PageableResponse<T> {
//
//    private int perPage;
//    private int pageNo;
//    private String sortBy;
//    private String sortDirection;
//    private List<T> content;
//
//    public PageableResponse(
//            final List<T> resultList,
//            final PageableSearchCriteria searchCriteria
//    ){
//        this.perPage = searchCriteria.getPerPage();
//        this.pageNo = searchCriteria.getPageNo();
//        this.sortBy = searchCriteria.getSortBy();
//        this.sortDirection = searchCriteria.getSortDirection().name();
//        this.content = resultList;
//    }
//
//}
