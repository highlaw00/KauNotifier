package kauproject.kaunotifier.service;

import kauproject.kaunotifier.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final RedisService redisService;

    public boolean verify(String key, String value) {
        return redisService.getValues(key).equals(value);
    }
}
