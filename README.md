# Getting Started

### Teste Itaú
Projeto de teste para o banco itaú.

### Como rodar
Na raiz do projeto execute:
```
docker-compose up -d
```

Este comando iniciará o Localstack e compilará os projetos de front-end e back-end sem a necessidade de baixar os pacotes Maven e NodeJS.

Após isso é necessário apenas executar o escript de deploy:
#####Linux:
```
sh ./infra/deploy-script-<local | prod>.sh
```
#####Windows:
```
.\infra\deploy-script-<local | prod>.bat
```

### Dependências
Este projeto apenas necessita do Docker instalado

### Problemas
EC2 não é suportado para execuções localmente [como explicado pela própria equipe da Localstack](https://github.com/localstack/localstack/issues/2560#issuecomment-667722065)
