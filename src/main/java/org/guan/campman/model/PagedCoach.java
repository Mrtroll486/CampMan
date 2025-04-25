package org.guan.campman.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PagedCoach {
    @JsonProperty("page_index")
    private int pageIndex;
    @JsonProperty("total_pages")
    private int totalPages;
    private List<Camp> camps;

    public PagedCoach() {}

    public PagedCoach(int pageIndex, int totalPages, List<Camp> camps) {
        this.pageIndex = pageIndex;
        this.totalPages = totalPages;
        this.camps = camps;
    }
}
