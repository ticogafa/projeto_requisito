//aqui é aonde nosso app será construido,
//  não botei em TS por ser assim q está na amigo
// e eu to aqui as 4:08 da manha, giu ta dormindo pelo amor de deus veou dormir tbm, perdao as explicacoes de gpt,
//  queria faz´r isso de forma rapida

//qualquer duvida por favor me perguntem, eu mostro nosso repo amigo funcionando, e lembrando eu codo MOBILE, talvez tenha muita coisa pra mudar ou muita coisa inutil
// mais rapido, é a gente codar em html css e js normal, inclusive não so mais rapido como mais facil, por todos do grupo ja esttarem ligados tgld

import React from 'react';
import { Provider } from 'react-redux';
import store from './src/store';
import Router from './src/routes';

function App() {
	return (
    	<Provider store={store}>
    		<Router />
		</Provider>
	);
}

export default App;
