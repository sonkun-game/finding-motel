package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PostModel;
import com.restfb.types.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostModel,Long> {
}
