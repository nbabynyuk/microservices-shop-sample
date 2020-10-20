package com.nb.feedbacks.controllers;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EmptyResponseSerializer.class)
public class EmptyResponse {
}
