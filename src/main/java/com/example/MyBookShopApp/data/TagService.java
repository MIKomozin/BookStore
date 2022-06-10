package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.entity.Tag;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagService {

    private TagRepository tagRepository;
    private BookRepository bookRepository;

    @Autowired
    public TagService(TagRepository tagRepository, BookRepository bookRepository) {
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
    }

    //для отображения всех тегов из БД
    public List<Tag> getTagData() {
        return tagRepository.findAll();
    }

    //2.3
    //для тэга находить его имя по ID
    public Tag getTagByTagId(Integer tagId) {
        return tagRepository.findTagByTagId(tagId);
    }

    public Page<Book> getBooksByTagId(Integer tagId, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return  bookRepository.findBooksByTagId(getTagByTagId(tagId).getId(),nextPage);
    }
}

