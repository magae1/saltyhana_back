package com.saltyhana.saltyhanaserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnthropicService {

    @Value("${anthropic.api.url}")
    private String apiUrl;

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public List<String> getRecommendations(String birth, String description) {
        String systemMessage = "당신은 하나은행 전문 금융 컨설턴트입니다.";
        String userMessage = buildPrompt(birth, description);

        HttpEntity<String> entity = createHttpEntity(systemMessage, userMessage);

        try {
//            log.info("Requesting Anthropic API: URL={}", apiUrl);
//            log.info("Request Body: {}", entity.getBody());

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
//                log.error("Anthropic API 응답 실패: 상태코드={}, 응답={}", response.getStatusCode(), response.getBody());
                throw new NotFoundException("Anthropic API");
            }

            return parseResponse(response.getBody());
        } catch (NotFoundException e) {
            throw e; // 이미 처리된 예외를 다시 던짐
        } catch (Exception e) {
            log.error("Anthropic API 호출 실패: {}", e.getMessage());
            throw new NotFoundException("Anthropic API");
        }
    }



    private HttpEntity<String> createHttpEntity(String systemMessage, String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("anthropic-version", "2023-06-01");

        String requestBody = createRequestBody(systemMessage, userMessage);
        return new HttpEntity<>(requestBody, headers);
    }

    private String createRequestBody(String systemMessage, String userMessage) {
        try {
            // 최상위 system 메시지와 messages 배열 생성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "claude-3-opus-20240229");
            requestBody.put("max_tokens", 3000);
            requestBody.put("system", systemMessage);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", userMessage));
            requestBody.put("messages", messages);

            // JSON 문자열로 변환
            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Request Body 생성 실패: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(String birth, String description) {
        String productList = """
        하나은행 금융상품 목록:
                - 하나의 정기예금: 계약기간 및 가입금액이 자유롭고 자동재예치를 통해 자금관리가 가능한 하나원큐 전용 정기예금 금리: 3.20%
                - 행복knowhow 연금예금: 노후자금, 생활자금, 재투자자금까지! 행복knowhow 연금예금으로 설계 가능 금리: 2.80%~2.80%
                - 정기예금: 목돈을 일정기간 동안 예치하여 안정적인 수익을 추구하는 예금 금리: 2.70%
                - 양도성예금증서(CD): 만기일 이전이라도 양도가 가능하여 단기자금운용에 적합한 시장성 예금 연(세전, 5년) 금리: 2.75%
                - 표지어음: 은행이 보유한 원어음(상업어음 또는 무역어음)을 근거로 발행하는 배서양도 가능한 어음 연(세전, 365일) 금리: 2.50%
                - 1년 연동형 정기예금: 서울보증보험의 보증서 발급 담보용 정기예금 금리: 2.60%
                - 대전하나 축구사랑 적금: 대전하나시티즌 승리를 기원하는 마음으로 적금 만기이자를 대전하나시티즌에 후원하는 상품 금리: 1.80%~4.30%
                - 하나 청년도약계좌: 청년의 중장기 자산형성 지원을 위한 금융 상품으로, 정부 기여금 및 비과세 혜택을 제공하는 적립식 상품금리: 4.50%~6.00%
                - 펫사랑 적금: 펫코노미 시대(반려인 1,500만명) 반려동물을 위한 목돈마련 저축상품 금리: 2.30%~2.80%
                - (내맘) 적금: 저축금액, 만기일, 자동이체 구간까지 내 맘대로 디자인하는 DIY적금 금리: 2.90%~3.40%
                - (아이) 꿈하나 적금: 아이의 출생, 입학 등 특별한 해에 특별금리를 제공하는 적금 금리: 2.95%~3.75%
                - 도전365 적금: 하나머니 앱 연동 자신의 걸음수에 따라 우대금리 혜택을 받는 적금 금리: 2.00%~4.50%
                - 주택청약종합저축: 내집마련의 모든것, 주택청약종합저축으로 모두 해결. 재테크에서도 기본은 주택청약! 2년 이상 가입할 경우 파격적인 금리까지 덤으로 가져가는 적금 금리: 3.10%~3.10%
                - 함께하는 사랑 적금: 지역경제 활성화 및 소외계층 지원을 위한 적립식 예금 상품 금리: 1.20%~2.10%
                - 행복나눔 적금: 지정단체에 사랑과 나눔을 실천할 수 있는 적금 금리: 0.90%~1.20%
                - 하나 중소기업재직자 우대저축: 중소벤처기업부 『중소기업재직자 우대저축공제 사업』 에 참여하는 재직자가 자산을 형성할 수 있도록 적립금 납입시 '기업지원금'을 지원하는 적립식 상품 금리: 3.00%~5.00%
                - 트래블로그 여행 적금: 트래블로그 카드 사용 실적으로 우대금리를 제공하는 하나원큐(비대면)전용 상품 금리: 2.40%~4.40%
                - 하나 아이키움 적금: 아동 양육을 위한 수당(영아[부모급여],아동,양육수당) 수급자 및 임산부 대상 우대 금리를 제공하고, 다자녀 가구 대상 특별 우대금리를 제공하는 상품 금리: 2.00%~8.00%
                - 하나 타이밍 적금: 게임하듯 재밌게 타이밍 버튼을 터치하여 저축시 우대금리가 제공 되는 상품 금리: 2.70%~3.70%
                - 평생 군인 적금: 군복무중인 직업군인 전용 적금 금리: 1.25%~5.25%
                - 희망저축계좌I: 보건복지부 『자산형성지원 통장사업 (운영지침)』에서 정한 대상자가 자산을 형성할 수 있도록 적립금 납입시 정부지원금을 지원하는 적립식 상품 금리: 2.00%~2.00%
                - 희망저축계좌II: 보건복지부 『자산형성지원 통장사업 (운영지침)』에서 정한 대상자가 자산을 형성할 수 있도록 적립금 납입시 정부지원금을 지원하는 적립식 상품 금리: 2.00%~2.00%
                - 손님케어 적금: 고객센터를 통해 가입하는 손님케어센터 전용적금 금리: 3.10%~4.50%
                - 자유적금: 계약기간 동안 수시로 적립할 수 있는 자유적립식 상품 금리: 2.85%~2.85%
                - 청년 주택드림 청약통장: 가입자격 및 무주택 조건 등을 충족하는 경우 최대 4.5% 이율 제공, 이자소득 500만원까지, 연간불입액 600만원 한도로 비과세혜택 적용, 무주택세대주인 근로소득자라면 최대 120만원 소득공제 금리: 3.10%~4.50%
                - 하나 장병내일준비 적금: 군 복무기간 중 목돈마련 지원을 위한 국군장병 전용 적립식 상품 금리: 5.00%~8.00%
                - 대전시미래두배청년통장: 대전광역시 청년기본조례에 따른 『미래두배청년통장 사업운영지침』에서 정한 대상자가 자산을 형성할 수 있도록 적립금 납입시 시지원금을 지원하는 적립식 상품 금리: 2.80%~4.00%
                - 미래행복통장: 통일부, 북한이탈주민지원재단(이하 "기관" 이라 함)으로부터 지원대상자로 승인 받은 북한이탈주민이 목돈 마련을 위해 적립금을 납입 하면, 정부지원금 관리기관에서 정부지원금을 추가로 적립하고, 법령이 정한 요건 충족 시 정부지원금을 지급받을 수 있는 상품 금리: 2.50%~5.00%
                - 하나 미소드림 적금: 미소금융대출 및 채무조정 성실상환자의 자산형성상품 금리: 4.00%~5.00%
                - 상호부금: 정액적립방식 또는 자유적립방식을 선택하여 적립하고 만기에 목돈을 마련하는 상품 금리: 2.60%~2.60%
                - 청년내일저축계좌: 보건복지부 『자산형성지원 통장사업(운영지침)』에서 정한 대상자가 자산을 형성할 수 있도록 적립금 납입시 정부지원금을 지원하는 적립식 상품 금리: 2.00%~5.00%
                - 달달 하나 통장: 급여이체 하나만으로 우대금리 및 수수료 면제 서비스를 제공하는 입출금이 자유로운 예금 통장
                - 쿠팡 셀러 통장: 쿠팡페이 셀러월렛서비스를 이용 시 우대 서비스를 제공하는 입출금이 자유로운 예금 통장
                - (지역장려금) 하나통장: 협약한 지자체의 지역장려금(결혼, 저출생, 청년지원 정책)입금 실적에 따라 혜택을 제공하는 지역장려금 전용통장
                - 영하나플러스 통장: 다양한 수수료 우대서비스를 제공하는 YOUTH고객 전용통장
                - 급여하나 통장: 직장인을 위한 수수료우대 혜택과 만35세이하 청년직장인에게 특별 우대금리가 제공되는 월급통장
                - 연금하나 통장: 연금 입금시 우대금리와 수수료혜택을 제공하는 연금손님 전용 통장
                - 주거래하나 통장: 급여(연금) 실적이 없어도, 카드대금, 공과금 실적, 청약통장 저축 등으로 수수료를 아낄 수 있는 통장
                - 하나 플러스 통장: 우대 금리도 받고, 수수료는 무제한 면제되는 통장
                - 경기기회사다리 통장: 청년의 경제적 자립과 안정적 금융생활을 지원하는 입출금이 자유로운 예금 상품
                - 네이버페이 머니 하나 통장: 네이버페이 머니 하나 통장 서비스 이용 시 우대금리를 제공하는 통장
                - 하나 주택연금 지킴이 통장: 주택담보 노후연금 수급자의 연금수급권리를 보호하기 위한 압류방지 전용통장
                - 하나 군인연금 평생안심통장: 군인연금 수급자의 기본적인 연금수급권리를 보호하기 위한 압류방지 전용통장
                - 하나 부가가치세 매입자 납부전용계좌: 조세특례제한법 제106조의 4(금 관련 제품에 대한 부가가치세 매입자 납부특례)에 따라 "금사업자"간 "금관련 제품"을 거래할 때 또는 제106조의 9(스크랩등에 대한 부가가치세 매입자 납부특례)에 따라 "스크랩등 사업자"간 "스크랩등"을 거래할 때 매매대금의 결제와 부가가치세 납부의 편의를 제공하는 입출금이 자유로운 통장
                - 행복나눔 통장: 지정단체에 사랑과 나눔을 실천할 수 있는 입출금이 자유로운 통장
                - 사업자 주거래 우대통장: 가맹점 매출대금 결제계좌 포함 각종 이체거래를 하는 주거래 개인사업자 고객에게 수수료 우대서비스를 제공하는 입출금이 자유로운 통장
                - 함께하는 사랑 통장: 지역경제 활성화 및 소외계층 지원을 위한 입출금이 자유로운 예금 상품
                - 하나 국민연금 안심(안도)통장: 국민연금을 수령하는 수급자 통장의 압류금지로 노후 생활에 안정을 기여하는 저축 예금 상품
                - 하나 공무원연금 평생안심통장: 공무원연금 수급자의 기본적인 연금수급권리를 보호하기 위한 압류방지 전용통장
                - 하도급협력대금 통장: 『하도급관리시스템』을 통해, 발주한 공사 또는 사업의 대금 및 임금이 하도급자와 근로자에게 기간내 집행될 수 있도록 하기 위한 지급이체 시스템 전용 통장
                - 건설하나로 통장: 수수료면제, 급여관리, 금리우대 등 건설인을 위한 우대 통장
                - Easy-One Pack(이지원 팩) 통장: 외국인 전용 입출금통장으로 카드실적이나 급여이체 및 적금이체 실적에 따라 각종 금융수수료가 면제되는 통장
                - 저축예금: 개인(개인사업자 포함)이 가입할 수 있는 수시입출금 예금
                - 보통예금: 가입대상 및 예치한도 제한이 없는 수시 입출금 예금
                - 증권저축계좌: 은행이 증권사와 업무협약을 통해 은행창구에서 개설하는 증권계좌로 은행의 요구불계좌와 증권계좌를 연결한 복합금융상품, 증권사의 증권계좌를 은행에서 개설대행함으로써 은행 요구불계좌의 편리한 지급결제 기능과 증권사의 증권서비스(주식매매 등), CMA의 고금리운용 등을 연계하는 계좌
                - 기업자유예금: 기업의 일시여유자금 운용에 적합한 수시 입출금 예금
                - 하나 행복지킴이 통장: 국가에서 지급하는 각종 복지급여의 수급권리를 보호하기 위한 압류방지 전용통장
                - 수퍼플러스: 입출금이 자유로우면서도 높은 수익을 얻을 수 있습니다.목돈을 12개월 미만의 기간으로 운용시에 적당한 상품이며 가입기간이나 가입한도에 제한 없는 목돈을 1개월 미만의 기간으로 운용하실 때에도 적당한 상품
                - 하나 밀리언달러 통장: 제휴 증권사를 통해 해외주식 매매 가능한 외화 다통화 입출금 통장
                - 하나 빌리언달러 통장: 법인 및 개인사업자 대상 수출입 수수료 우대 혜택을 제공하는외화 다통화 입출금 통장
                - 일달러 외화적금: $1부터 자유롭게 모으고 일부 인출도 가능한 미달러 전용 자유적립 외화적금
                - 더 와이드(The Wide)외화적금: 금리, 환율 및 외국환 수수료 등의 다양한 우대혜택을 제공하는 입금이 자유롭고 분할인출이 가능한 자유적립 외화적금
                - 자녀사랑외화로유학적금: 유학생 및 어학연수생 대상으로 적립일자, 적립횟수에 제한이 없는 분할인출이 가능한 자유적립 외화적금
                - 스마트팝콘 외화적립예금: 가입기간 중 송금, 환전 등 외환거래 발생시 우대이율을 추가로 제공하는 스마트폰 전용상품으로 입금이 자유롭고 분할인출이 가능한 자유적립형 외화정기예금
                - 외화정기예금: 여유외화자금을 외화예금이자획득을 목적으로 예치기간을 사전에 약정하고 일정 외화금액을 예입하는기한부 외화 저축성 예금
                - 외화고단위플러스 정기예금(금리연동형): 다양한 금리적용 및 이자지급방식을 제공하는 외화정기예금
                - 외화고단위플러스 정기예금(금리확정형): 다양한 금리적용 및 이자지급방식을 제공하는 외화정기예금
                - HIFI PLUS외화적립예금: 입금이 자유롭고 분할인출이 가능한 자유적립형 외화정기예금
                - 외화다통화 예금: 하나의 계좌로 여러 통화를 동시에 거래할 수 있는 외화 입출금 통장
                - 외화수퍼플러스: 거액 단기 유휴자금을 고수익으로 운용할 수 있는 입출금이 자유로운 외화상품
                - 외화양도성예금증서(통장식): 외화정기예금보다 높은 이자를 받을 수 있으며 제3자에게 양도할 수 있는 통장식 외화양도성 예금
                - 글로벌페이 전용통장: 글로벌페이카드 결제를 위한 전용통장으로 원화통장입금시 자동출금 및 USD로 환전되어 글로벌페이외화통장으로 자동이체 글로벌페이원화통장과 글로벌페이 외화통장을 각각 개설
                - 외화보통예금: 기업의 단기운전자금 운용에 적합한 수시 입출금 예금
                - 외화당좌예금: 기업의 단기운전자금 운용에 적합한 수시 입출금 예금
                - 지수플러스 정기예금 안정형 24-21호(1년): 만기해지 시 예금의 원금은 보장되면서 기초자산의 변동에 연동하여 이자수익이 결정되는 상품
                - 지수플러스 정기예금 적극형 24-21호(1년,고단위): 만기해지 시 예금의 원금은 보장되면서 기초자산의 변동에 연동하여 이자수익이 결정되는 상품
                - 지수플러스 정기예금 적극형 24-21호(6개월): 만기해지 시 예금의 원금은 보장되면서 기초자산의 변동에 연동하여 이자수익이 결정되는 상품            
                """;

        return String.format(
                """
                당신은 하나은행 전문 금융 컨설턴트입니다. 사용자의 프로필을 분석하고 제공된 목록에서 가장 적합한 금융 상품을 추천해주세요.
                
                사용자 정보:
                - 나이: %s
                - 소비 성향: %s
        
                %s
                
            
                **중요 고려 사항:**
                - 사용자의 나이와 소비 성향에 적합한 상품을 우선적으로 선택.
 
                응답 형식: "하나 청년도약계좌,급여하나 월복리 적금,정기예금
                9개의 상품만을 응답해줘야만 해.
       
                총 9개의 상품을 보여줘. 정기예금 및 투자형 예금 3개, 적립식 상품 2개, 특화 및 융합 상품 2개, 사업 및 외화 중심 상품 2개씩  추천해주고, 해당 분류에 추천 상품이 없으면 다른 분류에서 추가로 추천, (응답 형식을 꼭 준수하여 출력해야함, 추천 이유는 불필요함!!)                
                """,
                birth, description, productList
        );
    }

    private List<String> parseResponse(String responseBody) {
        try {
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

            // content 필드에서 데이터를 추출
            List<Map<String, Object>> contentList = (List<Map<String, Object>>) responseMap.get("content");
            if (contentList == null || contentList.isEmpty()) {
                log.warn("API 응답에서 'content' 필드를 찾을 수 없습니다.");
                throw new NotFoundException("Anthropic API 응답");
            }

            StringBuilder fullContent = new StringBuilder();
            for (Map<String, Object> contentItem : contentList) {
                if ("text".equals(contentItem.get("type"))) {
                    fullContent.append(contentItem.get("text"));
                }
            }

            String content = fullContent.toString();
            log.info("Anthropic API Content: {}", content);

            // 상품 이름 리스트로 변환
            List<String> productList = Arrays.stream(content.split(",|\\n")) // 쉼표 및 줄바꿈 기준으로 분리
                    .map(String::trim) // 공백 제거
                    .filter(item -> !item.isEmpty()) // 빈 문자열 제거
                    .collect(Collectors.toList());

            log.info("Parsed Products: {}", productList);
            return productList;
        } catch (Exception e) {
            log.error("API 응답 파싱 실패: {}", e.getMessage());
            throw new NotFoundException("Anthropic API 응답");
        }
    }
}
