# Prog3

Turmeszkek
Az én automatám is úgy működik, mint a többi sejtautomata, hogy egy 2D rács minden mezője (cellája, sejtje) egy adott állapotban van (ez lehet kétállapotú – élő vagy halott), majd minden egyes iterációban a környezettől függően változik a cellák állapota. A program része egy GUI-t kezdőállapotok beállítására. Továbbá itt lehet állapotot elmenteni és visszatölteni is. Lehet a szimuláció sebességét változtatni, és a szimulációt bármikor le lehet állítani, illetve újra el lehet majd indítani. A szimuláció tehát egy 2D rácsot fog jelenteni, amelynek minden cellája 2 állapotú (0 vagy 1). Ezen  van egy aktuális pozíció és irány, amelyet egy hangya(vagy termesz) fog reprezentálni. Minden lépésben a hangya az aktuális állapotának és a cella tartalmának megfelelően csinálja a következőket:
1.	Fordul 90 fok valamilyen sokszorosával → L[eft], R[ight], N[o turn], U[-turn]
2.	Az aktuális cellába beleír valamit (0 vagy 1)
3.	Előre lép egyet
4.	Átmegy egy másik állapotba
Egy lehetséges program pl.:
[állapot-cellaérték-irány-újérték-újállapot]
0-0-R-1-0
0-1-R-1-1
1-0-N-0-0
1-1-N-0-1
Tehát 0-s állapotban, ha 0-s cellán áll, akkor jobbra fordul, 1-est ír, és marad 0-s állapotban. Ha 1-es cellán áll, akkor jobbra fordul és átmegy 1-es állapotba. Amikor 1-es állapotban van, ha 0-át talál, akkor átmegy 0-s állapotba; ha 1-et talál, akkor azt 0-ra javítja, de marad 1-es állapotban. A program által generált eredményeket png fájlba tudja menteni a felhasználó.
