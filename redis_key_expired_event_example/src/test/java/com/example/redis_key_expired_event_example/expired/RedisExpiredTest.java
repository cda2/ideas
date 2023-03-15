package com.example.redis_key_expired_event_example.expired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.redis_key_expired_event_example.expired.config.EmbeddedRedisConfig;
import com.example.redis_key_expired_event_example.expired.config.RedisConfig;
import com.example.redis_key_expired_event_example.expired.domain.Domain;
import com.example.redis_key_expired_event_example.expired.domain.Dumb;
import com.example.redis_key_expired_event_example.expired.listener.ExpiredListener;
import com.example.redis_key_expired_event_example.expired.repository.DomainRepository;
import com.example.redis_key_expired_event_example.expired.repository.DumbRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
// server 또는 embedded 를 선택한다. 프로필로 설정한다.
@Import({RedisConfig.class, EmbeddedRedisConfig.class})
public class RedisExpiredTest {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DumbRepository dumbRepository;

    // Expire 이벤트를 받는 리스너를 주입받는다.
    @Autowired
    private ExpiredListener expiredListener;

    private final Domain domain = new Domain(1L, "test");
    private final Dumb dumb = new Dumb(1L, 42.0);

    // 테스트가 시작되기 전에 모든 데이터를 삭제하고, 리스너의 카운트를 초기화한다.
    @BeforeEach
    public void setUp() {
        domainRepository.deleteAll();
        dumbRepository.deleteAll();
        expiredListener.resetCount();
    }

    @Test
    public void domainExpiredTest() throws InterruptedException {
        // given
        // 2초 후에 만료되는 도메인을 저장한다.
        domainRepository.save(domain);

        // 저장된 도메인의 개수는 1개이다.
        assertThat(domainRepository.count()).isEqualTo(1);
        // 리스너의 도메인 카운트는 0이다. (아직 만료되지 않았기 때문)
        assertThat(expiredListener.getDomainCount()).isEqualTo(0);
        assertThat(expiredListener.getOthersCount()).isEqualTo(0);

        // when
        // 3초 후에 만료된 도메인을 조회한다. (2초로 하면 이벤트 처리 과정이 약간 늦어 실패하는 경우가 있다.)
        Thread.sleep(3000);

        // then
        // 도메인의 개수는 0개이다. (만료되었기 때문)
        assertThat(domainRepository.count()).isEqualTo(0);
        // 리스너의 도메인 Expire 카운트는 1이다.
        assertThat(expiredListener.getDomainCount()).isEqualTo(1);
        // 리스너의 기타 Expire 카운트는 0이다.
        assertThat(expiredListener.getOthersCount()).isEqualTo(0);
    }

    @Test
    public void mixedTest() throws InterruptedException {
        // given
        // 2초 후에 만료되는 도메인과 3초 후에 만료되는 다른 도메인 객체를 설정한다.
        dumbRepository.save(dumb);
        domainRepository.save(domain);

        // 저장된 도메인의 개수는 1개이다.
        assertThat(dumbRepository.count()).isEqualTo(1);
        // 저장된 다른 도메인의 개수는 1개이다.
        assertThat(domainRepository.count()).isEqualTo(1);
        // 리스너의 도메인 카운트는 0이다. (아직 만료되지 않았기 때문)
        assertThat(expiredListener.getDomainCount()).isEqualTo(0);
        // 리스너의 기타 카운트는 0이다. (아직 만료되지 않았기 때문)
        assertThat(expiredListener.getOthersCount()).isEqualTo(0);

        // when
        // 4초 후에 만료된 도메인을 조회한다.
        Thread.sleep(4000);

        // then
        // 도메인의 개수는 삭제되어 0개이다.
        assertThat(dumbRepository.count()).isEqualTo(0);
        // 다른 도메인의 개수도 삭제되어 0개이다.
        assertThat(domainRepository.count()).isEqualTo(0);
        // 리스너의 도메인 Expire 카운트는 1이다.
        assertThat(expiredListener.getDomainCount()).isEqualTo(1);
        // 리스너의 기타 Expire 카운트는 1이다.
        assertThat(expiredListener.getOthersCount()).isEqualTo(1);
    }
}
