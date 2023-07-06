package com.newsfeed.board.user.entity;
public enum UserRoleEnum {

    NONUSER(Authority.NONUSER),
    USER(Authority.USER);


    private final String authority;

    UserRoleEnum(String authority){this.authority = authority;}
    public String getAuthority(){return this.authority;}

    public static class Authority{
        private static final String NONUSER = "ROLE_NONUSER";
        private static final String USER = "ROLE_USER";
    }
}
