package com.briomar.chatflow;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserResponse {
    @SerializedName("page")
    public int page;

    @SerializedName("per_page")
    public int perPage;

    @SerializedName("total")
    public int total;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("data")
    public List<User> data;

    public static class User {
        @SerializedName("id")
        public int id;

        @SerializedName("email")
        public String email;

        @SerializedName("first_name")
        public String firstName;

        @SerializedName("last_name")
        public String lastName;

        @SerializedName("avatar")
        public String avatar;
    }
}

