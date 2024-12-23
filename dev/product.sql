-------------- 인기상품
INSERT INTO product (id, dcls_month, fin_co_no, fin_prdt_cd, kor_co_nm, fin_prdt_nm, mtrt_int, spcl_cnd, join_deny, join_member, etc_note, max_limit, dcls_strt_day, dcls_end_day, fin_co_subm_day)
VALUES
    (1, '2024-06-01', 1001, 1, '하나은행', '369 정기예금', '단리', '3개월마다 중도해지 혜택 제공', 'N', '실명의 개인', '가입금액: 1백만원 이상', NULL, '2024-06-01T00:00:00', NULL, '2024-05-25T12:00:00'),
    (2, '2024-06-01', 1002, 2, '하나은행', '고단위 플러스', '단리', '우대 금리 최대 5.5%', 'N', '실명의 개인 또는 개인사업자', '이자: 매월 지급 가능', NULL, '2024-06-01T00:00:00', NULL, '2024-05-25T12:00:00'),
    (3, '2024-06-01', 1003, 3, '하나은행', '주거래하나 월복리적금', '복리', '주거래 고객 우대 이자', 'N', '실명의 개인', '가입기간: 최대 12개월', NULL, '2024-06-01T00:00:00', NULL, '2024-05-25T12:00:00'),
    (4, '2024-06-01', 1004, 4, '하나은행', '급여하나 월복리적금', '복리', '급여 이체 우대 금리 제공', 'N', '실명의 개인', '이체실적에 따라 추가 혜택', NULL, '2024-06-01T00:00:00', NULL, '2024-05-25T12:00:00'),
    (5, '2024-06-01', 1005, 5, '하나은행', '내집마련 더블업 적금', '복리', '집 마련 목표 금리 혜택', 'N', '실명의 개인', '주택구입 시 추가 이자 지급', NULL, '2024-06-01T00:00:00', NULL, '2024-05-25T12:00:00'),
    (6, '2024-06-01', 1006, 6, '하나은행', '부자씨 적금', '복리', '고정 금리 최대 4%', 'N', '실명의 개인', '저축습관 형성 목표', NULL, '2024-06-01T00:00:00', NULL, '2024-05-25T12:00:00');



