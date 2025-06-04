package com.example.userserver.follow;

import com.example.userserver.user.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public boolean isFollow(int userId, int followerId) {
        Follow follow = followRepository.findByUserIdAndFollowerId(userId, followerId);
        return follow != null;
    }

    @Transactional
    public Follow followUser(int userId, int followerId) {
        if (isFollow(userId, followerId)) {
            return null;
        }

        sendFollowerMessage(userId, followerId, true);

        return followRepository.save(new Follow(userId, followerId));
    }

    @Transactional
    public boolean unfollowUser(int userId, int followerId) {
        Follow follow = followRepository.findByUserIdAndFollowerId(userId, followerId);
        if (follow == null) {
            return false;
        }

        sendFollowerMessage(userId, followerId, false);
        followRepository.delete(follow);
        return true;
    }

    private void sendFollowerMessage(int userId, int followerId, boolean isFollow) {
        FollowMessage followMessage = new FollowMessage(userId, followerId, isFollow);

        try {
            kafkaTemplate.send("user.follower", objectMapper.writeValueAsString(followMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserInfo> listFollower(int userId) {
        return followRepository.findFollowingByUserId(userId);
    }

    public List<UserInfo> listFollowing(int userId) {
        return followRepository.findFollowingByUserId(userId);
    }

}