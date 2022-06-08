package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    //для отображения всех тегов из БД
    public List<Tag> getTagData() {
        return tagRepository.findAll();
    }


}

/*
public Page<Book> getBooksByTagName(String tagName, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return tagRepository.findBooksByTagName(tagName, nextPage);
    }

     public Page<Book> getBooksByTag(String tagName, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<Integer> bookId = getBookIdByTagId(tagName).stream().map(Book2Tag::getId).collect(Collectors.toList());
        return tagRepository.findBooksById(bookId, nextPage);
    }

    public List<Book2Tag> getBookIdByTagId(String tagName) {
        return tagRepository.findBookIdByTagId(getTagByTagName(tagName).getId());
    }

    public Page<Book> getBooksByTag(String tagName, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<Book> books = getBook2TagByTag(tagName).stream().map(Book2Tag::getBook).collect(Collectors.toList());
        return books;
    }

    public Tag getTagByTagName(String tagName) {
        return tagRepository.findTagByTagName(tagName);
    }

    public List<Book2Tag> getBook2TagByTag(String tagName) {
        return getTagByTagName(tagName).getTag2Book();
    }

    public List<Book> getBooksByTag(String tagName) {
        List<Book> books = getBook2TagByTag(tagName).stream().map(Book2Tag::getBook).collect(Collectors.toList());
        return books;
    }
 */
