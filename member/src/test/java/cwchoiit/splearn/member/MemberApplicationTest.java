package cwchoiit.splearn.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(SplearnTestConfiguration.class)
class MemberApplicationTest {

    @Test
    void contextLoads() {}

    @Test
    void mainTest() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            MemberApplication.main(new String[] {});
            assertThat(TimeZone.getDefault().getID()).isEqualTo("Asia/Seoul");
            mocked.verify(() -> SpringApplication.run(MemberApplication.class, new String[] {}));
        }
    }
}
