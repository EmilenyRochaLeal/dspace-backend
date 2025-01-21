# DSpace Backend 

Este documento descreve os passos para configurar e solucionar problemas no backend do DSpace usando Docker.

## Verificar 
- Docker
- Docker Compose

   ```bash
    docker --version
    ```

    ```bash
    docker-compose --version
    ```

## Etapas de Configuração caso não tenha o docker

### 1. Atualização do Sistema e Instalação do Docker

1. Atualize o sistema:

    ```bash
    sudo apt update && sudo apt upgrade -y
    ```

2. Instale o Docker e o Docker Compose:

    ```bash
    sudo apt install docker.io docker-compose -y
    ```

3. Verifique se o Docker foi instalado:

    ```bash
    docker --version
    ```

4. Verifique se o Docker Compose foi instalado:

    ```bash
    docker-compose --version
    ```

### 2. Resolução de Problemas: Docker Compose

Caso o **docker-compose** não funcione devido ao erro do módulo `distutils`, resolva com os seguintes comandos:

1. Remova o Docker Compose antigo:

    ```bash
    sudo apt remove docker-compose -y
    ```

2. Instale o novo plugin do Docker Compose:

    ```bash
    sudo apt install docker-compose-plugin -y
    ```

3. Verifique se o problema foi resolvido:

    ```bash
    docker-compose --version
    ```

### 3. Configuração do Projeto

1. Crie uma pasta para o projeto:

    ```bash
    mkdir ~/dspaceProject
    cd ~/dspaceProject
    ```

2. Clone o repositório do backend:

    ```bash
    git clone https://github.com/DSpace/DSpace.git
    ```

3. Caso ocorra erro ao criar o repositório, remova-o e tente novamente:

    ```bash
    rm -rf DSpace
    git clone https://github.com/DSpace/DSpace.git
    ```

### 4. Inicialização dos Serviços

1. Inicie os serviços usando Docker Compose:

    ```bash
    sudo docker-compose up -d
    ```

2. Verifique o status do Docker:

    ```bash
    sudo systemctl status docker
    ```

    - Caso o status seja **loaded**, inicie o serviço manualmente:

      ```bash
      sudo systemctl start docker
      ```

3. Acompanhe os logs dos contêineres em um terminal separado:

    ```bash
    docker-compose logs -f
    ```

4. Verifique os contêineres em execução:

    ```bash
    docker ps
    ```

### 5. Resolução de Problemas: Porta `5432` Ocupada

Caso o Docker não consiga iniciar o contêiner `dspacedb` devido à porta `5432` já estar ocupada:

1. Identifique o sistema que está usando a porta:

    ```bash
    sudo lsof -i :5432
    ```

2. Pare temporariamente o PostgreSQL no host:

    ```bash
    sudo systemctl stop postgresql
    ```

3. Verifique se o PostgreSQL foi parado:

    ```bash
    sudo systemctl status postgresql
    ```

4. Rode novamente o Docker Compose:

    ```bash
    docker compose up -d
    ```

5. Quando necessário, reinicie o PostgreSQL:

    ```bash
    sudo systemctl start postgresql
    ```

### 6. Testando a Configuração

Acesse o backend do DSpace no navegador:

```text
http://localhost:8080/server
```

---

[documentação oficial do DSpace](https://github.com/DSpace/DSpace).
