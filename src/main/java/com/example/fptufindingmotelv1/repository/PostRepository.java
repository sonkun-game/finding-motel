package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PostModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostModel,Long> {
    List<PostModel> findByVisibleTrue(Sort sort);
}
