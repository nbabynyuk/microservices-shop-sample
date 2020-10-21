package com.nb.feedbacks.controllers.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EmptyResponseSerializer.class)
public class EmptyResponse {
}
