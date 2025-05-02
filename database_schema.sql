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

CREATE TABLE carrinhos (
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);

CREATE TABLE carrinho_itens (
    id SERIAL PRIMARY KEY,
    carrinho_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    FOREIGN KEY (carrinho_id) REFERENCES carrinhos (id),
    FOREIGN KEY (produto_id) REFERENCES produtos (id),
    UNIQUE (carrinho_id, produto_id)
);

-- Popular tabela de usuários
INSERT INTO usuarios (nome, email, senha, tipo) VALUES
('Admin User', 'admin@example.com', 'admin123', 'LOJISTA'),
('João Silva', 'joao@example.com', 'senha123', 'CLIENTE'),
('Maria Santos', 'maria@example.com', 'senha456', 'CLIENTE'),
('Pedro Oliveira', 'pedro@example.com', 'senha789', 'CLIENTE'),
('Ana Souza', 'ana@example.com', 'senha321', 'CLIENTE');

-- Popular tabela de produtos
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

-- Criar carrinhos para os usuários
INSERT INTO carrinhos (usuario_id)
SELECT id FROM usuarios;

-- Adicionar itens aos carrinhos
-- Carrinho do João
INSERT INTO carrinho_itens (carrinho_id, produto_id, quantidade)
VALUES 
((SELECT id FROM carrinhos WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'joao@example.com')), 
 (SELECT id FROM produtos WHERE nome = 'Smartphone Galaxy S23'), 1),
((SELECT id FROM carrinhos WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'joao@example.com')), 
 (SELECT id FROM produtos WHERE nome = 'Headphone Bluetooth'), 1);

-- Carrinho da Maria
INSERT INTO carrinho_itens (carrinho_id, produto_id, quantidade)
VALUES 
((SELECT id FROM carrinhos WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'maria@example.com')), 
 (SELECT id FROM produtos WHERE nome = 'iPhone 15'), 1),
((SELECT id FROM carrinhos WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'maria@example.com')), 
 (SELECT id FROM produtos WHERE nome = 'SSD 1TB'), 2);

-- Carrinho do Pedro
INSERT INTO carrinho_itens (carrinho_id, produto_id, quantidade)
VALUES 
((SELECT id FROM carrinhos WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'pedro@example.com')), 
 (SELECT id FROM produtos WHERE nome = 'Notebook Lenovo'), 1),
((SELECT id FROM carrinhos WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'pedro@example.com')), 
 (SELECT id FROM produtos WHERE nome = 'Mouse Gamer'), 1),
((SELECT id FROM carrinhos WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'pedro@example.com')), 
 (SELECT id FROM produtos WHERE nome = 'Teclado Mecânico'), 1);

-- Carrinho da Ana
INSERT INTO carrinho_itens (carrinho_id, produto_id, quantidade)
VALUES 
((SELECT id FROM carrinhos WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'ana@example.com')), 
 (SELECT id FROM produtos WHERE nome = 'Cadeira Gamer'), 1),
((SELECT id FROM carrinhos WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'ana@example.com')), 
 (SELECT id FROM produtos WHERE nome = 'Webcam HD'), 1);