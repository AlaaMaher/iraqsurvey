package com.example.asamir.iraqproject.OfflineLogin;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

class UsersRepository {

    private UserDao mUserDao;
    private LiveData<User> mUser;
    UsersRepository(Application application ) {
        UsersRoomDatabase db = UsersRoomDatabase.getDatabase(application);
        mUserDao = db.wordDao();

    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData< User> getUser(String email , String password) {
        mUser = mUserDao.getUser(email  , password);
        return mUser;
    }


    void insert(User user) {
        new insertAsyncTask(mUserDao).execute(user);
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
