# Splearn 개발 가이드

## 아키텍쳐
- 헥사고날 아키텍쳐
- 도메인 모델 패턴

### 계층
- `Domain Layer`
- `Application Layer`
- `Adapter Layer`

> 외부(Actor) → 어댑터 → 애플리케이션 → 도메인
> 
> **의존성의 방향은 항상 외부에서 내부로 흘러야 한다.**

## 패키지

```bash
├── adapter
│ ├── in                  # Primary Adapter
│ │ └── web
│ └── out                 # Secondary Adapter
│     └── persistence
├── application
│   ├── provided          # 애플리케이션이 제공하는 기능 (Port)
│   └── required          # 애플리케이션이 필요로 하는 외부 기능 (Port)
└── domain                # 소중한 도메인 
```

## 헥사고날 아키텍쳐 + 클린 아키텍쳐의 허용 범위

> **도메인 클래스에 JPA 관련 애노테이션 붙이는게 괜찮은가요?**
> 
> '애노테이션 = 주석'이다. 
> - 애노테이션이 달린다고 도메인의 코드나 방향이 변하는가? → 아니다.
> - 기존 작성된 테스트 코드에 영향을 주는가? → 아니다.
> - 앞으로 작성할 테스트 코드에 영향을 주는가? → 아니다.
> - 특정 기술에 의존적인가? → 이 질문에는 이렇게 반문해보면 된다. JPA를 사용하지 않고 다른 기술을 사용했을 때 도메인을 수정해야 하는가? → 아니다.
> 
> 즉, 결론은 아무런 문제가 없다. 아무 걱정 하지 말고 사용하면 된다.
---
> **포트의 `XxxRepository`에서 Spring Data 의 Repository 의존해도 되는건가요?**
>
> Spring Data Commons에 존재하는 Repository 인터페이스는 아무런 기능을 제공하지 않는다.
> 만약 앞으로 스프링을 안 사용하는 일이 생긴다고 가정해도 (그럴일도 없겠지만) 스프링 컨테이너같은건 아무것도 필요없고 
> 딱, `spring-data-commons` 라이브러리만 추가하면 사용가능하다.
> 
> "어? 그럼 차라리 JpaRepository를 사용하면 되지 않나요?"
> 
> 근본적인 이유는 '테스트' 때문이다. 스프링 컨테이너를 띄우는 테스트는 Repository만 상속받아도 프록시로 만들어주지만
> 스프링 컨테이너를 띄우지 않고 서비스 코드를 테스트하고 싶을 때가 있다. 이럴땐 Fake로 Repository를 구현해야 하는데
> JpaRepository를 상속받게 만들었다면 Fake로 Repository를 만들기가 너무 귀찮고 힘들다. (제공하는 많은 기본 시그니처들 때문에)
> 또한, JpaRepository가 아닌 Repository(Spring Data Commons에 속한)를 상속받음으로써, 추후 JPA가 아니라 MongoDB라든지
> 다른 스토리지를 사용하더라도 아무런 문제가 없다.
> 
> 즉, 결론은 Repository 상속받아도 아무런 문제가 없다.
---
> **JPA의 변경감지를 사용하지 않고 명시적으로 `save(...)`를 호출하는 이유는?**
> 
> - 우선 Spring Data를 사용하는 것이지, Spring Data JPA를 사용하고 있는게 아니다.
> - JPA에서 다른 스토리지 ORM으로 변경된다면(예를 들어 MongoDB, JDBC) `save()` 호출은 선택이 아닌 필수다.
> - `AbstractAggreateRoot`의 도메인 이벤트 publish 같은 일부 Spring Data 인프라는 `save()` 호출 시점에 동작합니다 