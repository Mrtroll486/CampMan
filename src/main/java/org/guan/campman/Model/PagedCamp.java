package org.guan.campman.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PagedCamp {
    @JsonProperty("page_index")
    private int pageIndex;
    @JsonProperty("total_pages")
    private int totalPages;
    private List<Camp> camps;

    public PagedCamp() {}

    public PagedCamp(int pageIndex, int totalPages, List<Camp> camps) {
        this.pageIndex = pageIndex;
        this.totalPages = totalPages;
        this.camps = camps;
    }
}
