# Mancala

## Board Setup
Each of the two players has his six pits in front of him. To the right of the six pits,
each player has a larger pit. At the start of the game, there are six stones in each
of the six round pits.

## Rules
### Game Play
The player who begins with the first move picks up all the stones in any of his
own six pits, and sows the stones on to the right, one in each of the following
pits, including his own big pit. No stones are put in the opponents' big pit. If the
player's last stone lands in his own big pit, he gets another turn. This can be
repeated several times before it's the other player's turn.

### Capturing Stones
During the game the pits are emptied on both sides. Always when the last stone
lands in an own empty pit, the player captures his own stone and all stones in the
opposite pit (the other player’s pit) and puts them in his own (big or little?) pit.

### The Game Ends
The game is over as soon as one of the sides runs out of stones. The player who
still has stones in his pits keeps them and puts them in his big pit. The winner of
the game is the player who has the most stones in his big pit.


>You can also find some visual explanations of the game rules by running a
>Google Search for Mancala or Kalaha game.

---
## Candidate Steps

### Setup of activities
I got some time to realtime understand the game itself and see some live playing examples. 
Based on that and on the assignment explanation I split the necessary effort into some user stories: 

- Creating Board
> Create the board with the base rules explained (2 players, 6 pits with 6 stones each player and the empty kallah)

- Regular play
> The basic move of the game consisting in clear one pit and fill the subsequent (including kallah and opponent pits) with the stones

- Next Player play
> When your play ends in some of your pits or opponent's pits, the next to play is the opponent

- Play Ending in Kallah
> When your last stone ends in your kallah you can repeat the process, so applying this validation inside the application

- Capturing Stones
> When the player last stone lands into an empty hole, the player capture the subsequent opponent's stones

- Ending Game
> When a player empty its side the game is over and the opponent collect all stones from all pits to its own kallah

Further information available at https://trello.com/b/igMelM4D/mancala-game

### Pre-Requirements
- [jdk11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [maven](https://maven.apache.org/download.cgi)
- [docker-compose](https://docs.docker.com/compose/install/)
- [make](http://gnuwin32.sourceforge.net/packages/make.htm) (for Windows)

## Tech stack
- Spring Boot (webflux and mongodb-reactive)
- MongoDB

## How to's
### Run

- `make local-run`

> [Local Swagger](http://localhost:8080/swagger-ui.html)

### Test
- `make test`

### Build with report
- `make report`

> You can access the report at `C:/{project_path}/target/site/jacoco/index.html`

### Sonar
- `make start` (to initialize sonar Docker image)
- `make sonar`

> Sonar run will work only on docker run

### Decisions
I opted to use MongoDB because it is highly scalable and the relationship between the mapped entities is not that important.
The important here in this game is the game state, not keep registry of all data.

Also the concern with SOLID and good code patterns, but for interface it was single-point usage, so I followed Rod Johnson tip 
for that. 

### Future Steps
- Use real cloud DB
- Apply CI/CD pipeline