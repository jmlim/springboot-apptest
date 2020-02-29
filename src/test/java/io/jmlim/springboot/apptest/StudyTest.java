package io.jmlim.springboot.apptest;

import io.jmlim.springboot.apptest.domain.Study;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order 어노테이션으로 순서를 정함.
class StudyTest {
    int value = 0;

    // 프로그래밍 등록
    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension
            = new FindSlowTestExtension(1000L);

    @Order(2)
    @FastTest
    @DisplayName("스터디 만들기 \uD83D\uDE31 fast")
    void create_new_study() {
        Study actual = new Study(100);
        assertThat(actual.getLimitCount()).isGreaterThan(0);
        System.out.println("create_new_study : " + (++value));
    }

    @Order(1)
    // @SlowTest
    @Test
    @DisplayName("스터디 만들기 \uD83D\uDE31 slow")
    @Disabled // junit-platform.properties 파일에 junit.jupiter.conditions.deactivate = org.junit.*DisabledCondition 설정되어있어 무시됨
    void create1_new_study_again() throws InterruptedException {
        Thread.sleep(1005L);
        System.out.println(this);
        System.out.println(++value);
    }

    @Order(3)
    @DisplayName("스터디 만들기 리핏테스트")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition} / {totalRepetitions}")
    void repeatTest(RepetitionInfo repetitionInfo) {
        // RepetitionInfo을 통해 반복된 정보들을 알 수 있다.
        System.out.println("test" + repetitionInfo.getCurrentRepetition() + " / " + repetitionInfo.getTotalRepetitions());
    }

    @Order(4)
    @DisplayName("스터디 만들기 ParameterizedTest String")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가","많이","추워지고","있네요."})
    @EmptySource // 비어있는 문자열을 인자로 추가.
    @NullSource // 널을 인자로 추가.
    @NullAndEmptySource // 널과 Empty 문자열을 인자로 추가.
    void parameterizedTest(String message) {
        System.out.println(message);
    }

    @Order(5)
    @DisplayName("스터디 만들기 ParameterizedTest Integer")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTest(Integer limit) {
        System.out.println(limit);
    }

    @Order(6)
    @DisplayName("스터디 만들기 ParameterizedTest Study object (use StudyConverter)")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40}) // 이경우는 숫자를 스터디 타입으로 받고자 하는것이기 때문에 컨버터를 만들어줘야 한다.
    void parameterizedTest(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimitCount());
    }
    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Can only convert to Study");
            return new Study(Integer.parseInt(source.toString()));
        }
    }

    @Order(7)
    @DisplayName("스터디 만들기 ParameterizedTest Csv")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "20, '스프링'"})
    void parameterizedTestCsv(Integer limit, String name) {
        Study study = new Study(limit, name);
        System.out.println(study);
    }

    @Order(8)
    @DisplayName("스터디 만들기 ParameterizedTest Csv (use ArgumentsAccessor)")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "20, '스프링'"})
    void parameterizedTestCsv(ArgumentsAccessor argumentsAccessor) {
        Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        System.out.println(study);
    }

    @Order(9)
    @DisplayName("스터디 만들기 ParameterizedTest Csv (use StudyAggregator)")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "20, '스프링'"})
    void parameterizedTestCsv(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study);
    }
    // 2개의 인자값 사용 시 SimpleArgumentConverter 대신 ArgumentsAggregator 상속하여 만듬
    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context)
                throws ArgumentsAggregationException {
            return new Study(accessor.getInteger(0), accessor.getString(1));
        }
    }

    //반드시 스태틱 void 로 작성.
    // 모든 테스트가 실행된 이전에 딱 한번 실행됨.
    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }

    //반드시 스태틱 void 로 작성.
    // 모든 테스트가 실행된 이후에 딱 한번 실행됨.
    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    // 각각 테스트 실행하기 이전에 실행
    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach");
    }

    // 각각 테스트 실행 후 실행
    @AfterEach
    void afterEach() {
        System.out.println("afterEach");
    }
}