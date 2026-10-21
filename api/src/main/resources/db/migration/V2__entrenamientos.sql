CREATE TABLE entrenamiento (
    id       BIGSERIAL    PRIMARY KEY,
    fecha    DATE         NOT NULL UNIQUE,
    objetivo VARCHAR(200) NOT NULL
);

CREATE TABLE asistencia (
    entrenamiento_id BIGINT NOT NULL REFERENCES entrenamiento (id) ON DELETE CASCADE,
    jugador_id       BIGINT NOT NULL REFERENCES jugador (id) ON DELETE CASCADE,
    PRIMARY KEY (entrenamiento_id, jugador_id)
);
