# java-blackjack
### 게임 용어 설명
- Hit: 카드를 한 장 추가로 지급받는다
- Bust: 카드의 합이 21을 초과하는 경우
- Stand: 더 이상 카드를 받지 않는다
- Blackjack: 카드의 합이 21이 되는 경우
- Hand: 플레이어 혹은 딜러가 현재 가지고 있는 카드 덱을 지칭하는 용어

### 주요 구현 기능
- [ ] 이름을 가진 N명의 플레이어를 생성한다 
- [ ] 딜러를 생성한다
- [ ] 베팅 전 딜러와 모든 플레이어에게 카드를 두 장씩 지급한다
- [ ] 플레이어가 Hit을 원하면 카드를 한 장 더 지급한다
  - 플레이어가 Bust면 더 이상 카드를 지급하지 않는다
  - 아니오가 들어오면 다음 순서로 넘김
- [ ] 딜러의 카드 합이 16 이하이면 16을 초과할 때까지 카드를 지급받는다
- [ ] 딜러와 플레이어들의 승패를 결정한다

### 입출력 기능
#### 입력
- [ ] 게임에 참여할 사람의 이름을 입력받기
  - 예외 사항
    - 이름에 영문, 한글이 아닌 문자가 포함된 경우
    - 이름 길이가 1에서 5 사이가 아닌 경우
    - 플레이어가 한 명 미만인 경우
    - 입력이 구분자로 시작하거나 끝나는 경우
- [x] 플레이어별로 반복하여 카드 더 지급받을지 입력받기
  - 예외 사항
    - `y` 혹은 `n`이 아닌 경우
#### 출력
- [ ] 베팅 전 지급받은 딜러(처음 받은 1장)와 플레이어(2장)들 카드 출력
- [ ] 딜러의 카드 추가 지급 여부 출력
- [ ] 플레이어와 딜러의 전체 카드와 결과를 출력
- [ ] 최종 승패 출력
