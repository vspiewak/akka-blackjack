# Akka BlackJack

* Simulate a BlackJack game using [Akka](https://akka.io)
* Show results using [D3.js](https://d3js.org)


## Rules

* 6 Decks
* S17
* DOA
* DAS
* SPL3
* No RSA
* ENHC
* ES


## Demo

    sbt test
    
    sbt run
    open index.html


## Results

This game have 0.11% advantage, so you will win on the long run,     
but a huge [variance](https://en.wikipedia.org/wiki/Variance) is expected !   

You can beat the [variance](https://en.wikipedia.org/wiki/Variance) by increasing rounds in [Main.scala](src/main/scala/fr/dailybrain/akka/blackjack/Main.scala)    

*Note:* You usually play 150 rounds per hour, 2000 hours per year when you are a Pro.


### 1.3 Million after 1 year 
![One Million](src/main/resources/assets/one-million.png)

### Huge Win
![Huge Win](src/main/resources/assets/huge-win.png)

### Huge Loss
![Huge Loss](src/main/resources/assets/huge-loss.png)

### Toss
![Toss](src/main/resources/assets/toss.png)
