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