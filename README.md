# Clarity Financce: Real-Time Finance & Stock Dashboard

Modern, enterprise-grade full-stack platform for managing personal expenses, budgets, borrowed/lent money, and real-time stock investment insights.

<p align="center">
  <img src="screenshot.png" alt="FortuneLedger Dashboard Screenshot" width="800"/>
</p>

---

## 🚀 Features

- **Secure Account Registration & Login** — JWT-based authentication, role-ready
- **Expense & Budget Tracking** — Add, edit, categorize expenses; set budgets and fixed costs
- **Borrowed / Lent Management** — Track money borrowed from or lent to others; automatic email notifications
- **Real-Time Stock Market Tracker** — Monitor favorite stocks with live pricing and news via public stock API
- **Interactive Dashboard** — Visualize budgets, expenses, top categories, most expensive item, and trends using Recharts
- **Transactional Email Alerts** — Automatic notifications for repayments and reminders
- **Lightning-Fast UI** — Responsive, accessible design powered by React (shadcn/ui, Tailwind) and Framer Motion
- **Production-Ready Security** — Spring Security (JWT), BCrypt passwords, PostgreSQL encryption at rest

---

## 🛠️ Tech Stack

- **Frontend:** React.js, TypeScript, shadcn/ui, Tailwind CSS, Zustand, React Query, Recharts, Framer Motion
- **Backend:** Spring Boot, Spring Security (JWT), Hibernate, PostgreSQL, JavaMail
- **Others:** Docker, RESTful APIs, CI/CD ready
- **Stock API:** Alpha Vantage (demo key), Yahoo Finance, or similar

---

## 📦 Getting Started

### Prerequisites

- Node.js (v18+), npm
- Java 17+, Maven or Gradle
- PostgreSQL (running locally or cloud)
- [Alpha Vantage API Key](https://www.alphavantage.co/support/#api-key) (for stocks)

---

### 1. Clone the Repo

```sh
git clone https://github.com/yourgithubusername/fortune-ledger.git
cd fortune-ledger
```

---

### 2. Backend Setup (`/backend`)

```sh
cd backend
# Configure PostgreSQL credentials and API keys in src/main/resources/application.properties
# Or use environment variables for prod!
./mvnw spring-boot:run
# or
./gradlew bootRun
```

- Make sure to set:
  - `spring.datasource.*`, `jwt.secret`, `spring.mail.*`, etc.

---

### 3. Frontend Setup (`/frontend`)

```sh
cd frontend
cp .env.example .env
# Edit .env to set VITE_API_URL, e.g.:
# VITE_API_URL=http://localhost:8080/api

npm install
npm run dev
```

---

## 🌈 Usage

- Visit [http://localhost:3000](http://localhost:3000)
- Register for a secure account
- Access dashboard for summary  
- Add expenses, set budgets, record borrowing/lending, and favorite stocks
- Visualize trends and get live market data!

---

## 📝 API Endpoints (Sample)

| Method | Endpoint                  | Description                                  |
|--------|---------------------------|----------------------------------------------|
| POST   | `/auth/register`          | Register new user                            |
| POST   | `/auth/login`             | Login, returns JWT                           |
| GET    | `/dashboard/summary`      | Dashboard analytics overview                 |
| POST   | `/expenses`               | Add new expense                              |
| GET    | `/expenses`               | List/filter expenses                         |
| POST   | `/budgets`                | Set monthly budget                           |
| POST   | `/borrowed`               | Log borrowed transaction                     |
| POST   | `/lent`                   | Log lent transaction                         |
| POST   | `/stocks`                 | Add favorite stock                           |
| GET    | `/stocks/live`            | Get live price/news by symbol                |

*See full [API docs](docs/API.md) for details.*

---

## 🏗️ Project Structure

```
backend/
  src/main/java/...           # Spring Boot REST API, config, models
  src/main/resources/...
frontend/
  src/api/                    # API functions
  src/components/             # Shared, shadcn/ui, charts
  src/features/               # Auth, dashboard, expenses, lent, borrowed, stocks, AI tips, ...
  src/state/                  # Zustand stores
  src/utils/                  # Helpers
  ...
```

---

## 🧑‍💻 Contributing

Contributions welcome!  
- Fork the repo and submit a PR
- Open an Issue for suggestions or bugs

---

## 📄 License

MIT

---

## ⭐️ Acknowledgments

- [shadcn/ui](https://ui.shadcn.com/)
- [Recharts](https://recharts.org/)
- [Alpha Vantage](https://www.alphavantage.co/)
- [Spring Boot](https://spring.io/projects/spring-boot)
