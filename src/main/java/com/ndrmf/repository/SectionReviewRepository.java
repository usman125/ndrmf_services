package com.ndrmf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndrmf.model.SectionReview;
import com.ndrmf.user.model.User;

import java.util.List;

public interface SectionReviewRepository extends JpaRepository<SectionReview, Long> {
    List<SectionReview> findAllBySectionReviewer(User user);
}