package com.comsense.assignment.service;

import com.comsense.assignment.models.EmployeeReview;
import com.comsense.assignment.repository.EmployeeReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewUpdateBufferService {

    private static final int BATCH_SIZE = 10;
    private final List<EmployeeReview> updateBuffer = new ArrayList<>();

    @Autowired
    private EmployeeReviewRepository employeeReviewRepository;


    public void updateReview(EmployeeReview updatedReview) {
            updateBuffer.add(updatedReview);
            if (updateBuffer.size() >= BATCH_SIZE) {
                saveUpdates();
            }
    }

    @Transactional
    public void saveUpdates() {
            if (!updateBuffer.isEmpty()) {
                employeeReviewRepository.saveAll(updateBuffer);
                updateBuffer.clear();
            }
    }

    @Scheduled(fixedRate = 50 * 60 * 1000)
    public void processScheduledUpdates() {
        saveUpdates();
    }

    @PreDestroy
    public void onShutdown() {
        saveUpdates();
    }
}
