package com.batton.projectservice.dto.issue;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetIssueBoardResDTO {
    private List<GetIssueBoardInfoResDTO> todoList = new ArrayList<>();
    private List<GetIssueBoardInfoResDTO> progressList = new ArrayList<>();
    private List<GetIssueBoardInfoResDTO> reviewList = new ArrayList<>();
    private List<GetIssueBoardInfoResDTO> doneList = new ArrayList<>();

    @Builder
    public GetIssueBoardResDTO (List<GetIssueBoardInfoResDTO> todoList, List<GetIssueBoardInfoResDTO> progressList, List<GetIssueBoardInfoResDTO> reviewList, List<GetIssueBoardInfoResDTO> doneList) {
        this.todoList = todoList;
        this.progressList = progressList;
        this.reviewList = reviewList;
        this.doneList = doneList;
    }
}
