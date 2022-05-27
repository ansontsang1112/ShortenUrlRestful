package com.hypernology.shortenurlrestful.repository;

import com.hypernology.shortenurlrestful.model.User;

import java.util.List;

public interface UserRepository {
    List<User> queryAllUser();
    <T> List<User> queryUserByID(T id);
    <T> List<User> queryUserByDiscordId(T discordId);
    <T> List<User> queryUserByMemberId(T memberId);

    User save(User user);
    <T, V, R> String update(T field, V value, R key);
    <T> String delete(T key);
}
