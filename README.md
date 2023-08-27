# HeadHunterLocations

Плагин на добавление кармы на сервер. Green-Tavern

# Как использовать:

```
  <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
```
	<dependency>
	    <groupId>com.github.MrTvistYT</groupId>
	    <artifactId>HeadHunterLocations</artifactId>
	    <version>v2</version>
	</dependency>
```


Основные методы в плагине:

```HeadHunterLocations.getKarma(<ник игрока>);```  - получить карму.

```HeadHunterLocations.settKarma(<ник игрока>, количество);``` - установить карму.

```HeadHunterLocations.addKarma(<ник игрока>, количество);``` - добавить карму.

```HeadHunterLocations.isTasked(<ник игрока>);``` - узнать есть ли задание на игрока.
