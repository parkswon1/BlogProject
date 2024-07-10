package com.example.blog.domain.tag.event;

import com.example.blog.domain.tag.entity.Tag;
import com.example.blog.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TagEventListener {
    private final TagRepository tagRepository;

    @EventListener
    public void handleTagFetchEvent(TagFetchEvent event){
        List<Tag> tags = new ArrayList<>();
        for (String tagName : event.getTagName()){
            Tag tag = tagRepository.findByName(tagName).orElseGet(() ->{
                Tag newTag = new Tag();
                newTag.setName(tagName);
                return tagRepository.save(newTag);
            });
            tags.add(tag);
        }
        event.setTags(tags);
    }
}
