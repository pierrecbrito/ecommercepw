CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    tipo VARCHAR(10) NOT NULL DEFAULT 'CLIENTE' CHECK (tipo IN ('CLIENTE', 'LOJISTA'))
);

CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    estoque INT NOT NULL DEFAULT 0
);

INSERT INTO usuarios (nome, email, senha, tipo) VALUES
('Admin User', 'admin@gmail.com', 'admin123', 'LOJISTA'),
('João Silva', 'joao@gmail.com', 'senha123', 'CLIENTE'),
('Maria Santos', 'maria@gmail.com', 'senha456', 'CLIENTE'),
('Pedro Oliveira', 'pedro@gmail.com', 'senha789', 'CLIENTE'),
('Ana Souza', 'ana@gmail', 'senha321', 'CLIENTE');

INSERT INTO produtos (nome, descricao, preco, estoque) VALUES
('Smartphone Galaxy S23', 'Smartphone Samsung com 256GB de armazenamento', 3999.99, 50),
('iPhone 15', 'Apple iPhone com tela Super Retina XDR', 5999.99, 30),
('Notebook Lenovo', 'Notebook com processador i7 e 16GB de RAM', 4500.00, 25),
('Monitor LG 27"', 'Monitor Full HD com taxa de atualização de 144Hz', 1200.00, 40),
('Headphone Bluetooth', 'Fone de ouvido sem fio com cancelamento de ruído', 350.00, 100),
('Mouse Gamer', 'Mouse com 12000 DPI e iluminação RGB', 180.00, 60),
('Teclado Mecânico', 'Teclado com switches blue e retroiluminação', 280.00, 45),
('SSD 1TB', 'SSD de alta velocidade com 1TB de armazenamento', 550.00, 30),
('Cadeira Gamer', 'Cadeira ergonômica com ajuste de altura e encosto reclinável', 950.00, 20),
('Webcam HD', 'Webcam com resolução 1080p e microfone integrado', 220.00, 35);

