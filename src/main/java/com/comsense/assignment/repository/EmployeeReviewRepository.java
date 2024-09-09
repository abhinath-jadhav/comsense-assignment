package com.comsense.assignment.repository;

import com.comsense.assignment.models.EmployeeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeReviewRepository extends JpaRepository<EmployeeReview, Long> {

    List<EmployeeReview> findAllByEmployeeIdIn(List<String> list);
}
