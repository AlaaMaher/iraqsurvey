package com.example.asamir.iraqproject.OfflineWork;

import android.arch.persistence.room.RoomDatabase;

import com.example.asamir.iraqproject.OfflineWork.Entities.CityEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.DistricEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.GovEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.OfficeEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.SurvayEntity;
import com.example.asamir.iraqproject.OfflineWork.Entities.UserProjectsEntity;

@android.arch.persistence.room.Database(entities = {
        GovEntity.class,
        CityEntity.class,
        DistricEntity.class,
        OfficeEntity.class,
        SurvayEntity.class,
        UserProjectsEntity.class
},
        version = 8)
public abstract class Database extends RoomDatabase {
    public abstract DaoInterface userDao();
}
