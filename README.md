# Функционално програмиране за напреднали със Scala

## Лекции

* [01 – За курса](https://scala-fmi.github.io/scala-fmi-2021/lectures/01-intro.html)
* [02 – Въведение във функционалното програмиране със Scala](https://scala-fmi.github.io/scala-fmi-2021/lectures/02-fp-with-scala.html)

## Генериране на лекции

### Setup

Имате нужда от инсталиран [pandoc](https://pandoc.org/installing.html).

Проектът има submodule зависимост към reveal.js. При/след клониране на репото инициализирайте модулите:

    git submodule update --init

**Local with Reaveal + Markdown**

To be able to successfully load .md files w/ the Markdown plugin and no co policy pass the `--allow-file-access-from-files` flag for google chrome. If you use a real browser and not google chrome
you are clearly an adult and can figure out how to fix it for yourself.

### Генериране на конретна лекция

    cd lectures
    ./generate-presentation.sh <лекция>

### Генериране на всички лекции

    cd lectures
    ./build.sh
