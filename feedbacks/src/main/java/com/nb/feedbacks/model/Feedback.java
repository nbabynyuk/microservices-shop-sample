package com.nb.feedbacks.model;

import lombok.Data;

@Data
public class Feedback {

    private String createdTime;
    private String modifiedTime;
    private String feedbackUUID;

    private String feedbackMessage;
    private Long userId;
    private String userDisplayName;
}
