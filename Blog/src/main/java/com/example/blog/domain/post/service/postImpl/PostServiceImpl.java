package com.example.blog.domain.post.service.postImpl;

import com.example.blog.domain.blog.entity.Blog;
import com.example.blog.domain.blog.event.BlogFetchEvent;
import com.example.blog.domain.category.entity.Category;
import com.example.blog.domain.category.event.CategoryFetchEvent;
import com.example.blog.domain.image.event.ImageSaveEvent;
import com.example.blog.domain.post.dto.PostRequest;
import com.example.blog.domain.post.entity.Post;
import com.example.blog.domain.post.event.PostViewIncrementEvent;
import com.example.blog.domain.post.repository.PostRepository;
import com.example.blog.domain.post.service.PostService;
import com.example.blog.domain.tag.entity.PostTag;
import com.example.blog.domain.tag.entity.Tag;
import com.example.blog.domain.tag.event.TagFetchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Post createPost(Long userId, PostRequest postRequest) {
        BlogFetchEvent blogFetchEvent = new BlogFetchEvent(this, userId);
        eventPublisher.publishEvent(blogFetchEvent);
        Blog blog = blogFetchEvent.getBlog();

        CategoryFetchEvent categoryFetchEvent = new CategoryFetchEvent(this, postRequest.getCategoryId());
        eventPublisher.publishEvent(categoryFetchEvent);
        Category category = categoryFetchEvent.getCategory();

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setBlog(blog);
        post.setCategory(category);

        if (postRequest.getImageFile() != null && !postRequest.getImageFile().isEmpty()) {
            ImageSaveEvent event = new ImageSaveEvent(postRequest.getImageFile(), post::setImage);
            eventPublisher.publishEvent(event);
        }

        postRepository.save(post);

        if (postRequest.getTagName() != null && !postRequest.getTagName().isEmpty()) {
            TagFetchEvent tagFetchEvent = new TagFetchEvent(this, postRequest.getTagName());
            eventPublisher.publishEvent(tagFetchEvent);
            List<Tag> tags = tagFetchEvent.getTags();
            for (Tag tag : tags) {
                PostTag postTag = new PostTag();
                postTag.setPost(post);
                postTag.setTag(tag);
                post.getPostTags().add(postTag);
            }
        }
        return postRepository.save(post);
    }

    @Override
    public Post getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다." + postId));
        PostViewIncrementEvent event = new PostViewIncrementEvent(this, postId);
        eventPublisher.publishEvent(event);
        return post;
    }

    @Override
    public Post updatePost(Long postId, PostRequest postRequest) {
        Post post = getPostById(postId);
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        CategoryFetchEvent categoryFetchEvent = new CategoryFetchEvent(this, postRequest.getCategoryId());
        eventPublisher.publishEvent(categoryFetchEvent);
        Category category = categoryFetchEvent.getCategory();

        post.setCategory(category);

        if (postRequest.getImageFile() != null && !postRequest.getImageFile().isEmpty()) {
            ImageSaveEvent event = new ImageSaveEvent(postRequest.getImageFile(), post::setImage);
            eventPublisher.publishEvent(event);
        }

        post.getPostTags().clear();
        if (postRequest.getTagName() != null && !postRequest.getTagName().isEmpty()) {
            TagFetchEvent tagFetchEvent = new TagFetchEvent(this, postRequest.getTagName());
            eventPublisher.publishEvent(tagFetchEvent);
            List<Tag> tags = tagFetchEvent.getTags();
            for (Tag tag : tags) {
                PostTag postTag = new PostTag();
                postTag.setPost(post);
                postTag.setTag(tag);
                post.getPostTags().add(postTag);
            }
        }

        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        postRepository.delete(post);
    }

    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> getPostsByBlogId(Long blogId, Pageable pageable) {
        return postRepository.findByBlogId(blogId, pageable);
    }
}