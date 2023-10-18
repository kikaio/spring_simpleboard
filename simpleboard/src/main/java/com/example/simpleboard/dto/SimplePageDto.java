package com.example.simpleboard.dto;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SimplePageDto<T extends Page> {

    //단순 Page 객체를 못넘기고 page의 단순정보만을 활용해야 하는 경우 사용할 class
    private int totalPages = 0;
    private long totalElements = 0;
    private int number;
    private int size;
    private boolean hasContent = false;
    private boolean isFirst = false;
    private boolean isLast = false;
    private boolean hasNext = false;
    private boolean hasPrevious = false;
    private int previousOrFirstPageIdx = 0;
    private int previousPageIdx = 0;
    private int nextPageIdx = 0;
    private int nextOrLastPageIdx = 0;
    private Sort sort = null;
    private String mapingUrl ="";

    //html 에서 노출되는 page는 인간친화적으로 1부터 보여주되 url page 요청은 idx 기반으로 요청할 수 있도록 pair를 활용[mustache 가 logicless라서...]
    private ArrayList<Pair<Integer, Integer>> pageIndexes = new ArrayList<>();

    public SimplePageDto(T page)
    {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.hasContent = page.hasContent();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        
        if(this.hasPrevious)
        {
            this.previousOrFirstPageIdx = page.previousOrFirstPageable().getPageNumber();
            this.previousPageIdx = page.previousPageable().getPageNumber();
        }
        if(this.hasNext)
        {
            this.nextPageIdx = page.nextPageable().getPageNumber();
            this.nextOrLastPageIdx = page.nextOrLastPageable().getPageNumber();
        }

        this.sort = page.getSort();

        if(this.hasContent)
        {
            int pageBarStartIdx = ((int)(this.number/this.size));
            for(int idx = pageBarStartIdx; idx < pageBarStartIdx+this.size; idx++)
            {
                pageIndexes.add(Pair.of(idx, idx+1));
            }
        }
    }
}
