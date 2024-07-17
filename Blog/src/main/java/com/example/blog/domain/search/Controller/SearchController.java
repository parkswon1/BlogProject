package com.example.blog.domain.search.Controller;

import com.example.blog.domain.search.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SearchController {
    private final ElasticsearchService elasticsearchService;

    @GetMapping("/search")
    public List<Map<String, Object>> searchAll(@RequestParam String query) throws IOException {
        System.out.println("start");
        List<Map<String, Object>> result = elasticsearchService.searchAll(query);
        System.out.println(result);
        return result;
    }
}