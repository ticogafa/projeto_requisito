export {};
/*

Como a gente ta usando type script a gente cria um arquivo com esse nome pra definir os tipos

Vamo lá um exemplo de codigo que eu peguei aqui é o de botão, geralmente o que é feito é, cria esses arquivos e vai criando o componente,
no caminhar da carroagem, a gente vai ver as variaveis que a gente tem e vai ver que elas vao reclamar que não tem tipagem nenhum e que
ta com o tipo "Any" sendo assim, você vê o tipo necessário e simplesmente adiciona aqui dessa forma

export ButtonProps = {
    
    variavel1?: string;
    
    // Tá deniz, mas o que essa interrogação faz, quando eu sei que é pra usar ela??
    // Que boa pergunta amigos imaginarios que eu to vendo aqui no meu quarto as 2 da manha!!
    // essa interrogação é feita pra dizer que essa variavel não é obrigatória, basicamente
    // dizendo que ela pode ser nula, sendo assim quando a gente usar o codigo em vários lugares
    // e tiver coisas que é preciso usar em uma e na outra nn, como uma label por exemplo, a gente
    // define essa variavel como obrigatória

    variavel2: number;

    // ^ pelo amor de deus né galera, com a explicação que eu dei no comentario passado
    // nao preciso explicar esse e outras coisas o tipo depende da variavel, tipo aqui
    // no app da amigo tem vários exemplos que eu n posso colocar aqui provavelmente,
    // mas eu mostro se vcs quiserem presencial, onde a gente bota um tipo e bota pra uma
    // variavel ter um array desses tipos

}

*/
//# sourceMappingURL=Button.types.js.map