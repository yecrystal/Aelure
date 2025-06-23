package com.aelure.controller;

import com.aelure.model.*;
import com.aelure.service.SearchService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/web-text")
    public ResponseEntity<List<FashionItem>> searchWebByText(@RequestBody TextSearchRequest request) {
        return ResponseEntity.ok(searchService.searchWebByText(request.getQuery()));
    }
}