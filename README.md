<h1 align="center">Ionov-JavaCloud</h1>
<h3 align="center">REST-сервис для загрузки и управления файлами пользователей с веб-интерфейсом.</h3>

<details>
<summary><strong>Содержание</strong></summary>

- [Для чего нужен "Ionov-JavaCloud" ?](#-для-чего-нужен-ionov-javacloud-)
- [Быстрый старт](#-быстрый-старт)
- [Как пользоваться ?](#-как-пользоваться-)
- [API Endpoints](#-api-endpoints)
- [Тестирование?](#-тестирование)

</details>

## 📋 Для чего нужен "Ionov-JavaCloud" ?

Полнофункциональное облачное хранилище файлов позволит хранить файлы на удалённом сервере и делиться ими с друзьями
или коллегами через интернет по всему миру.
После авторизации пользователи могут добавлять или скачивать ранее добавленные файлы, а так же менять их имя либо
удалять их из облака.

<h3> Детальнее про технологии использованные в облаке </h3>
<details>
<summary><strong>Детальнее</strong></summary>

- **Backend**: REST API на Spring Boot с JWT авторизацией
- **Java 17** + **Spring Boot 3**
- **Spring Security** + **JWT** для авторизации
- **Spring Data JPA** + **PostgreSQL** для работы с БД
- **Flyway** для миграций БД
- **Docker** для контейнеризации
-
- **Frontend**: Веб-приложение на Vue.js 3 с TypeScript
- **Database**: PostgreSQL для хранения метаданных
- **Storage**: Файловая система для хранения загруженных файлов

</details>

## 🛠 Быстрый старт

<h3> Быстрый старт используя Docker</h3>

```bash
  git clone --depth 1 --branch latest https://github.com/ionover/javaDip
```

Согласовано установите корректные значения в файл docker-compose.yml переменная BACK-OPTIONS_ALLOWEDORIGIN и файл
frontend/.env переменная VUE_APP_BASE_URL. Затем выполните команду:

```bash
  cd scripts && ./startAll.sh
```

<h3> Требования для запуска</h3>
<details>
<summary><strong>Требования</strong></summary>

- Docker 25 и старше
- Docker Compose v1.39.4 и старше
- Node 20 и старше
- Java 17
- Maven 3

 </details>

## 🐣 Как пользоваться ?

После успешного старта приложения -> откройте в браузере ссылку указанную в docker-compose.yml переменная
BACK-OPTIONS_ALLOWEDORIGIN. Введите логин пароль и добавьте первый файл.

### Тестовые данные

После запуска создается тестовые пользователи:

- **Login**: `admin`
- **Password**: `admin`
- **Login**: `user`
- **Password**: `user`

## 📡 API Endpoints

### Авторизация

- `POST /cloud/login` - Вход в систему
- `POST /cloud/logout` - Выход из системы

### Управление файлами

- `GET /cloud/list?limit=N` - Получить список файлов
- `POST /cloud/file?filename=name` - Загрузить файл
- `GET /cloud/file?filename=name` - Скачать файл
- `PUT /cloud/file?filename=name` - Переименовать файл
- `DELETE /cloud/file?filename=name` - Удалить файл

Все запросы (кроме login) требуют заголовок `auth-token`.

Подробная спецификация API: [CloudServiceSpecification.yaml](CloudServiceSpecification.yaml)

## 🧪 Тестирование

Код покрыт unit тестами и интеграционными тестами.
Unit тесты запускаются внутри ./startAll.sh по умолчанию
Для запуска интеграционных тестов после ./startAll.sh дождитесь пока приложение включиться и выполните команду:

### Запуск тестов

```bash
   ./runIntegrationTests.sh
```

## 📄 Лицензия

Этот проект создан в образовательных целях.