-------------- 일반상품
INSERT INTO product (id, dcls_month, fin_co_no, fin_prdt_cd, kor_co_nm, fin_prdt_nm, mtrt_int, spcl_cnd, join_deny, join_member, etc_note, max_limit, dcls_strt_day, dcls_end_day, fin_co_subm_day)
VALUES
    (7, '2024-12-01', 1001, 103, '하나은행', '고단위 플러스(금리확정형)', '단리', '이자 지급 방법과 지급 시기를 자유롭게 선택 가능', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (8, '2024-12-01', 1001, 104, '하나은행', '하나의 정기예금', '단리', '계약기간 및 가입금액이 자유롭고 자동재예치를 통해 자금관리가 가능', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (9, '2024-12-01', 1001, 105, '하나은행', '행복knowhow 연금예금', '단리', '노후자금, 생활자금, 재투자자금으로 설계 가능', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (10, '2024-12-01', 1001, 106, '하나은행', '정기예금', '단리', '목돈을 일정기간 동안 예치하여 안정적인 수익을 추구', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (11, '2024-12-01', 1001, 107, '하나은행', '양도성예금증서(CD)', '단리', '만기일 이전 양도가 가능하여 단기자금 운용에 적합', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (12, '2024-12-01', 1001, 108, '하나은행', '표지어음', '단리', '배서양도가 가능한 어음', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (13, '2024-12-01', 1001, 109, '하나은행', '1년 연동형 정기예금', '단리', '서울보증보험의 보증서 발급 담보용', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (14, '2024-12-01', 1001, 110, '하나은행', '대전하나 축구사랑 적금', '단리', '대전하나시티즌 후원을 위한 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (15, '2024-12-01', 1001, 111, '하나은행', '하나 청년도약계좌', '단리', '중장기 자산형성 지원을 위한 금융 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (16, '2024-12-01', 1001, 112, '하나은행', '펫사랑 적금', '단리', '반려동물을 위한 저축상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (17, '2024-12-01', 1001, 114, '하나은행', '(내맘) 적금', '단리', '저축금액과 만기일을 자유롭게 설계', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (18, '2024-12-01', 1001, 115, '하나은행', '연금하나 월복리 적금', '단리', '연금 가입자에게 월복리 혜택 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (19, '2024-12-01', 1001, 117, '하나은행', '(아이) 꿈하나 적금', '단리', '특별한 날에 특별금리를 제공하는 적금', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (20, '2024-12-01', 1001, 119, '하나은행', '도전365 적금', '단리', '하나머니 앱과 연동된 걸음수 우대 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (21, '2024-12-01', 1001, 120, '하나은행', '주택청약종합저축', '단리', '내집마련을 위한 기본 저축상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (22, '2024-12-01', 1001, 121, '하나은행', '함께하는 사랑 적금', '단리', '지역경제 활성화 및 소외계층 지원 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (23, '2024-12-01', 1001, 122, '하나은행', '행복나눔 적금', '단리', '사랑과 나눔을 실천할 수 있는 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (24, '2024-12-01', 1001, 123, '하나은행', '하나 중소기업재직자 우대저축', '단리', '중소기업재직자 자산형성 지원 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (25, '2024-12-01', 1001, 124, '하나은행', '트래블로그 여행 적금', '단리', '트래블로그 카드 사용 실적으로 우대금리 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (26, '2024-12-01', 1001, 125, '하나은행', '하나 아이키움 적금', '단리', '아동 양육 및 다자녀 가구 우대 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (27, '2024-12-01', 1001, 127, '하나은행', '하나 타이밍 적금', '단리', '타이밍 버튼으로 재미와 우대금리 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (28, '2024-12-01', 1001, 128, '하나은행', '평생 군인 적금', '단리', '군 복무 중 직업군인 전용 적금', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (29, '2024-12-01', 1001, 129, '하나은행', '희망저축계좌I', '단리', '정부지원금을 지원받을 수 있는 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (30, '2024-12-01', 1001, 130, '하나은행', '희망저축계좌II', '단리', '정부지원금을 지원받을 수 있는 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (31, '2024-12-01', 1001, 131, '하나은행', '손님케어 적금', '단리', '고객센터를 통해 가입하는 전용 적금', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (32, '2024-12-01', 1001, 132, '하나은행', '자유적금', '단리', '계약기간 동안 수시로 적립 가능한 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (33, '2024-12-01', 1001, 133, '하나은행', '청년 주택드림 청약통장', '단리', '비과세 혜택 및 최대 4.5% 이율 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (34, '2024-12-01', 1001, 134, '하나은행', '하나 장병내일준비 적금', '단리', '군 복무기간 중 목돈마련을 위한 적금', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (35, '2024-12-01', 1001, 135, '하나은행', '대전시미래두배청년통장', '단리', '청년 자산형성을 위한 시지원금 제공 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (36, '2024-12-01', 1001, 136, '하나은행', '미래행복통장', '단리', '북한이탈주민 자산형성 및 정부지원금 제공 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (37, '2024-12-01', 1001, 137, '하나은행', '하나 미소드림 적금', '단리', '미소금융대출 성실 상환자 전용 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (38, '2024-12-01', 1001, 138, '하나은행', '상호부금', '단리', '정액 또는 자유적립방식 선택 가능', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (39, '2024-12-01', 1001, 139, '하나은행', '청년내일저축계좌', '단리', '정부지원금이 포함된 자산형성 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (40, '2024-12-01', 1001, 140, '하나은행', '달달 하나 통장', '단리', '급여이체로 우대금리와 수수료 면제 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (41, '2024-12-01', 1001, 141, '하나은행', '쿠팡 셀러 통장', '단리', '쿠팡페이 셀러월렛서비스 이용 시 우대 서비스 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (42, '2024-12-01', 1001, 142, '하나은행', '(지역장려금) 하나통장', '단리', '지역장려금 입금 실적에 따라 혜택 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (43, '2024-12-01', 1001, 143, '하나은행', '영하나플러스 통장', '단리', 'YOUTH 고객 전용 수수료 우대 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (44, '2024-12-01', 1001, 144, '하나은행', '급여하나 통장', '단리', '청년직장인에게 특별 우대금리 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (45, '2024-12-01', 1001, 145, '하나은행', '연금하나 통장', '단리', '연금 입금 시 우대금리와 수수료 혜택 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (46, '2024-12-01', 1001, 146, '하나은행', '주거래하나 통장', '단리', '카드대금, 공과금 실적으로 수수료 절약 가능', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (47, '2024-12-01', 1001, 147, '하나은행', '하나 플러스 통장', '단리', '우대 금리와 수수료 면제 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (48, '2024-12-01', 1001, 148, '하나은행', '경기기회사다리 통장', '단리', '청년의 경제적 자립 지원 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (49, '2024-12-01', 1001, 149, '하나은행', '네이버페이 머니 하나 통장', '단리', '네이버페이 서비스 우대금리 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (50, '2024-12-01', 1001, 150, '하나은행', '하나 주택연금 지킴이 통장', '단리', '압류방지 전용 노후연금 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (51, '2024-12-01', 1001, 151, '하나은행', '하나 군인연금 평생안심통장', '단리', '군인연금 수급권리를 보호하는 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (52, '2024-12-01', 1001, 152, '하나은행', '하나 부가가치세 매입자 납부전용계좌', '단리', '금 사업자 대상 부가가치세 납부 전용 통장', 'N', '법인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (53, '2024-12-01', 1001, 153, '하나은행', '행복나눔 통장', '단리', '사랑과 나눔을 실천할 수 있는 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (54, '2024-12-01', 1001, 154, '하나은행', '사업자 주거래 우대통장', '단리', '이체거래가 많은 개인사업자 고객에게 수수료 혜택', 'N', '법인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (55, '2024-12-01', 1001, 155, '하나은행', '함께하는 사랑 통장', '단리', '지역경제 활성화와 소외계층 지원 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (56, '2024-12-01', 1001, 156, '하나은행', '하나 국민연금 안심 통장', '단리', '국민연금을 보호하는 압류방지 통장', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (57, '2024-12-01', 1001, 157, '하나은행', '하나 공무원연금 평생안심통장', '단리', '공무원연금을 보호하는 압류방지 통장', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (58, '2024-12-01', 1001, 158, '하나은행', '하도급협력대금 통장', '단리', '하도급자의 대금 및 임금 집행 지원 상품', 'N', '법인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (59, '2024-12-01', 1001, 159, '하나은행', '건설하나로 통장', '단리', '건설인을 위한 수수료 면제 및 금리 우대 상품', 'N', '법인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (60, '2024-12-01', 1001, 160, '하나은행', 'Easy-One Pack 통장', '단리', '외국인을 위한 금융 수수료 면제 통장', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (61, '2024-12-01', 1001, 161, '하나은행', '저축예금', '단리', '개인이 수시로 입출금할 수 있는 예금', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (62, '2024-12-01', 1001, 162, '하나은행', '보통예금', '단리', '제한 없이 입출금 가능한 예금', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (63, '2024-12-01', 1001, 163, '하나은행', '증권저축계좌', '단리', '증권사의 주식 매매 기능과 연결된 복합 금융 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (64, '2024-12-01', 1001, 164, '하나은행', '기업자유예금', '단리', '기업의 일시여유자금 운용에 적합한 예금', 'N', '법인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (65, '2024-12-01', 1001, 165, '하나은행', '하나 행복지킴이 통장', '단리', '국가 복지급여 수급권리를 보호하는 압류방지 통장', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (66, '2024-12-01', 1001, 166, '하나은행', '수퍼플러스', '단리', '입출금이 자유로우면서도 높은 수익을 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (67, '2024-12-01', 1001, 167, '하나은행', '하나 밀리언달러 통장', '단리', '해외주식 매매 가능한 외화 입출금 통장', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (68, '2024-12-01', 1001, 168, '하나은행', '하나 빌리언달러 통장', '단리', '법인 대상 수출입 수수료 우대 혜택 통장', 'N', '법인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (69, '2024-12-01', 1001, 169, '하나은행', '일달러 외화적금', '단리', '$1부터 자유롭게 적립 가능한 외화 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (70, '2024-12-01', 1001, 170, '하나은행', '더 와이드(The Wide)외화적금', '단리', '환율 및 외환 수수료 우대 혜택 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (71, '2024-12-01', 1001, 171, '하나은행', '자녀사랑외화로유학적금', '단리', '유학생을 위한 적립일자 제한 없는 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (72, '2024-12-01', 1001, 172, '하나은행', '스마트팝콘 외화적립예금', '단리', '외환거래 발생 시 우대이율 제공', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (73, '2024-12-01', 1001, 173, '하나은행', '외화정기예금', '단리', '여유 외화를 일정 기간 예치하는 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (74, '2024-12-01', 1001, 174, '하나은행', '외화고단위플러스 정기예금(금리연동형)', '단리', '금리연동 방식의 외화정기예금', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (75, '2024-12-01', 1001, 175, '하나은행', '외화고단위플러스 정기예금(금리확정형)', '단리', '금리확정 방식의 외화정기예금', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (76, '2024-12-01', 1001, 176, '하나은행', 'HIFI PLUS외화적립예금', '단리', '입금이 자유롭고 분할인출이 가능한 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (77, '2024-12-01', 1001, 177, '하나은행', '외화다통화 예금', '단리', '다양한 통화를 동시에 거래 가능', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (78, '2024-12-01', 1001, 178, '하나은행', '외화수퍼플러스', '단리', '거액 단기 유휴자금을 고수익으로 운용', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (79, '2024-12-01', 1001, 179, '하나은행', '외화양도성예금증서(통장식)', '단리', '양도 가능한 외화정기예금 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (80, '2024-12-01', 1001, 180, '하나은행', '글로벌페이 전용통장', '단리', '글로벌페이카드 결제 전용 통장', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (81, '2024-12-01', 1001, 181, '하나은행', '외화보통예금', '단리', '기업의 단기 운전자금 운용에 적합한 상품', 'N', '법인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (82, '2024-12-01', 1001, 182, '하나은행', '외화당좌예금', '단리', '기업의 단기 운전자금 운용에 적합한 당좌 상품', 'N', '법인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (83, '2024-12-01', 1001, 183, '하나은행', '지수플러스 정기예금 안정형 24-21호(1년)', '단리', '기초자산 변동에 연동한 안정형 수익 제공 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (84, '2024-12-01', 1001, 184, '하나은행', '지수플러스 정기예금 적극형 24-21호(1년,고단위)', '단리', '기초자산 변동에 연동한 적극형 수익 제공 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00'),
    (85, '2024-12-01', 1001, 185, '하나은행', '지수플러스 정기예금 적극형 24-21호(6개월)', '단리', '6개월 만기 기초자산 변동에 연동한 수익 제공 상품', 'N', '개인', NULL, NULL, '2024-12-01T00:00:00', NULL, '2024-12-01T00:00:00');



-------------- 안가상품
INSERT INTO rate (id, product_id, intr_rate_type, intr_rate_type_nm, rsrv_type, rsrv_type_nm, save_trm, intr_rate, intr_rate2)
VALUES
    (1, 1, 'S', '단리', '01', '정기예금', 12, 3.5, 4.5), -- 369 정기예금
    (2, 2, 'S', '단리', '01', '정기예금', 12, 4.0, 5.5), -- 고단위 플러스
    (3, 3, 'C', '복리', '02', '정기적금', 12, 4.2, 5.0), -- 주거래하나 월복리적금
    (4, 4, 'C', '복리', '02', '정기적금', 12, 4.0, 4.8), -- 급여하나 월복리적금
    (5, 5, 'C', '복리', '02', '정기적금', 12, 3.8, 4.5), -- 내집마련 더블업 적금
    (6, 6, 'C', '복리', '02', '정기적금', 12, 4.0, 4.5); -- 부자씨 적금

-------------- 일반상품
INSERT INTO rate (id, product_id, intr_rate_type, intr_rate_type_nm, rsrv_type, rsrv_type_nm, save_trm, intr_rate, intr_rate2)
VALUES
    (7, 7, 'S', '단리', '01', '정기예금', 12, 2.6, 2.6), -- 고단위 플러스(금리확정형)
    (8, 8, 'S', '단리', '01', '정기예금', 12, 3.2, 3.2), -- 하나의 정기예금
    (9, 9, 'S', '단리', '01', '연금예금', 12, 2.8, 2.8), -- 행복knowhow 연금예금
    (10, 10, 'S', '단리', '01', '정기예금', 12, 2.7, 2.7), -- 정기예금
    (11, 11, 'S', '단리', '01', '양도성예금증서', 60, 2.75, 2.75), -- 양도성예금증서(CD)
    (12, 12, 'S', '단리', '01', '표지어음', 12, 2.5, 2.5), -- 수정 완료
    (13, 13, 'S', '단리', '01', '정기예금', 12, 2.6, 2.6), -- 1년 연동형 정기예금
    (14, 14, 'C', '복리', '02', '정기적금', 12, 1.8, 4.3), -- 대전하나 축구사랑 적금
    (15, 15, 'C', '복리', '02', '정기적금', 12, 4.5, 6.0), -- 수정 완료
    (16, 16, 'C', '복리', '02', '정기적금', 12, 2.3, 2.8), -- 펫사랑 적금
    (17, 17, 'C', '복리', '02', '정기적금', 12, 2.9, 3.4), -- (내맘) 적금
    (18, 18, 'C', '복리', '02', '정기적금', 12, 3.5, 4.5), -- 연금하나 월복리 적금
    (19, 19, 'C', '복리', '02', '정기적금', 12, 2.95, 3.75), -- (아이) 꿈하나 적금
    (20, 20, 'C', '복리', '02', '정기적금', 12, 2.0, 4.5), -- 도전365 적금
    (21, 21, 'S', '단리', '01', '정기적금', 24, 3.1, 3.1), -- 주택청약종합저축
    (22, 22, 'C', '복리', '02', '정기적금', 12, 1.2, 2.1), -- 함께하는 사랑 적금
    (23, 23, 'C', '복리', '02', '정기적금', 12, 0.9, 1.2), -- 행복나눔 적금
    (24, 24, 'C', '복리', '02', '정기적금', 12, 3.0, 5.0), -- 하나 중소기업재직자 우대저축
    (25, 25, 'C', '복리', '02', '정기적금', 12, 2.4, 4.4), -- 트래블로그 여행 적금
    (26, 26, 'C', '복리', '02', '정기적금', 12, 2.0, 8.0), -- 하나 아이키움 적금
    (27, 27, 'C', '복리', '02', '정기적금', 12, 2.7, 3.7), -- 하나 타이밍 적금
    (28, 28, 'C', '복리', '02', '정기적금', 12, 1.25, 5.25), -- 평생 군인 적금
    (29, 29, 'C', '복리', '02', '정기적금', 12, 2.0, 2.0), -- 희망저축계좌I
    (30, 30, 'C', '복리', '02', '정기적금', 12, 2.0, 2.0), -- 희망저축계좌II
    (31, 31, 'C', '복리', '02', '정기적금', 12, 3.1, 4.5), -- 손님케어 적금
    (32, 32, 'C', '복리', '02', '정기적금', 12, 2.85, 2.85), -- 자유적금
    (33, 33, 'S', '단리', '01', '정기예금', 24, 3.1, 4.5), -- 청년 주택드림 청약통장
    (34, 34, 'C', '복리', '02', '정기적금', 12, 5.0, 8.0), -- 하나 장병내일준비 적금
    (35, 35, 'C', '복리', '02', '정기적금', 12, 2.8, 4.0), -- 대전시미래두배청년통장
    (36, 36, 'C', '복리', '02', '정기적금', 12, 2.5, 5.0), -- 미래행복통장
    (37, 37, 'C', '복리', '02', '정기적금', 12, 4.0, 5.0), -- 하나 미소드림 적금
    (38, 38, 'C', '복리', '02', '정기적금', 12, 2.6, 2.6), -- 상호부금
    (39, 39, 'C', '복리', '02', '정기적금', 12, 2.0, 5.0), -- 청년내일저축계좌
    (40, 40, 'S', '단리', '01', '입출금통장', 12, 1.4, 1.6), -- 달달 하나 통장
    (41, 41, 'S', '단리', '01', '입출금통장', 12, 1.3, 1.5), -- 쿠팡 셀러 통장
    (42, 42, 'S', '단리', '01', '입출금통장', 12, 1.2, 1.4), -- (지역장려금) 하나통장
    (43, 43, 'S', '단리', '01', '입출금통장', 12, 1.4, 1.6), -- 영하나플러스 통장
    (44, 44, 'S', '단리', '01', '입출금통장', 12, 1.5, 1.7), -- 급여하나 통장
    (45, 45, 'S', '단리', '01', '입출금통장', 12, 1.6, 1.8), -- 연금하나 통장
    (46, 46, 'S', '단리', '01', '입출금통장', 12, 1.3, 1.5), -- 주거래하나 통장
    (47, 47, 'S', '단리', '01', '입출금통장', 12, 1.7, 1.9), -- 하나 플러스 통장
    (48, 48, 'S', '단리', '01', '입출금통장', 12, 1.6, 1.8), -- 경기기회사다리 통장
    (49, 49, 'S', '단리', '01', '입출금통장', 12, 1.5, 1.7), -- 네이버페이 머니 하나 통장
    (50, 50, 'S', '단리', '01', '입출금통장', 12, 1.4, 1.6), -- 하나 주택연금 지킴이 통장
    (51, 51, 'S', '단리', '01', '입출금통장', 12, 1.5, 1.7), -- 하나 군인연금 평생안심통장
    (52, 52, 'S', '단리', '01', '입출금통장', 12, 1.2, 1.4), -- 하나 부가가치세 매입자 납부전용계좌
    (53, 53, 'S', '단리', '01', '입출금통장', 12, 1.3, 1.5), -- 행복나눔 통장
    (54, 54, 'S', '단리', '01', '입출금통장', 12, 1.4, 1.6), -- 사업자 주거래 우대통장
    (55, 55, 'S', '단리', '01', '입출금통장', 12, 1.6, 1.8), -- 함께하는 사랑 통장
    (56, 56, 'S', '단리', '01', '입출금통장', 12, 1.7, 1.9), -- 하나 국민연금 안심 통장
    (57, 57, 'S', '단리', '01', '입출금통장', 12, 1.8, 2.0), -- 하나 공무원연금 평생안심통장
    (58, 58, 'S', '단리', '01', '입출금통장', 12, 1.4, 1.6), -- 하도급협력대금 통장
    (59, 59, 'S', '단리', '01', '입출금통장', 12, 1.5, 1.7), -- 건설하나로 통장
    (60, 60, 'S', '단리', '01', '입출금통장', 12, 1.4, 1.6), -- Easy-One Pack 통장
    (61, 61, 'S', '단리', '01', '입출금통장', 12, 1.5, 1.7), -- 저축예금
    (62, 62, 'S', '단리', '01', '입출금통장', 12, 1.6, 1.8), -- 보통예금
    (63, 63, 'S', '단리', '01', '입출금통장', 12, 1.7, 1.9), -- 증권저축계좌
    (64, 64, 'S', '단리', '01', '입출금통장', 12, 1.8, 2.0), -- 기업자유예금
    (65, 65, 'S', '단리', '01', '입출금통장', 12, 1.4, 1.6), -- 하나 행복지킴이 통장
    (66, 66, 'S', '단리', '01', '입출금통장', 12, 1.6, 1.8), -- 수퍼플러스
    (67, 67, 'S', '단리', '01', '입출금통장', 12, 1.7, 1.9), -- 하나 밀리언달러 통장
    (68, 68, 'S', '단리', '01', '입출금통장', 12, 1.8, 2.0), -- 하나 빌리언달러 통장
    (69, 69, 'C', '복리', '02', '정기적금', 12, 2.0, 3.5), -- 일달러 외화적금
    (70, 70, 'C', '복리', '02', '정기적금', 12, 2.1, 3.8), -- 더 와이드(The Wide)외화적금
    (71, 71, 'C', '복리', '02', '정기적금', 12, 2.2, 4.0), -- 자녀사랑외화로유학적금
    (72, 72, 'C', '복리', '02', '정기적금', 12, 2.3, 4.2), -- 스마트팝콘 외화적립예금
    (73, 73, 'S', '단리', '01', '정기예금', 12, 2.4, 2.6), -- 외화정기예금
    (74, 74, 'S', '단리', '01', '정기예금', 12, 2.5, 2.7), -- 외화고단위플러스 정기예금(금리연동형)
    (75, 75, 'S', '단리', '01', '정기예금', 12, 2.6, 2.8), -- 외화고단위플러스 정기예금(금리확정형)
    (76, 76, 'C', '복리', '02', '정기적금', 12, 2.7, 4.5), -- HIFI PLUS외화적립예금
    (77, 77, 'S', '단리', '01', '입출금통장', 12, 1.8, 2.0), -- 외화다통화 예금
    (78, 78, 'S', '단리', '01', '입출금통장', 12, 1.9, 2.1), -- 외화수퍼플러스
    (79, 79, 'S', '단리', '01', '정기예금', 12, 2.0, 2.2), -- 외화양도성예금증서(통장식)
    (80, 80, 'S', '단리', '01', '입출금통장', 12, 2.1, 2.3), -- 글로벌페이 전용통장
    (81, 81, 'S', '단리', '01', '입출금통장', 12, 2.2, 2.4), -- 외화보통예금
    (82, 82, 'S', '단리', '01', '입출금통장', 12, 2.3, 2.5), -- 외화당좌예금
    (83, 83, 'S', '단리', '01', '정기예금', 12, 2.4, 2.6), -- 지수플러스 정기예금 안정형 24-21호(1년)
    (84, 84, 'S', '단리', '01', '정기예금', 12, 2.5, 2.7), -- 지수플러스 정기예금 적극형 24-21호(1년,고단위)
    (85, 85, 'S', '단리', '01', '정기예금', 6, 2.6, 2.8); -- 지수플러스 정기예금 적극형 24-21호(6개월)

