package org.example.dao.InterF;

import org.example.domain.Application;

import java.util.List;

public interface ApplicationDao {
    Application getApplicationById(int id);

    void createApplicationByStudentClassId(int id, int classId) ;

    List<Application> getApplicationByStudentId(int studentId);

    List<Application> getPendingApplications();

    void rejectOrApproveByIdFeedback(String status, int applicationId, String f);

    String getRequestByApplicationId(int applicationId);

    int getStudentIdByApplicationId(int applicationId);

    int getClassIdByApplicationId(int applicationId);

    void createEnrollmentApplication(int studentId, int classId);

    void withdrawApplication(int applicationId);

    void resubmitApplication(int applicationId);

}
