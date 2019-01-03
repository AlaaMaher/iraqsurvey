package com.example.asamir.iraqproject.OfflineWork;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

import com.example.asamir.iraqproject.OfflineWork.Entities.CityEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.DistricEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.GovEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.JobEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.OfficeEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.SurvayEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.UserProjectsEntity;

@android.arch.persistence.room.Database(entities = {
        GovEntity.class,
        CityEntity.class,
        DistricEntity.class,
        OfficeEntity.class,
        SurvayEntity.class,
        UserProjectsEntity.class,
        JobEntity.class
},
        version = 9 )




public abstract class Database extends RoomDatabase {
    public abstract DaoInterface userDao();



    /*
    public static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE jobTable (id INTEGER primary key autoincrement NOT NULL,  "
                    + "jobId TEXT ," + " jobName TEXT )" );
        }
    };
    */






}

