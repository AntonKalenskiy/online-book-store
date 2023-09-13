package com.springframework.boot.onlinebookstore.dto.book;

public record BookSearchParameters(String[] authors, String[] titles) {
}
