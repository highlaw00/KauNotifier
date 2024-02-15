package kauproject.kaunotifier.redis;

import kauproject.kaunotifier.controller.SubscriptionForm;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
public class RedisObjectTest {

    @Autowired
    private RedisService redisService;

    @Test
    void saveAndGetObject() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", "choiyool00@gmail.com");
        hashMap.put("name", "최율");

        redisService.setHashOps("choiyool00@gmail.com", hashMap);

        String email = redisService.getHashOps("choiyool00@gmail.com", "email");
        String name = redisService.getHashOps("choiyool00@gmail.com", "name");

        assertThat(email).isEqualTo("choiyool00@gmail.com");
        assertThat(name).isEqualTo("최율");
    }
}
