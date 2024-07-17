package com.example.blog.domain.blog.service.impl;

import com.example.blog.domain.blog.entity.Blog;
import com.example.blog.domain.blog.event.BlogViewEvent;
import com.example.blog.domain.blog.repository.BlogRepository;
import com.example.blog.domain.blog.service.BlogService;
import com.example.blog.domain.image.event.ImageSaveEvent;
import com.example.blog.domain.user.entity.User;
import com.example.blog.domain.user.event.UserLoadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final ApplicationEventPublisher eventPublisher;

    //블로그 저장, 생성을 하는 로직
    //user에서 이벤트로 User객체를 받아온다
    @Override
    public void saveBlog(Long userid, String title, String text, MultipartFile imageFile) {
        Optional<Blog> blogOpt = blogRepository.findByUserId(userid);
        Blog blog;
        if (!blogOpt.isPresent()){
            blog = new Blog();
        }else {
            blog = blogOpt.get();
        }

        if (userid != null){
            UserLoadEvent userLoadEvent = new UserLoadEvent(userid);
            eventPublisher.publishEvent(userLoadEvent);
            User user = userLoadEvent.getUser();
            blog.setUser(user);
        }

        if (title != null){
            blog.setTitle(title);
        }

        if (text != null){
            blog.setDescription(text);
        }

        if (imageFile != null && !imageFile.isEmpty()){
            ImageSaveEvent event = new ImageSaveEvent(imageFile, blog::setImage);
            eventPublisher.publishEvent(event);
        }
        blogRepository.save(blog);
    }

    //블로그 블로그 아이디로 가져오는 로직
    @Override
    public Blog getBlogByBlogId(Long blogId, Long viewerId) throws IllegalAccessException {
        Optional<Blog> blogOpt = blogRepository.findById(blogId);
        if (blogOpt.isEmpty()){
            throw new IllegalAccessException(blogId + "에 해당하는 블로그가 없습니다.");
        }
        BlogViewEvent event = new BlogViewEvent(blogOpt.get(), viewerId);
        eventPublisher.publishEvent(event);
        blogOpt.get().getUser().setPassword(null);
        blogOpt.get().getUser().setUsername(null);
        return blogOpt.get();
    }

    //블로그 사용자 이름id로 가져오는 로직
    @Override
    public Blog getBlogByUserId(Long userId, Long viewerId) throws IllegalAccessException {
        Optional<Blog> blogOpt = blogRepository.findByUserId(userId);
        if (blogOpt.isEmpty()){
            throw new IllegalAccessException("user" + userId + "는 블로그를 가지고 있지 않습니다.");
        }
        BlogViewEvent event = new BlogViewEvent(blogOpt.get(), viewerId);
        eventPublisher.publishEvent(event);
        blogOpt.get().getUser().setPassword(null);
        blogOpt.get().getUser().setUsername(null);
        return blogOpt.get();
    }

    //블로그 리스트를 받아오는 로직 페이징 기능을 사용해서 받아온다.
    @Override
    public Page<Blog> getBlogsList(Pageable pageable) throws IllegalAccessException {
        Pageable sortedByDescId = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "updatedAt", "createdAt"));
        Page<Blog> allPage = blogRepository.findAll(sortedByDescId);
        if (allPage.isEmpty()){
            throw new IllegalAccessException("존재하는 블로그가 없습니다.");
        }
        return allPage;
    }

    @Override
    public Blog getBlogById(Long blogId){
        return blogRepository.findById(blogId).orElse(null);
    }
}