## CSV-Editor

# Általános dokumentáció

## 1.1. A feladat leírása

A program feladata egy bójákból kirakott pálya megjelenítése és szerkesztése. A pálya bal oldali íve kék, jobb
oldali íve sárga bójákból van kirakva és a célvonalat narancssárga bóják jelzik. A pályákat csv fájlokban tároljuk,
amiket a program képes megnyitni és kezelni. Feladata, hogy a megnyitott fájl tartalmát kijelezze egy 2 dimenziós
felülnézeti koordináta rendszerben. Minden fájlban tárolt objektumot felrajzol a térképre annak megfelelő grafikus
megjelenítéssel (kék, sárga, narancssárga, piros kör), hogy az milyen típusú. A felhasználó képes nagyítani és
kicsinyíteni a térképen és tudja mozgatni is azt mind a négy irányba. Ezenkívül kiválasztva egy objektumot képes
változtatni a helyzetén, színén és tudja törölni is, illetve hozzá is lehet adni új elemet a térképre. A szerkesztés
befejezése után a változtatásokat el lehet menteni új fájlként és felül is lehet írni az éppen megnyitott fájlt.

## 1.2. Use-case-ek és User manual

**Fájlok megnyitása**
A felhasználó a File->Open menüpontban tudja kiválasztani a megnyitni kívánt fájlt.

**Térkép kezelése**
A nagyítás funkciót az egér görgőjével lehet elérni, illetve az erre kijelölt a jobb alsó sarokban található nagyítás,
kicsinyítés gombokkal és csúszkával egér nélküli használat esetére. A térkép mozgatását kattintással és a kurzor
húzásával lehet megvalósítani.

**Objektumok szerkesztése**
Az objektumok helyzetét a SHIFT billentyű lenyomva tartásakor az objektumra való kattintással és a kurzor húzásával lehet megváltoztatni. Objektumokat kijelölni úgy tudunk, hogy lenyomva tartjuk a SHIFT billentyűt és rákattintunk
a kijelölni kívánt objektumra. Törölni úgy tudjuk az objektumot, hogy kijelöljük és lenyomjuk a DEL gombot, vagy
az Edit->Delete menüpontban. Új objektumot hozzáadni az Edit->Add menüpontban lehet. Az új objektum a térkép
alapértelmezett 0,0 koordinátáján fog megjelenni fehér színnel. A kijelölt objektum színét megváltoztatni a jobb
felső sarokban levő szín állító menüvel tudjuk.

**Módosítások mentése**
A felhasználó a File->Save vagy File-> Save as menüpontban tudja elmenteni a változtatásait új fájlba, vagy felülírni
a jelenlegit.

**Üres fájl létrehozása**
Új fájlt nyitni a File->New file menüpontra kattintva tudunk. Alapértelmezetten nem menti el a létrehozott fájlt a
program.


**Megjelenítési beállítások**
A View->Reset viewport menüpontra kattintva tudjuk alaphelyzetbe állítani a vászon eltolását és nagyítását. A
nagyítást alap értékre tudjuk még állítani a jobb alsó sarokban lévő Default gombbal. A View->Show curve menüpontra kattintva vizualizálni tudjuk az objektumok által kirajzolt pályát. Ez a menüpont várakozással járhat nagyobb
méretű CSV fájlok esetén.

**Gyorsbillentyűk**
A sokszor használt menüpontokhoz gyorsbillentyűk tartoznak, amiket meg tudunk tekinteni az egyes menüpontok
nevei mellett.

## 1.3. Kezelt fájlok

A pályákat csv fájlokban tároljuk, aminek tartalma miden sorban a következő: egy objektum x,y koordinátája 8 tizedesjegy pontossággal vesszővel elválasztva és az objektum típusát jelölő egész szám ( 0 - narancssárga; 1 - kék; 2 - sárga; 3 - piros; 4 - fehér ). A fájlok elmentésére is ugyan ez a fájlformátum használatos. Példa az adatokra:

##### 6.8159,1.5863,2

##### 8.6708,2.4786,1

## 1.5. Tervezési megfontolások

A programban definiálva van egy osztály a bóják kezelésére. Ebben az osztályban eltároljuk az adott objektum x
és y koordinátáját double típusú változókban és a színét egy ezt kifejező egész típusú változóban.

Definiált egy a grafikus megjelenítésért felelős osztályt. Ez fogja kezelni a térképen az objektumok megjelenítését illetve a térkép megfelelő nagyítású és helyzetű megjelenítését és a menüpontok kijelzését.

Ezt az osztályt három alegység építi fel. A menü sávot megvalósító osztály, a felülnézeti koordináta rendszert
megvalósító osztály és a láblécet megvalósító osztály alkotja a megjelenítést.

A grafikus megjelenítővel való interakciókat egy erre létrehozott osztály kezeli. Emellett saját Mouse Listener osztályt is implementáltam, hogy a térképen való mozgatási nagyítási és szerkesztési műveleteket kezeljem.

Az objektumok összekötéséhez szükséges azok sorbarendezése. Erre egy külsö TSP solvert használtam fel. Ez a könyvtár genetikus algoritmussal heurisztikusan becsli az utazó ügynök probléma megoldását.
