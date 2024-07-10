package com.example.blog.domain.tag.service.impl;

import com.example.blog.domain.tag.entity.Tag;
import com.example.blog.domain.tag.repository.TagRepository;
import com.example.blog.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> createTags(List<String> names) {
        List<Tag> tags = new ArrayList<>();
        for (String name : names) {
            Tag tag = tagRepository.findByName(name).orElseGet(() -> {
                Tag newTag = new Tag();
                newTag.setName(name);
                return tagRepository.save(newTag);
            });
            tags.add(tag);
        }
        return tags;
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with id: " + id));
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with name: " + name));
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }
}
