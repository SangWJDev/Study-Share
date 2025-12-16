# 커밋 체크

커밋 완료 후 해당 이슈의 체크박스를 업데이트합니다.

## 실행 조건

사용자가 `git commit`을 완료한 후 이 명령어를 실행합니다.

## 수행 작업

### 1. 최근 커밋 확인
```bash
git log -1 --pretty=format:"%s"
```

### 2. 현재 브랜치에서 이슈 번호 추출
```bash
git branch --show-current
```
브랜치명 예시: `feature/#3-user-signup` → 이슈 번호: 3

### 3. 이슈 체크박스 업데이트

사용자에게 완료한 작업이 무엇인지 물어보세요.
그 후 `gh issue edit` 명령어로 체크박스를 업데이트합니다.

```bash
gh issue view {이슈번호} --json body -q .body
# 체크박스를 [ ] → [x]로 변경
gh issue edit {이슈번호} --body "수정된 본문"
```

### 4. 진행 상황 알림

사용자에게 현재 진행 상황을 알려주세요:
- 완료된 항목 수 / 전체 항목 수
- 남은 작업 목록

## 출력 예시

```
✅ 체크박스 업데이트 완료!

진행 상황: 2/5 완료

완료:
- [x] User 엔티티 작성
- [x] UserRepository 작성

남은 작업:
- [ ] UserService 작성
- [ ] UserController 작성
- [ ] 테스트 코드 작성
```