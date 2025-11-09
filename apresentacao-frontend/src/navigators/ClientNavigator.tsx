import { Outlet, Route, Routes } from 'react-router-dom';
import ClientView from '../views/Cliente/ClientView';

/*
	ClientNavigator Rotas p telas relacionadas ao cliente.
	Mantemos as rotas mínimas aqui (index -> ClienteView) para que as páginas em si permaneçam
	implementadas dentro do cliente
*/

export default function ClientNavigator() {
	return (
		<Routes>
			<Route path="/" element={<ClientLayout />}> 
				{/* rota index: /client -> ClienteView */}
				<Route index element={<ClientView />} />
				{/* Adicionar mais rotas de cliente aninhadas aqui, por exemplo: */}
				{/* <Route path=":id" element={<ClientDetailView />} /> */}
			</Route>
		</Routes>
	);
}

function ClientLayout() {
	// Layout para rotas de cliente. Mantém minimal por enquanto: renderiza rotas aninhadas via Outlet.
	return <Outlet />;
}
