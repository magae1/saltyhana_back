package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.ConsumptionTestAnsDTO;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestAnswerDTO;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResponseDTO;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResultResponseDTO;
import com.saltyhana.saltyhanaserver.dto.form.ConsumptionTestResultForm;
import com.saltyhana.saltyhanaserver.dto.form.TestResultDTO;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTendency;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTest;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTestAnswer;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.enums.ConsumptionMBTIEnum;
import com.saltyhana.saltyhanaserver.enums.ConsumptionTypeEnum;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.repository.ConsumptionTendencyRepository;
import com.saltyhana.saltyhanaserver.repository.ConsumptionTestAnswerRepository;
import com.saltyhana.saltyhanaserver.repository.ConsumptionTestRepository;
import com.saltyhana.saltyhanaserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConsumptionTestService {
    private final ConsumptionTestRepository consumptionTestRepository;
    private final ConsumptionTestAnswerRepository consumptionTestAnswerRepository;
    private final ConsumptionTendencyRepository consumptionTendencyRepository;
    private final UserRepository userRepository;

    //페이지 하나 get
    public ConsumptionTestResponseDTO getPage(Long id) {
        Optional<ConsumptionTest> test = Optional.ofNullable(consumptionTestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("getPage not found")));

        return ConsumptionTestResponseDTO.builder()
                .question(test.get().getQuestion())
                .build();
    }

    //응답 데이터 보내기(ConsumptionTestResultForm)
    @Transactional
    public void sendResult(Long userId, ConsumptionTestResultForm form) {
        List<TestResultDTO> answers = form.getAnswers();

        List<Object> tests = consumptionTestRepository.findAllTests();
        List<ConsumptionTestAnsDTO> boards = tests.stream().map(o -> {
            Object[] array = (Object[]) o;
            ConsumptionTest test = (ConsumptionTest) array[0];
            ConsumptionTestAnswer answer = (ConsumptionTestAnswer) array[1];
            return ConsumptionTestAnsDTO.builder()
                    .questionNum(test.getSeqNum())
                    .answerNum(answer.getSeqNum())
                    .score(answer.getScore())
                    .build();
        }).toList();

        int totalScore = 0;
        for (TestResultDTO test : answers) {
            for (ConsumptionTestAnsDTO board : boards) {
                Integer questionNum = board.getQuestionNum();
                Integer answerNum = board.getAnswerNum();
                if (Objects.equals(test.getQuestionNum(), questionNum) && Objects.equals(test.getAnswerNum(), answerNum)) {
                    totalScore = totalScore + board.getScore();
                }
            }
        }

        Optional<ConsumptionTendency> tendencyResult = consumptionTendencyRepository.findByScore(totalScore);
        if (tendencyResult.isEmpty()) {
            log.warn("소비 경향을 찾을 수 없습니다.");
            return;
        }
        ConsumptionTendency tendency = tendencyResult.get();
        User user = User.builder()
                .id(userId)
                .consumptionTendency(tendency)
                .build();

        userRepository.save(user);
    }

    //1~10개 응답데이터 모아서 소비성향 반환
    public ConsumptionTestResultResponseDTO getTendency(Long userId) throws ResponseStatusException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("유저"));

        ConsumptionTendency tendency = user.getConsumptionTendency();

        if (tendency == null) {
            throw new NotFoundException("소비 경향");
        }

        return ConsumptionTestResultResponseDTO.builder()
                .title(tendency.getTitle())
                .description(tendency.getDescription())
                .type(tendency.getType().toString())
                .mbti(tendency.getMbti().toString())
                .emoji(tendency.getEmoji())
                .build();
