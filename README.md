# RestoReserve

## Instalar PostgreSQL

### Ubuntu/Debian
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Crear usuario y base de datos
```bash
sudo -u postgres psql

CREATE USER resto WITH PASSWORD '1234';
CREATE DATABASE resto_db OWNER resto;
\q
```

## Backend

### Configurar variables
```bash
export DB_HOST_SPRING=localhost
export DB_PORT=5432
export DB_NAME=resto_db
export DB_USER=resto
export DB_PASSWORD=1234
```

### Ejecutar
```bash
./mvnw spring-boot:run
```

La API estará en: http://localhost:8081