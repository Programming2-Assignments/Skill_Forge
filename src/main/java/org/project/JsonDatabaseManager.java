package org.project;
import com.google.gson.*;
import java.io.*;
import java.util.ArrayList;

public class JsonDatabaseManager {
    private static final String USERS_FILE = "users.json";
    private Gson gson;

    public JsonDatabaseManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }


    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);

        if (!file.exists()) {
            return users;
        }

        try (FileReader reader = new FileReader(USERS_FILE)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject userObj = element.getAsJsonObject();
                String role = userObj.get("role").getAsString();

                User user;
                if (role.equals("Student")) {
                    user = gson.fromJson(userObj, Student.class);
                } else {
                    user = gson.fromJson(userObj, Instructor.class);
                }
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }


    public void saveUsers(ArrayList<User> users) {
        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public User findUserByEmail(String email) {
        ArrayList<User> users = loadUsers();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


    public boolean updateUser(User updatedUser) {
        ArrayList<User> users = loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                saveUsers(users);
                return true;
            }
        }
        return false;
    }
}