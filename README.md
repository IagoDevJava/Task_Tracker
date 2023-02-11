# java-sprint2-hw
Second sprint homework

## Это репозиторий проекта "Трекер задач"  
#### Он позволяет эффективно организовать совместную работу над задачами.

#### Архитектура монолитная. Без подключения к БД, сохранение и загрузка информации из файла

Наше приложение **умеет**:
1. Добавлять задачи, подзадачи. 
2. Распределять задачи по категориям - Задача, Эпик, Подзадача. 
3. Удалять и редактировать все типы задач.
3. Менять статусы у всех типов задач.

Приложение написано на Java. Пример кода:

```java
import managers_types.InMemoryTaskManager;

public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    }
}
```
------
О том, как научиться создавать такие приложения, можно узнать в [Яндекс-Практикуме](https://practicum.yandex.ru/java-developer/ "Тут учат Java!") 
