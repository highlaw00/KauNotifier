# KauNotifier

# Goal
한국항공대학교 재학생들을 위한 공지사항 알리미 서비스입니다.
하루에도 많은 수(0~20)의 교내 공지사항이 업로드 되지만, 푸시 알람이 존재하지 않아 유용하거나 중요한 공지사항을 놓치는 학우가 많습니다.
그런 학우들의 니즈를 반영해 **한국항공대학교 공지사항 알리미**를 만들고자 합니다.

# Service Architecture
## 이메일 발송 서비스 아키텍처
![Kau-notifier-email-architecture drawio](https://github.com/highlaw00/KauNotifier/assets/65754646/e55ca9d5-1f4a-4be3-be31-126600edcf21)

### 이메일 서비스 세부설명
1. 매일 밤 9시에 EventBridge Scheduler가 미리 작성된 AWS Step Functions을 트리거합니다.
2. 외부 데이터베이스(https://planetscale.com) 에 연결하여 크롤링 해야하는 URL을 가져옵니다.
3. 공지사항 페이지는 동적으로 컨텐츠가 생성되기 때문에 가상 브라우저(Puppeteer: https://pptr.dev/) 를 AWS Lambda 내에서 실행하여 크롤링합니다.
    - Puppeteer는 런타임에 브라우저가 필요합니다.
    - 따라서, 서버리스 환경을 위한 Chromium 브라우저를 지원하는 node.js 라이브러리 Chromium을 도입하였습니다.(https://github.com/Sparticuz/chromium)
4. 크롤링을 하는 도중 사이트에 문제가 발생하거나 네트워크 이슈가 발생하면 런타임에서 Timeout이 발생합니다. 이것을 모니터링하기 위해 크롤링에 실패하는 경우 AWS SNS를 사용해 개발자에게 알람을 보냅니다.
5. 크롤링이 완료되면 AWS Lambda 내부에서 AWS SES를 호출하여 구독자에게 메일을 보냅니다.
    - AWS SES에서 미리 저장해둔 메일 템플릿 참조하여 구독자들에게 메일을 발송합니다.
    - AWS SES는 병목 현상이 존재하기 때문에 Step Functions의 기능인 Map을 활용해 구독자를 일정 단위로 split하여 메일을 발송합니다.

### AWS Step Functions를 도입한 이유
- 만약, 크롤링 타겟의 컨텐츠가 동적으로 생성되지 않았다면 가상 브라우저를 사용하지 않고 HTML 데이터를 그대로 파싱하면 됨.
- 그러나, 크롤링 타겟이 동적으로 컨텐츠를 생성하기 때문에 **가상 브라우저를 필히 사용해야**하고, 이는 컴퓨팅 자원이 상당히 많이 듦.
- 이러한 크롤링 과정을 10개 이상 하나의 런타임에서 진행하는 것은 Lambda Timeout을 발생시킬 가능성이 매우 높음(최대 15분).
- AWS Lambda 내부에서 또 다른 Lambda를 호출하는 Lambda Invoke API를 활용할 수 있겠으나, Lambda Invoke API는 람다를 호출해줄 뿐이다. 즉, 호출한 자식 람다의 결과로 무언가를 하는것이 불가능하다는 것.
- 따라서 AWS Step Functions를 도입하였다.

## 구독 홈페이지 및 API 서버 아키텍처
![Kau-notifier-server-architecture drawio (1)](https://github.com/highlaw00/KauNotifier/assets/65754646/036d0152-ef26-48ff-ad39-b73c41aa6b68)


### 구독 홈페이지 세부설명
1. Route53/CloudFront를 통해 (https://kau-notifier.site) 에 접속하였을 때 AWS S3에 배포된 HTML 파일(React.js)이 반환 됨.
2. API 서버(Spring boot) 또한, Route53/CloudFront를 통해 접근할 수 있음. (HTTPS 통신을 위해 CloudFront 배포 설정)
3. API 서버는 외부 데이터베이스 (https://planetscale.com) 에 연결하여 데이터를 주고 받으며 구독 정보를 생성, 갱신, 삭제함.


# Components

### 서버

- 사용 프레임워크
- 사용 Database

- ERD 다이어그램
![image](https://github.com/highlaw00/KauNotifier/assets/65754646/6874bb62-7caf-4f09-bd0e-fd653dbeeca1)

- 클래스 다이어그램
![image](https://github.com/highlaw00/KauNotifier/assets/65754646/db9b43c2-d79f-4722-9980-66796cbe65d5)

### 웹 스크래퍼
