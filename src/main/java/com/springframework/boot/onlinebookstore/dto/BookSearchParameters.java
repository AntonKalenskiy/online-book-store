package com.springframework.boot.onlinebookstore.dto;

public record BookSearchParameters(String[] authors, String[] titles) {
}