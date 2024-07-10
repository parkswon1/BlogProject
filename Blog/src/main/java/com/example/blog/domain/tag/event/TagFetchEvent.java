package com.example.blog.domain.tag.event;

import com.example.blog.domain.tag.entity.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
@Setter
public class TagFetchEvent extends ApplicationEvent {
    private final List<String> tagName;
    private List<Tag> tags;

    public TagFetchEvent(Object source, List<String> tagName) {
        super(source);
        this.tagName = tagName;
    }
}