//
//        List<ConsumptionTestAnswer> answers = consumptionTestAnswerRepository.findAll();
//        List<ConsumptionTestAnswerDTO> answerDtos = answers.stream()
//                .map(answer -> ConsumptionTestAnswerDTO.builder()
//                        .body(answer.getBody())
//                        .seqNum(answer.getSeqNum())
//                        .build())
//                .collect(Collectors.toList());
//
//        //점수 계산
//        Integer totalScore = getScore(answerDtos);
//
//        //유형 결정
//        ConsumptionTendency consumptionTendency = determineTendency(totalScore, id);
//
//        //저장
//        consumptionTendencyRepository.save(consumptionTendency);
//
//        ConsumptionTestResultResponseDTO resultDTO = ConsumptionTestResultResponseDTO.builder()
//                .id(consumptionTendency.getId())
//                .title(consumptionTendency.getTitle())
//                .description(consumptionTendency.getDescription())
//                .type(String.valueOf(consumptionTendency.getType()))
//                .mbti(String.valueOf(consumptionTendency.getMbti()))
//                .emoji(consumptionTendency.getEmoji())
//                .build();
//
//        return resultDTO;
    }

    //총점 계산
    public Integer getScore(List<ConsumptionTestAnswerDTO> answerDtos) {
        Integer totalScore = 0;
        for (ConsumptionTestAnswerDTO answerDto : answerDtos) {
            if (answerDto.getSeqNum() == 1) totalScore += 10;
            else if (answerDto.getSeqNum() == 2) totalScore += 5;
            else if (answerDto.getSeqNum() == 3) totalScore += 0;
        }
        return totalScore;
    }

    //점수 기반으로 성향 결정
    public ConsumptionTendency determineTendency(Integer totalScore, Long id) {
        ConsumptionTendency tendency;
        if (totalScore >= 50) {
            tendency = ConsumptionTendency.builder()
                    .id(id)
                    .title("마지막에 웃는 \n 진짜 승리자!")
                    .description("당장의 소비를 참고 자산을 관리하는 당신!\\n부자가 될 자격이 있습니다!")
                    .type(ConsumptionTypeEnum.YONO)
                    .mbti(ConsumptionMBTIEnum.___J)
                    .emoji("눈깔하트짤url")
                    .build();

        } else if (totalScore >= 40) {
            tendency = ConsumptionTendency.builder()
                    .id(id)
                    .title("욜로와 요노 그 사이 어딘가")
                    .description("돈 쓰는 즐거움과 자산 관리의 중요성을 아시는군요! \n 자산을 모으기 위해 조금만 더 노력해볼까요?")
                    .type(ConsumptionTypeEnum.YOLO)
                    .mbti(ConsumptionMBTIEnum._S_P)
                    .emoji("안경잽이웃는짤url")
                    .build();
        } else {
            tendency = ConsumptionTendency.builder()
                    .id(id)
                    .title("돈 쓰는 게 제일 좋아! 욜로족")
                    .description("돈 쓰는 즐거움을 아는 당신!\\n돈 모으는 즐거움도 한 번 느껴보실래요?")
                    .type(ConsumptionTypeEnum.YOLO)
                    .mbti(ConsumptionMBTIEnum.___P)
                    .emoji("돈눈깔url")
                    .build();
        }
        return tendency;
    }

//    //n번째 질문 하나에 대한 답변 데이터 반환
//    public ConsumptionTestAnswerDTO getOneResult(Long id) {
//        ConsumptionTestAnswer testResult = consumptionTestRepository.findResultById(consumptionTestRepository.findTestById(id));
//        return ConsumptionTestAnswerDTO.builder()
//                .body(testResult.getBody())
//
//                .seqNum(testResult.getSeqNum())
//                .build();
//    }

//    //1~10개 질문에 대한 응답 싹 모아서, 소비성향(테스트 결과) 반환
//    //TendencyDTO 안필요하나? 그렇다면 뭐로..?
//    public ConsumptionTestResultResponseDTO getResult(Long id){
//        ConsumptionTestAnswer answer=consumptionTestAnswerRepository.findById(id);
//    }


/*    @Transactional
//    public ConsumptionTestResultResponseDTO getsetResult(ConsumptionTestResultResponseDTO resultDTO) {
//        List<ConsumptionTestAnswer> answers = consumptionTestAnswerRepository.findAll();
//        List<ConsumptionTestAnswerDTO> answerDtos = answers.stream()
//                .map(answer -> ConsumptionTestAnswerDTO.builder()
//                        .body(answer.getBody())
//                        .seqNum(answer.getSeqNum())
//                        .build())
                .collect(Collectors.toList());
*/

    //맞나..? TestAnswerDTO 리스트 기반으로 TestResponseDTO 반환할라한건데..
//        answerDtos.stream()
//                .map(dto -> ConsumptionTestResponseDTO.builder()
//                        .question(dto.getBody())
//                        .answers(answerDtos)
//                        .build());
//
//        //맞나..?

/*      //점수 계산
        Integer totalScore = getScore(answerDtos);

        //유형 결정
        ConsumptionTendency consumptionTendency = determineTendency(totalScore, resultDTO);

        //저장
        consumptionTendencyRepository.save(consumptionTendency);
        return resultDTO;
*/
//        // 1부터 시작하는 인덱스 생성
//        AtomicInteger index = new AtomicInteger(1);
//
//        //몇번 문제인지, 몇번 보기를 골랐는지
//        answerDtos.stream()
//                .map(dto -> ConsumptionTestResultForm.builder()
//                        .answerNum(dto.getSeqNum())
//                        .questionNum(index.getAndIncrement()) //인덱스 1~10까지 순서 증가
//                        .build())
//                .collect(Collectors.toList());
}