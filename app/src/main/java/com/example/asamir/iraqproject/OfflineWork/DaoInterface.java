package com.example.asamir.iraqproject.OfflineWork;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.asamir.iraqproject.OfflineWork.Entities.CityEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.DistricEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.GovEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.JobEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.OfficeEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.SurvayEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.UserProjectsEntity;

import java.util.List;

@Dao
public interface DaoInterface {

    //--------  govs work
    @Insert
    void insertGov(GovEntity govEntity);
    @Query("SELECT * FROM govTable")
    List<GovEntity> getGovs();
    @Query("DELETE FROM govTable")
    void deleteGovData();


    //--------  cityWork
    @Insert
    void insertCity(CityEntity cityEntity);
    @Query("SELECT * FROM cityTable")
    List<CityEntity> getAllCities();
    @Query("SELECT * FROM cityTable WHERE govId =:govId")
    List<CityEntity> getCityBygovId(String govId);
    @Query("DELETE FROM cityTable")
    void deleteCityData();

    // ------------- district work
    @Insert
    void insertDistric(DistricEntity districEntity);
    @Query("SELECT * FROM districTable")
    List<DistricEntity> getAllDistrict();
    @Query("SELECT * FROM districTable WHERE cityId =:cityId")
    List<DistricEntity> getDistricByCityId(String cityId);
    @Query("DELETE FROM districTable")
    void deleteDistrictsData();

    //----------- office work
    @Insert
    void insertoffice(OfficeEntity officeEntity);
    @Query("SELECT * FROM officeTable")
    List<OfficeEntity> getAllOfficeies();
    @Query("SELECT * FROM officeTable WHERE  districId=:districId ")
    List<OfficeEntity> getOfficeByDistricId(String districId);
    @Query("SELECT * FROM officeTable WHERE  districId=:districId and project_id=:projectId and officeVisit=0 ")
    //@Query("SELECT * FROM officeTable WHERE  districId=:districId and project_id=:projectId ")
    List<OfficeEntity> getOfficeByDistricIdandProjectId(String districId,String projectId);
    @Query("SELECT * FROM officeTable WHERE  districId=:districId and project_id=:projectId and officeVisit=1 ")
    //@Query("SELECT * FROM officeTable WHERE  districId=:districId and project_id=:projectId  ")
    List<OfficeEntity> getOfficeByDistricIdandProjectId2(String districId,String projectId);
    @Query("DELETE FROM officeTable")
    void deleteOfficeData();


    //----  Survay Screen
    @Insert
    void insertSurvay(SurvayEntity survayEntity);
    @Query("SELECT * FROM survayTable")
    List<SurvayEntity> getAllSurvies();
    @Query("SELECT * FROM survayTable WHERE isUploaded =:isUploaded")
    List<SurvayEntity> getSurvayByIsUploaded(String isUploaded);
    @Query("UPDATE survayTable SET isUploaded =:isuploaded WHERE id =:id")
    void update(String isuploaded, int id);
    //------User Projects

    @Insert
    void insertProjects(UserProjectsEntity survayEntity);
    @Query("SELECT * FROM userProjects")
    List<UserProjectsEntity> getAllUserProjects();
    @Query("SELECT * FROM userProjects WHERE userId =:userId")
    List<UserProjectsEntity> getUserProjectsbyId(String userId);
    @Query("DELETE FROM userProjects")
    void deleteUserProjectsData();


    //--------  gobs work
    @Insert
    void insertJob(JobEntity jobEntity);
    @Query("SELECT * FROM jobTable ")
    List<JobEntity> getJobs();
    @Query("SELECT * FROM jobTable where projectId=:projectId")
    List<JobEntity> getJobsByProject(String projectId);
    @Query("DELETE FROM jobTable")
    void deleteJobData();


}
