#!/bin/bash

echo "=== Testando Endpoints da API de Barbearia ==="
echo ""

# Cores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verificar se backend está rodando
echo -e "${YELLOW}1. Verificando se backend está online...${NC}"
if curl -s http://localhost:8080/api/servico > /dev/null; then
    echo -e "${GREEN}✓ Backend está online${NC}"
else
    echo -e "${RED}✗ Backend não está respondendo em http://localhost:8080${NC}"
    exit 1
fi
echo ""

# Testar API de serviços
echo -e "${YELLOW}2. Testando GET /api/servico (Listar serviços)...${NC}"
SERVICOS=$(curl -s "http://localhost:8080/api/servico")
COUNT=$(echo "$SERVICOS" | python3 -c "import sys, json; print(len(json.load(sys.stdin)))" 2>/dev/null)
if [ ! -z "$COUNT" ]; then
    echo -e "${GREEN}✓ Retornou $COUNT serviços${NC}"
    echo "$SERVICOS" | python3 -m json.tool | head -20
else
    echo -e "${RED}✗ Erro ao buscar serviços${NC}"
fi
echo ""

# Testar API de profissionais disponíveis - Segunda-feira 16/12/2025 às 10h
echo -e "${YELLOW}3. Testando GET /api/agendamentos/profissionais-disponiveis${NC}"
echo "   Parâmetros: servicoId=1, dataHora=2025-12-16T10:00:00 (Segunda 10h)"
PROF_DISPONIVEIS=$(curl -s "http://localhost:8080/api/agendamentos/profissionais-disponiveis?servicoId=1&dataHora=2025-12-16T10:00:00")
COUNT_PROF=$(echo "$PROF_DISPONIVEIS" | python3 -c "import sys, json; print(len(json.load(sys.stdin)))" 2>/dev/null)
if [ "$COUNT_PROF" = "0" ]; then
    echo -e "${YELLOW}⚠ Nenhum profissional disponível (array vazio)${NC}"
    echo "   Resposta: $PROF_DISPONIVEIS"
elif [ ! -z "$COUNT_PROF" ]; then
    echo -e "${GREEN}✓ Retornou $COUNT_PROF profissionais disponíveis${NC}"
    echo "$PROF_DISPONIVEIS" | python3 -m json.tool
else
    echo -e "${RED}✗ Erro ao buscar profissionais${NC}"
    echo "   Resposta: $PROF_DISPONIVEIS"
fi
echo ""

# Testar com Quarta-feira às 15h
echo -e "${YELLOW}4. Testando novamente com Quarta-feira 18/12/2025 às 15h${NC}"
PROF_DISPONIVEIS2=$(curl -s "http://localhost:8080/api/agendamentos/profissionais-disponiveis?servicoId=1&dataHora=2025-12-18T15:00:00")
COUNT_PROF2=$(echo "$PROF_DISPONIVEIS2" | python3 -c "import sys, json; print(len(json.load(sys.stdin)))" 2>/dev/null)
if [ "$COUNT_PROF2" = "0" ]; then
    echo -e "${YELLOW}⚠ Nenhum profissional disponível (array vazio)${NC}"
elif [ ! -z "$COUNT_PROF2" ]; then
    echo -e "${GREEN}✓ Retornou $COUNT_PROF2 profissionais disponíveis${NC}"
    echo "$PROF_DISPONIVEIS2" | python3 -m json.tool
else
    echo -e "${RED}✗ Erro ao buscar profissionais${NC}"
fi
echo ""

# Testar endpoint de debug
echo -e "${YELLOW}5. Testando endpoint de debug /api/dev/test-profissionais-disponiveis${NC}"
DEBUG_RESULT=$(curl -s "http://localhost:8080/api/dev/test-profissionais-disponiveis")
COUNT_DEBUG=$(echo "$DEBUG_RESULT" | python3 -c "import sys, json; print(len(json.load(sys.stdin)))" 2>/dev/null)
if [ "$COUNT_DEBUG" = "0" ]; then
    echo -e "${YELLOW}⚠ Nenhum profissional disponível (array vazio)${NC}"
elif [ ! -z "$COUNT_DEBUG" ]; then
    echo -e "${GREEN}✓ Retornou $COUNT_DEBUG profissionais${NC}"
    echo "$DEBUG_RESULT" | python3 -m json.tool
else
    echo -e "${RED}✗ Erro no endpoint de debug${NC}"
fi
echo ""

# Verificar dados do banco
echo -e "${YELLOW}6. Verificando dados no banco MySQL...${NC}"
echo "   Profissionais qualificados para serviço 1:"
docker exec -i barbearia-container mysql -uroot -proot barbearia_db -e \
    "SELECT p.id, p.nome, ps.servicos_oferecidos_id 
     FROM profissional p 
     INNER JOIN profissional_servico ps ON p.id = ps.profissional_id 
     WHERE ps.servicos_oferecidos_id = 1 
     LIMIT 5;" 2>/dev/null | tail -n +2
echo ""

echo -e "${GREEN}=== Teste concluído ===${NC}"
