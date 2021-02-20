# Функционално програмиране за напреднали със Scala

## Лекции

* [01 – За курса](https://scala-fmi.github.io/scala-fmi-2021/lectures/01-intro.html)

## Генериране на лекции

### Setup

Имате нужда от инсталиран [pandoc](https://pandoc.org/installing.html).

Проектът има submodule зависимост към reveal.js. При/след клониране на репото инициализирайте модулите:

    git submodule update --init

### Генериране на конретна лекция

    cd lectures
    ./generate-presentation.sh <лекция>

### Генериране на всички лекции

    cd lectures
    ./build.sh
