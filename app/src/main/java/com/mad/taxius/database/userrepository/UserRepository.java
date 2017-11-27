package com.mad.taxius.database.userrepository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.mad.taxius.R;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.model.Journey;
import com.mad.taxius.model.User;
import com.mad.taxius.util.Encryptor;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.field;
import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Repository class for user data and it is singleton which is initiated only one time
 * throughout the application life cycle
 */
@Singleton
public class UserRepository {

    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PASSWORD = "password";

    private MobileServiceClient mClient;
    private MobileServiceTable<User> mUserTable;
    private Context mContext;

    @Inject
    public UserRepository(Context context, MobileServiceClient client) {
        this.mContext = context;
        this.mClient = client;
        if (mClient != null) mUserTable = mClient.getTable(User.class);
    }

    /**
     * Login with google account's basic profiles of email and name
     *
     * @param email    email value of google account verified
     * @param name     name value of google account verified
     * @param callback callback interface
     */
    public void loginWithGoogle(String email, String name,
                                final UserRepositoryContract.LoginCallback callback) {
        User userToLogin = new User();
        userToLogin.setEmail(email);
        userToLogin.setName(name);
        userToLogin.setIsSocialLogin(true);
        new LoginWithGoogleAsyncTask(callback).execute(userToLogin);
    }

    /**
     * Async class that performs login logic
     */
    private class LoginWithGoogleAsyncTask extends AsyncTask<User, Void, User> {

        private UserRepositoryContract.LoginCallback mLoginCallback;

        public LoginWithGoogleAsyncTask(UserRepositoryContract.LoginCallback callback) {
            mLoginCallback = callback;
        }

        /**
         * Validate the login details with user repository on another thread
         *
         * @param params user details entered
         * @return user object validated or null for invalid details
         */
        @Override
        protected User doInBackground(User... params) {
            User userToLogin = params[0];
            try {
                Log.d(Constant.Debug.TAG, "Login with google account");
                if (hasEmailInTable(userToLogin.getEmail())) return userToLogin;
                else return addUserInTable(userToLogin);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(Constant.Debug.TAG, "Error from logging with google account");
                return null;
            }
        }

        /**
         * Perform the logic based on the results of the logging
         *
         * @param user user object validated
         */
        @Override
        protected void onPostExecute(User user) {
            if (user == null) mLoginCallback.onLoginFail();
            else mLoginCallback.onLoginSuccess(user);
        }
    }

    /**
     * Add new user data to the database
     *
     * @param user user to be saved
     * @return user object saved
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private User addUserInTable(User user) throws ExecutionException, InterruptedException {
        if (!encryptPasswordForUser(user)) return null;
        User insertedUser = mUserTable.insert(user).get();
        return insertedUser;
    }

    /**
     * Encrypt the password before saving to the database
     *
     * @param user is object that encrypts its password
     * @return boolean for whether encryption is successful
     */
    private boolean encryptPasswordForUser(User user) {
        String encryptedPassword = Encryptor.encrypt(user.getPassword());
        if (encryptedPassword == null) return false;
        user.setPassword(encryptedPassword);
        return true;
    }

    /**
     * Checks if user email entered by user in in the database
     *
     * @param email user input of email
     * @return boolean for whether the email exists in the database
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private boolean hasEmailInTable(String email) throws ExecutionException, InterruptedException {
        List<User> users = mUserTable.where().field(COLUMN_EMAIL).eq(email)
                .select(COLUMN_EMAIL).execute().get();
        return users.size() > 0;
    }

    /**
     * Login with the login details that users entered manually
     *
     * @param email    user input of email
     * @param password user input of password
     * @param callback callback interface
     */
    public void loginWithDetails(String email, String password,
                                 final UserRepositoryContract.LoginCallback callback) {
        User userToCompare = new User(email, password);
        new LoginAsyncTask(callback).execute(userToCompare);
    }

    /**
     * Async class to perform logging with the login details that users entered manually
     */
    private class LoginAsyncTask extends AsyncTask<User, Void, User> {

        private UserRepositoryContract.LoginCallback mLoginCallback;

        public LoginAsyncTask(UserRepositoryContract.LoginCallback callback) {
            mLoginCallback = callback;
        }

        /**
         * Checks if user's login details are valid on another thread
         *
         * @param params user details input
         * @return validated user object or null if invalid
         */
        @Override
        protected User doInBackground(User... params) {
            try {
                Log.d(Constant.Debug.TAG, "Try to match user details against Database");
                return hasMatchedUserInTable(params[0]);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(Constant.Debug.TAG, "Error from matching login details against Database");
                return null;
            }
        }

        /**
         * Perform the logic based on the results of logging
         *
         * @param user validated user or null if invalid
         */
        @Override
        protected void onPostExecute(User user) {
            if (user == null) mLoginCallback.onLoginFail();
            else mLoginCallback.onLoginSuccess(user);
        }
    }

    /**
     * Checks if there is matched user data with login details that are entered by user
     *
     * @param user user object that contains login details
     * @return matched user or null if there is no match
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private User hasMatchedUserInTable(User user) throws ExecutionException, InterruptedException {
        if (!encryptPasswordForUser(user)) return null;
        List<User> users = mUserTable.where().field(COLUMN_EMAIL).eq(user.getEmail())
                .and(field(COLUMN_PASSWORD).eq(user.getPassword()))
                .select(COLUMN_EMAIL, COLUMN_NAME).execute().get();
        if (users.size() != 1) return null;
        else return users.get(0);
    }

    /**
     * Save new user to the database
     *
     * @param user     is the user object to be saved
     * @param callback callback interface
     */
    public void registerNewUser(User user, final UserRepositoryContract.SignUpCallback callback) {
        new AddUserAsyncTask(callback).execute(user);
    }

    /**
     * Async class that adds the user data to the database
     */
    private class AddUserAsyncTask extends AsyncTask<User, Void, User> {

        private UserRepositoryContract.SignUpCallback mSignUpCallback;

        public AddUserAsyncTask(UserRepositoryContract.SignUpCallback callback) {
            mSignUpCallback = callback;
        }

        @Override
        protected User doInBackground(User... params) {
            try {
                Log.d(Constant.Debug.TAG, "Save new user to the Azure Database");
                User userToInsert = params[0];
                if (hasEmailInTable(userToInsert.getEmail())) return null;
                else return addUserInTable(userToInsert);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(Constant.Debug.TAG, "Error from saving new user to Azure Database");
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            if (user == null) mSignUpCallback.onSignUpFail();
            else mSignUpCallback.onSignUpSuccess(user);
        }
    }


}
