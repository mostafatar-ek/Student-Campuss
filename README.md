# Student Campus Portal

A web-based social platform for university students built with Spring Boot and Thymeleaf. Students can connect, share posts, chat privately, and stay notified — all in one place.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2 |
| Templating | Thymeleaf |
| Database | H2 (file-based, auto-created) |
| ORM | Spring Data JPA / Hibernate |
| Email | Gmail SMTP (Spring Mail) |
| Styling | Custom CSS + Font Awesome 6 |
| Build | Maven |

---

## Features

- **Registration & Login** — Email + password with a 6-digit email verification code required before first login
- **Student Profile** — Avatar upload, name, major, bio editing; public profile viewable by any student
- **Social Feed** — Create posts with text, images, or file attachments and an optional subject tag
- **Likes & Comments** — Like/unlike posts, comment on posts; post author receives notifications
- **Student Search** — Search students by name or major
- **Direct Messaging** — One-to-one private chat with text and file attachments; auto-refreshes every 3 seconds
- **Notifications** — In-app notifications for likes, comments, and new messages with an unread badge
- **Persistent Auth** — HttpOnly cookie session with 7-day expiry

---

## Getting Started

### Prerequisites

- Java 17+
- Maven

### Run

```bash
cd student-portal
mvn spring-boot:run
```

Open your browser at `http://localhost:8008`

### Build JAR

```bash
mvn package -DskipTests
java -jar target/student-portal-0.0.1-SNAPSHOT.jar
```

---

## Configuration

All settings are in `src/main/resources/application.properties`.

| Property | Default | Description |
|---|---|---|
| `server.port` | `8008` | HTTP port |
| `spring.datasource.url` | `jdbc:h2:~/data/studentportal` | H2 file path |
| `upload.dir` | `./uploads` | Local file upload directory |
| `spring.mail.username` | — | Gmail address for sending verification emails |
| `spring.mail.password` | — | Gmail app password |

The H2 database is created automatically on first run — no setup needed.

**H2 Console** (for inspecting the database):
```
http://localhost:8008/h2-console
JDBC URL : jdbc:h2:~/data/studentportal
Username : sa
Password : (leave blank)
```

---

## Project Structure

```
src/main/java/com/studentportal/
├── controllers/        # MVC controllers (one per feature)
│   ├── AuthController.java
│   ├── FeedController.java
│   ├── ChatController.java
│   ├── ProfileController.java
│   ├── StudentMvcController.java
│   ├── NotifMvcController.java
│   └── ErrorController.java
├── services/           # Business logic
├── models/             # JPA entities (Student, Post, Comment, Like, Message, Notification)
├── repos/              # Spring Data repositories
├── dto/                # Form/view data transfer objects
└── util/
    └── CookieUtil.java # Cookie-based session management

src/main/resources/
├── templates/          # Thymeleaf HTML pages
│   └── fragments/
│       └── sidebar.html
└── static/
    └── style.css
```

---

## Data Model

| Table | Key Relationships |
|---|---|
| `students` | owns posts, comments, likes, messages, notifications |
| `posts` | belongs to a student; has many comments and likes |
| `comments` | belongs to a post and a student (author) |
| `likes` | belongs to a post and a student |
| `messages` | sender → student, receiver → student |
| `notifications` | recipient → student; type: LIKE / COMMENT / MESSAGE |

---

## Authentication

Authentication is handled manually — there is no Spring Security. Every protected route calls `CookieUtil.get(request)` and redirects to `/login` if the cookie is absent.

There is a **single user role: Student**. All registered and verified students share the same permissions.

| Action | Restricted to owner? |
|---|---|
| Edit profile / avatar | Yes |
| Delete a post | Yes — author only |
| Read messages | Yes — conversation participants only |
| View notifications | Yes — own only |
| View feed, profiles, search | No — all students |

---

## Pages & Routes

| Route | Description |
|---|---|
| `GET /login` | Login page |
| `GET /register` | Registration page |
| `POST /verify` | Email code verification |
| `GET /feed` | Main social feed |
| `GET /profile` | Own profile |
| `GET /profile/edit` | Edit profile |
| `GET /students` | Search students |
| `GET /students/{id}` | View another student's profile |
| `GET /chat` | Conversations list |
| `GET /chat/{id}` | Open a conversation |
| `POST /chat/{id}/send` | Send a message |
| `GET /notifications` | Notifications list |
| `GET /api/uploads/{filename}` | Serve uploaded files |
