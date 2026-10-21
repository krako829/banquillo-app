CREATE TABLE jugador (
    id               BIGSERIAL    PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    dorsal           INTEGER      NOT NULL UNIQUE CHECK (dorsal BETWEEN 1 AND 99),
    posicion         VARCHAR(20)  NOT NULL,
    contacto_familia VARCHAR(150)
);
