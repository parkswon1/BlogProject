package com.example.blog.domain.search.service;

import com.example.blog.domain.blog.entity.Blog;
import com.example.blog.domain.blog.repository.BlogRepository;
import com.example.blog.domain.post.controller.PostController;
import com.example.blog.domain.post.entity.Post;
import com.example.blog.domain.post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final RestHighLevelClient client;

    private final ObjectMapper objectMapper;

    private final BlogRepository blogRepository;

    private final PostRepository postRepository;
    private final PostController postController;

    @PostConstruct
    @Transactional
    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void scheduleIndexing() throws IOException {
        indexAllBlogsAndPosts();
    }

    @Transactional
    public void indexAllBlogsAndPosts() throws IOException {
        List<Blog> blogs = blogRepository.findAllWithAssociations();
        for (Blog blog : blogs) {
            indexBlog(blog);
        }

        List<Post> posts = postRepository.findAllWithAssociations();
        for (Post post : posts) {
            indexPost(post);
        }
    }

    public String indexBlog(Blog blog) throws IOException {
        Map<String, Object> blogMap = objectMapper.convertValue(blog, Map.class);
        IndexRequest indexRequest = new IndexRequest("blogs").id(blog.getId().toString())
                .source(blogMap, XContentType.JSON);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        return indexResponse.getId();
    }

    public String indexPost(Post post) throws IOException {
        Map<String, Object> postMap = objectMapper.convertValue(post, Map.class);
        IndexRequest indexRequest = new IndexRequest("posts").id(post.getId().toString())
                .source(postMap, XContentType.JSON);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        return indexResponse.getId();
    }

    public List<Map<String, Object>> searchAll(String query) throws IOException {
        SearchRequest postSearchRequest = new SearchRequest("posts");
        SearchSourceBuilder postSearchSourceBuilder = new SearchSourceBuilder();
        postSearchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, "title", "content", "postTags.tag"));
        postSearchRequest.source(postSearchSourceBuilder);

        SearchRequest blogSearchRequest = new SearchRequest("blogs");
        SearchSourceBuilder blogSearchSourceBuilder = new SearchSourceBuilder();
        blogSearchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, "title", "description"));
        blogSearchRequest.source(blogSearchSourceBuilder);

        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
        multiSearchRequest.add(postSearchRequest);
        multiSearchRequest.add(blogSearchRequest);

        MultiSearchResponse response = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);
        List<Map<String, Object>> results = new ArrayList<>();
        for (MultiSearchResponse.Item item : response.getResponses()) {
            results.addAll(extractSearchResults(item.getResponse()));
        }
        return results;
    }

    private List<Map<String, Object>> extractSearchResults(SearchResponse response) {
        List<Map<String, Object>> results = new ArrayList<>();
        response.getHits().forEach(hit -> results.add(hit.getSourceAsMap()));
        return results;
    }
}