# Vehicle Dashboard

A full-stack vehicle dashboard application.

The application recreates a vehicle dashboard interface with real-time data display, backend-driven state, and user interactions that update persisted dashboard values. 
<br>
<br>
The complete system can be deployed with Docker Compose, including the Angular frontend, Spring Boot backend, PostgreSQL database, and Adminer database viewer. 

## Live Links

- Application: `http://18.218.225.123`
- Read-only database access provided via Adminer: `http://18.218.225.123:8080/`
- Github repo: https://github.com/nztan/vehicle-dashboard
- Adminer login:
```bash
System: PostgreSQL
Server: db
Database: vehicle_dashboard
Username: readonly_user
Password: readonly123
Schema: vehicle_dashboard
```

## Localhost Setup Instructions
### Prerequisites

- Docker
- Docker Compose
- .env file
```bash
#exmaple
POSTGRES_USER=user
POSTGRES_PASSWORD=password
```

### Run the application
1. From the project root:
```bash
docker compose up -d
```
This starts up:
- PostgreSQL database
- Spring Boot backend
- Angular frontend served by Nginx
- Adminer database GUI
2. Open `http://localhost` in your browser
3. For database access: `http://localhost:8080/`
4. Adminer login and config same as above

## Tech Stack

- Frontend: Angular, TypeScript, Tailwind CSS, PrimeNG, ECharts, Lucide
- Backend: Java 17, Spring Boot, Gradle
- Database: PostgreSQL, Liquibase
- Deployment: Docker Compose, Nginx, Adminer, AWS EC2

## Architecture
Browser (Angular)
- Polling to fetch data in every 1.5 seconds

Spring Boot Backend
- VehicleSimulator (@Scheduled) generate mock up data every 2 seconds
- PostgreSQL (vehicle_reading, vehicle_setting)

## Dashboard UI
### Indicators
![img.png](assets/img.png)
<br>
- Parking brake: Speed = 0
- Check engine: RPM >= 800
- Motor status: RPM >= 500
- Battery low: battery level <= 20%