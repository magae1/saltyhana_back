package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.ConsumptionTestAnswerWithScoreDTO;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResponseDTO;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResultResponseDTO;
import com.saltyhana.saltyhanaserver.dto.form.ConsumptionTestResultFormDTO;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTendency;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTest;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTestAnswer;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.exception.BadRequestException;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.repository.ConsumptionTendencyRepository;
import com.saltyhana.saltyhanaserver.repository.ConsumptionTestAnswerRepository;
import com.saltyhana.saltyhanaserver.repository.ConsumptionTestRepository;
import com.saltyhana.saltyhanaserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
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
    public void receiveResult(Long userId, List<ConsumptionTestResultFormDTO> resultForm)
        throws ResponseStatusException {
        int numOfTests = (int) consumptionTestRepository.count();
        if (numOfTests != resultForm.size()) {
            throw new BadRequestException();
        }

        int totalScore = calculateTotalScore(resultForm);
        log.info("Total score: {}", totalScore);

        Optional<ConsumptionTendency> tendencyResult = consumptionTendencyRepository.findByScore(totalScore);
        if (tendencyResult.isEmpty()) {
            throw new BadRequestException();
        }

        ConsumptionTendency tendency = tendencyResult.get();
        log.info("Tendency: {}", tendency);
        userRepository.updateConsumptionTendency(userId, tendency.getId());
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
    }

    private int calculateTotalScore(List<ConsumptionTestResultFormDTO> resultForm)
        throws ResponseStatusException {
        List<ConsumptionTestAnswerWithScoreDTO> boards = getConsumptionTestAnswerWithScoreList();

        int totalScore = 0;
        for (ConsumptionTestResultFormDTO result : resultForm) {
            boolean isFound = false;
            for (ConsumptionTestAnswerWithScoreDTO board : boards) {
                Integer questionNum = board.getQuestionNum();
                Integer answerNum = board.getAnswerNum();
                if (Objects.equals(result.getQuestionNum(), questionNum) && Objects.equals(
                    result.getAnswerNum(), answerNum)) {
                    totalScore = totalScore + board.getScore();
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                throw new BadRequestException();
            }
        }
        return totalScore;
    }

    private List<ConsumptionTestAnswerWithScoreDTO> getConsumptionTestAnswerWithScoreList() {
        List<Object> tests = consumptionTestRepository.findAllTests();
        return tests.stream().map(o -> {
            Object[] array = (Object[]) o;
            ConsumptionTest test = (ConsumptionTest) array[0];
            ConsumptionTestAnswer answer = (ConsumptionTestAnswer) array[1];
            return ConsumptionTestAnswerWithScoreDTO.builder()
                .questionNum(test.getSeqNum())
                .answerNum(answer.getSeqNum())
                .score(answer.getScore())
                .build();
        }).toList();
    }
}
